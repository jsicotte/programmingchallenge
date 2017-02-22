package org.jsicotte.challenge.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import org.jsicotte.challenge.core.User;
import org.jsicotte.challenge.core.dao.TokenDao;
import org.jsicotte.challenge.core.dao.UserDao;

import java.time.Instant;
import java.util.Optional;

/**
 * Authenticator for a string token.
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
        if(tokenString == null) {
            return Optional.empty();
        }

        Token token = tokenDao.getToken(tokenString);
        if(token == null) {
            return Optional.empty();
        }

        Instant expires = token.getExpires();
        Instant now = Instant.now();

        /*
        Though expiration may be handled by the underlying storage mechanism (for example Redis), perform this check
        anyway. Should the underlying data store change, expiration will be unaffected.
         */
        if(expires.isBefore(now)) {
            return Optional.empty();
        }

        // Find the user and mark them as Role.USER since they are known to the system
        User user = userDao.findUserByName(token.getUsername());
        user.setRole(Role.USER);

        return Optional.of(user);
    }
}