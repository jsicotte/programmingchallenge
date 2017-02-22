package org.jsicotte.challenge.core.dao.memory;

import org.jsicotte.challenge.auth.Token;
import org.jsicotte.challenge.core.dao.TokenDao;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jsicotte on 2/21/17.
 */
public class InMemoryTokenDao implements TokenDao {
    private Map<String, Token> tokenStore = new HashMap<>();
    @Override
    public Token getToken(String tokenString) {
        return tokenStore.get(tokenString);
    }

    @Override
    public void saveToken(Token token) {
        tokenStore.put(token.getTokenString(), token);
    }

    @Override
    public void removeToken(String tokenString) {
        tokenStore.remove(tokenString);
    }
}
