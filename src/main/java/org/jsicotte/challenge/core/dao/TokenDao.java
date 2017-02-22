package org.jsicotte.challenge.core.dao;

import org.jsicotte.challenge.auth.Token;

/**
 * Created by jsicotte on 2/21/17.
 */
public interface TokenDao {
    Token getToken(String tokenString);
    void saveToken(Token token);
    void removeToken(String tokenString);
}
