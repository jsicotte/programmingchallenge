package org.jsicotte.challenge.core.dao.redis;

import org.jsicotte.challenge.auth.Token;
import org.jsicotte.challenge.core.dao.TokenDao;
import org.junit.BeforeClass;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;

public class RedisTokenDaoTest {
    private static TokenDao tokenDao;

    @BeforeClass
    public static void createDao() {
        Jedis jedis = new Jedis("localhost");
        tokenDao = new RedisTokenDao(jedis);
    }

    @Test
    public void addTokenTest() {
        String tokenString = UUID.randomUUID().toString();
        Token token = new Token(tokenString, "test", Instant.now().plus(Duration.ofMinutes(1L)));
        tokenDao.saveToken(token);

        Token persistedToken = tokenDao.getToken(tokenString);

        assertThat(persistedToken.getUsername(), equalTo(token.getUsername()));
    }

    @Test
    public void removeTokenTest() {
        String tokenString = UUID.randomUUID().toString();
        Token token = new Token(tokenString, "test", Instant.now().plus(Duration.ofMinutes(1L)));
        tokenDao.saveToken(token);

        Token persistedToken = tokenDao.getToken(tokenString);

        assertThat(persistedToken.getUsername(), equalTo(token.getUsername()));

        tokenDao.removeToken(tokenString);
        persistedToken = tokenDao.getToken(tokenString);

        assertThat(persistedToken, nullValue());
    }

    @Test
    public void tokenTimeoutTest() throws InterruptedException {
        String tokenString = UUID.randomUUID().toString();
        Token token = new Token(tokenString, "test", Instant.now().plus(Duration.ofSeconds(5)));
        tokenDao.saveToken(token);

        Token persistedToken = tokenDao.getToken(tokenString);

        assertThat(persistedToken, is(notNullValue()));
        Thread.sleep(5000L);
        persistedToken = tokenDao.getToken(tokenString);

        assertThat(persistedToken, is(nullValue()));
    }
}
