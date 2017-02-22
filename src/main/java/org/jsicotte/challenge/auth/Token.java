package org.jsicotte.challenge.auth;

import java.time.Instant;

/**
 * Created by jsicotte on 2/21/17.
 */
public class Token {
    private String tokenString;
    private String username;
    private Instant expires;

    public Token() {}


    public Token(String tokenString, String username, Instant expires) {
        this.tokenString = tokenString;
        this.username = username;
        this.expires = expires;
    }

    public String getTokenString() {
        return tokenString;
    }

    public void setTokenString(String tokenString) {
        this.tokenString = tokenString;
    }


    public Instant getExpires() {
        return expires;
    }

    public void setExpires(Instant expires) {
        this.expires = expires;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
