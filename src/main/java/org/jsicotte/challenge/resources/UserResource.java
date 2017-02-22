package org.jsicotte.challenge.resources;

import org.jsicotte.challenge.core.User;
import org.jsicotte.challenge.core.dao.UserDao;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
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
    @PermitAll
    public void addUser(User user) {
        userDao.saveUser(user);
    }

    @PUT
    @RolesAllowed({"USER"})
    public void updateUser(User user) {
        User fetchedUser = userDao.findUserByName(user.getUsername());

        fetchedUser.setJson(user.getJson());

        userDao.saveUser(user);
    }

    @DELETE
    @RolesAllowed({"USER"})
    public void removeUser(@QueryParam("username") String username) {
        userDao.removeUserByUsername(username);
    }
}
