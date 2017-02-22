package org.jsicotte.challenge.auth;

import org.jsicotte.challenge.core.User;

import java.time.Instant;

/**
 * Created by jsicotte on 2/21/17.
 */
public class Token {
    private String tokenString;
    private User user;
    private Instant expires;

    public Token(String tokenString, User user, Instant expires) {
        this.tokenString = tokenString;
        this.user = user;
        this.expires = expires;
    }

    public String getTokenString() {
        return tokenString;
    }

    public void setTokenString(String tokenString) {
        this.tokenString = tokenString;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instant getExpires() {
        return expires;
    }

    public void setExpires(Instant expires) {
        this.expires = expires;
    }
}
