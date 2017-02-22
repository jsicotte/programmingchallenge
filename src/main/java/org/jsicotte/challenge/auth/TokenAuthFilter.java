package org.jsicotte.challenge.auth;

import io.dropwizard.auth.AuthFilter;
import org.jsicotte.challenge.core.User;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import java.io.IOException;

/**
 * Created by jsicotte on 2/21/17.
 */
public class TokenAuthFilter extends AuthFilter<String, User> {
    public TokenAuthFilter() {
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String credentials = containerRequestContext.getHeaders().getFirst("access-token");

        if(!this.authenticate(containerRequestContext, credentials, "BASIC")) {
            throw new WebApplicationException(this.unauthorizedHandler.buildResponse(this.prefix, this.realm));
        }
    }

    public static class Builder extends AuthFilterBuilder<String, User, TokenAuthFilter> {
        @Override
        protected TokenAuthFilter newInstance() {
            return new TokenAuthFilter();
        }
    }
}
