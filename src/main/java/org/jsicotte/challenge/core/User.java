package org.jsicotte.challenge.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jsicotte.challenge.auth.Role;

import java.security.Principal;

/**
 * Created by jsicotte on 2/20/17.
 */
public class User implements Principal {
    private String username;
    private String password;
    private String json;
    private Role role;

    public User() {

    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, Role role) {
        this.username = username;
        this.role = role;
    }

    public User(String name) {
        this.username = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    @JsonIgnore
    public String getName() {
        return null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
