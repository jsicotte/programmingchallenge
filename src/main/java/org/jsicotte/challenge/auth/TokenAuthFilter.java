package org.jsicotte.challenge.auth;

import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.AuthenticationException;
import org.jsicotte.challenge.core.User;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.util.Optional;

/**
 * Token authentication filter
 */
@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class TokenAuthFilter extends AuthFilter<String, User> {
    private static final String ACCESS_TOKEN_HEADER = "access-token";
    private static final User DEFAULT_USER = new User("guest", Role.GUEST);
    private static final Optional<User> OPTIONAL_DEFAULT_USER = Optional.of(DEFAULT_USER);

    public TokenAuthFilter() {
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String credentials = containerRequestContext.getHeaders().getFirst(ACCESS_TOKEN_HEADER);

        Optional<User> user = Optional.empty();
        try {
            user = authenticator.authenticate(credentials);
        } catch (AuthenticationException e) {
            throw new WebApplicationException("Unable to validate credentials", Response.Status.UNAUTHORIZED);
        }

        if (!user.isPresent()) {
            // default to the guest user
            user = OPTIONAL_DEFAULT_USER;
        }

        SecurityContext securityContext = new CustomSecurityContext(user.get(), containerRequestContext.getSecurityContext());
        containerRequestContext.setSecurityContext(securityContext);
    }

    public static class Builder extends AuthFilterBuilder<String, User, TokenAuthFilter> {
        @Override
        protected TokenAuthFilter newInstance() {
            return new TokenAuthFilter();
        }
    }
}
