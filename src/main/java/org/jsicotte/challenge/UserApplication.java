package org.jsicotte.challenge;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jsicotte.challenge.auth.TokenAuthFilter;
import org.jsicotte.challenge.auth.TokenAuthenticator;
import org.jsicotte.challenge.conf.AppConfiguration;
import org.jsicotte.challenge.core.User;
import org.jsicotte.challenge.core.dao.TokenDao;
import org.jsicotte.challenge.core.dao.UserDao;
import org.jsicotte.challenge.core.dao.memory.InMemoryTokenDao;
import org.jsicotte.challenge.core.dao.memory.InMemoryUserDao;
import org.jsicotte.challenge.resources.AuthResource;
import org.jsicotte.challenge.resources.RootResource;
import org.jsicotte.challenge.resources.UserResource;

/**
 * Created by jsicotte on 2/20/17.
 */
public class UserApplication extends Application<AppConfiguration> {
    public static void main(String[] args) throws Exception {
        new UserApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<AppConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(AppConfiguration configuration,
                    Environment environment) {
        UserDao userDao = new InMemoryUserDao();
        TokenDao tokenDao = new InMemoryTokenDao();
        AuthResource authResource = new AuthResource(userDao, tokenDao);
        TokenAuthenticator tokenAuthenticator = new TokenAuthenticator(tokenDao);
        UserResource userResource = new UserResource(userDao);

        environment.jersey().register(new RootResource());
        environment.jersey().register(authResource);
        environment.jersey().register(userResource);
        environment.jersey().register(new AuthDynamicFeature(
                new TokenAuthFilter.Builder()
                        .setAuthenticator(tokenAuthenticator)
                        .setPrefix("Bearer")
                        .buildAuthFilter()));
    }
}
