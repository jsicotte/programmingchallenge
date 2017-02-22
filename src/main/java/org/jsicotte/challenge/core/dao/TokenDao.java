package org.jsicotte.challenge.core.dao;

import org.jsicotte.challenge.auth.Token;

/**
 * DAO Interface for token management
 */
public interface TokenDao {
    Token getToken(String tokenString);
    void saveToken(Token token);
    void removeToken(String tokenString);
}
