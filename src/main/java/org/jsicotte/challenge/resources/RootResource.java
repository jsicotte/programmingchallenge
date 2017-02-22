package org.jsicotte.challenge.resources;

import org.jsicotte.challenge.auth.Role;
import org.jsicotte.challenge.core.User;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by jsicotte on 2/20/17.
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RootResource {

    @GET
    @PermitAll
    public Object getMainPage(@Context SecurityContext securityContext) {
        User principal = (User) securityContext.getUserPrincipal();
        if(principal.getRole().equals(Role.GUEST)) {
            return "hello world";
        } else {
            return principal;
        }
    }
}
