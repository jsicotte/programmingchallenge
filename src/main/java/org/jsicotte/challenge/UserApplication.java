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
 * Created by jsicotte on 2/20/17.
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
        // nothing to do yet
    }

    @Override
    public void run(AppConfiguration configuration,
                    Environment environment) {
        /*
        In memory data storage for testing
         */
        // UserDao userDao = new InMemoryUserDao();
        // TokenDao tokenDao = new InMemoryTokenDao();
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:sqlite:/Users/jsicotte/Documents/workspaces/programmingtest2/user");
        UserDao userDao = new JdbcUserDao(dataSource);

        // always make sure the admin account is there
        userDao.saveUser(DEFAULT_ADMIN_USER);

        Jedis jedis = new Jedis("localhost");
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
                        .setPrefix("Bearer")
                        .buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
    }
}
