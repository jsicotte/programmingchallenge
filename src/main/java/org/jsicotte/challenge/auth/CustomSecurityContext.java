package org.jsicotte.challenge.auth;

import org.jsicotte.challenge.core.User;

import javax.ws.rs.core.SecurityContext;

/**
 * Custom security context to assist in resolving roles
 */
public class CustomSecurityContext implements SecurityContext {
    private final User principal;
    private final SecurityContext securityContext;

    public CustomSecurityContext(User principal, SecurityContext securityContext) {
        this.principal = principal;
        this.securityContext = securityContext;
    }

    @Override
    public User getUserPrincipal() {
        return principal;
    }

    @Override
    public boolean isUserInRole(String role) {
        return role.equals(principal.getRole().name());
    }

    @Override
    public boolean isSecure() {
        return securityContext.isSecure();
    }

    @Override
    public String getAuthenticationScheme() {
        return "CUSTOM_TOKEN";
    }
}