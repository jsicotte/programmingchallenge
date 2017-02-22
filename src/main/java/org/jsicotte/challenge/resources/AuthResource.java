package org.jsicotte.challenge.resources;

import org.jsicotte.challenge.auth.Token;
import org.jsicotte.challenge.core.dao.TokenDao;
import org.jsicotte.challenge.core.User;
import org.jsicotte.challenge.core.dao.UserDao;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/**
 * Created by jsicotte on 2/21/17.
 */
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {
    private UserDao userDao;
    private TokenDao tokenDao;

    public AuthResource(UserDao userDao, TokenDao tokenDao) {
        this.userDao = userDao;
        this.tokenDao = tokenDao;
    }

    @POST
    @PermitAll
    public Token createToken(@QueryParam("username") String username, @QueryParam("password") String password) {
        User user = userDao.findUserByNameAndPassword(username, password);

        String tokenString = UUID.randomUUID().toString();
        Instant expirationInstant = Instant.now().plus(Duration.ofSeconds(1200));
        Token token = new Token(tokenString, user.getUsername(), expirationInstant);

        tokenDao.saveToken(token);

        return token;
    }


    @DELETE
    @RolesAllowed({"USER"})
    public void removeToken(@PathParam("access_token") String accessToken) {
        tokenDao.removeToken(accessToken);
    }
}
