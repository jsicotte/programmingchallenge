package org.jsicotte.challenge;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jsicotte.challenge.auth.TokenAuthFilter;
import org.jsicotte.challenge.auth.TokenAuthenticator;
import org.jsicotte.challenge.conf.AppConfiguration;
import org.jsicotte.challenge.core.User;
import org.jsicotte.challenge.core.dao.TokenDao;
import org.jsicotte.challenge.core.dao.UserDao;
import org.jsicotte.challenge.core.dao.redis.RedisTokenDao;
import org.jsicotte.challenge.core.dao.sqlite.JdbcUserDao;
import org.jsicotte.challenge.resources.AuthResource;
import org.jsicotte.challenge.resources.RootResource;
import org.jsicotte.challenge.resources.UserResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import redis.clients.jedis.Jedis;

/**
 * Entrypoint into the application.
 */
public class UserApplication extends Application<AppConfiguration> {
    private static final User DEFAULT_ADMIN_USER = new User("jsicotte","password");
    public static void main(String[] args) throws Exception {
        new UserApplication().run(args);
    }

    @Override
    public String getName() {
        return "user-app";
    }

    @Override
    public void initialize(Bootstrap<AppConfiguration> bootstrap) {
    }

    @Override
    public void run(AppConfiguration configuration, Environment environment) {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(configuration.getSqliteUrl());
        UserDao userDao = new JdbcUserDao(dataSource);

        // always make sure the admin account is there
        userDao.saveUser(DEFAULT_ADMIN_USER);

        Jedis jedis = new Jedis(configuration.getRedisHost());
        TokenDao tokenDao = new RedisTokenDao(jedis);

        AuthResource authResource = new AuthResource(userDao, tokenDao);
        TokenAuthenticator tokenAuthenticator = new TokenAuthenticator(tokenDao, userDao);
        UserResource userResource = new UserResource(userDao);

        environment.jersey().register(new RootResource());
        environment.jersey().register(authResource);
        environment.jersey().register(userResource);
        environment.jersey().register(new AuthDynamicFeature(
                new TokenAuthFilter.Builder()
                        .setAuthenticator(tokenAuthenticator)
                        .setPrefix("CUSTOM_TOKEN")
                        .buildAuthFilter()));

        environment.jersey().register(RolesAllowedDynamicFeature.class);
    }
}
