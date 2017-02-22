package org.jsicotte.challenge.core.dao.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jsicotte.challenge.auth.Token;
import org.jsicotte.challenge.core.dao.TokenDao;
import redis.clients.jedis.Jedis;

import java.io.IOException;

/**
 * Implementation of the TokenDao for Redis
 */
public class RedisTokenDao implements TokenDao {
    private ObjectMapper mapper = new ObjectMapper();
    private Jedis jedis;

    public RedisTokenDao(Jedis jedis) {
        this.jedis = jedis;
        mapper.registerModule(new JavaTimeModule());
    }

    @Override
    public Token getToken(String tokenString) {
        String value = jedis.get(tokenString);
        if (value == null) {
            return null;
        }

        try {
            Token token = mapper.readValue(value, Token.class);
            return token;
        } catch (IOException e) {
            throw new RuntimeException("cannot serialize token", e);
        }
    }

    @Override
    public void saveToken(Token token) {

        String tokenString = token.getTokenString();
        try {
            String jsonTokenString = mapper.writeValueAsString(token);
            this.jedis.set(tokenString, jsonTokenString);
            this.jedis.expireAt(tokenString, token.getExpires().getEpochSecond());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("cannot serialize token", e);
        }
    }

    @Override
    public void removeToken(String tokenString) {
        jedis.del(tokenString);
    }
}
