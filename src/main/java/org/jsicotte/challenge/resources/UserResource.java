package org.jsicotte.challenge.resources;

import io.dropwizard.auth.Auth;
import org.jsicotte.challenge.core.User;
import org.jsicotte.challenge.core.dao.UserDao;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by jsicotte on 2/21/17.
 */
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    private UserDao userDao;

    public UserResource(UserDao userDao) {
        this.userDao = userDao;
    }

    @POST
    public void addUser(User user) {
        userDao.saveUser(user);
    }

    @PUT
    public void updateUser(@Auth User user) {
        User fetchedUser = userDao.findUserByName(user.getUsername());

        fetchedUser.setJson(user.getJson());

        userDao.saveUser(user);
    }

    @DELETE
    public void removeUser(@Auth User user, String username) {
        userDao.removeUserByUsername(username);
    }
}
