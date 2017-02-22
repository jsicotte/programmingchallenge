package org.jsicotte.challenge.auth;

/**
 * Authorization roles.
 *<ul>
 *     <li>GUEST represents any user (including one without any authentication / authorization</li>
 *     <li>USER represents a known user</li>
 *</ul>
 */
public enum Role {
    GUEST, USER
}
