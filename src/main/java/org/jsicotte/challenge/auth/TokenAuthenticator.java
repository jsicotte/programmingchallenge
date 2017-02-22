package org.jsicotte.challenge.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import org.jsicotte.challenge.core.User;
import org.jsicotte.challenge.core.dao.TokenDao;
import org.jsicotte.challenge.core.dao.UserDao;

import java.time.Instant;
import java.util.Optional;

/**
 * Created by jsicotte on 2/20/17.
 */
public class TokenAuthenticator implements Authenticator<String, User> {
    private TokenDao tokenDao;
    private UserDao userDao;

    public TokenAuthenticator(TokenDao tokenDao, UserDao userDao) {
        this.tokenDao = tokenDao;
        this.userDao = userDao;
    }

    @Override
    public Optional<User> authenticate(String tokenString) throws AuthenticationException {
        Token token = tokenDao.getToken(tokenString);
        if(token == null) {
            return Optional.empty();
        }

        Instant expires = token.getExpires();
        Instant now = Instant.now();

        if(expires.isBefore(now)) {
            return Optional.empty();
        }

        User user = userDao.findUserByName(token.getUsername());

        return Optional.of(user);
    }
}