package org.jsicotte.challenge.resources;

import io.dropwizard.auth.Auth;
import org.jsicotte.challenge.core.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by jsicotte on 2/20/17.
 */
@Path("/")
public class RootResource {
    @GET
    public String getMainPage(@Auth User user) {
        return "hello world";
    }
}
