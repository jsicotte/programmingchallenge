package org.jsicotte.challenge.core.dao.sqlite;

import org.apache.commons.codec.digest.DigestUtils;
import org.jsicotte.challenge.core.User;
import org.jsicotte.challenge.core.dao.UserDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementation of the UserDao for SQLite. This DAO expects the following schema to exist:
 * <pre>
 *     create table user (username varcar(30) primary key, password varcar(256), json text);
 * </pre>
 */
public class JdbcUserDao implements UserDao {
    /*
     Implementation note: the passwords are hashed using SHA-1 for now. The author is aware that better hashing functions
     exist and would most likely provide better cryptographic properties.
     */

    private JdbcTemplate jdbcTemplate;

    public JdbcUserDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public User findUserByNameAndPassword(String username, String password) {
        User fetchedUser = jdbcTemplate.queryForObject("select * from user where username = ? and password = ?",
                new UserRowMapper(), username, DigestUtils.sha1Hex(password));

        return fetchedUser;
    }

    @Override
    public void saveUser(User user) {
        // save or update
        User fetchedUser = null;
        try {
            fetchedUser = findUserByName(user.getUsername());
        } catch (EmptyResultDataAccessException e) {

        }

        if (fetchedUser == null) {
            String hashedPassword = DigestUtils.sha1Hex(user.getPassword());
            jdbcTemplate.update("insert into user (username, password, json) values (?, ?, ?)",
                    user.getUsername(), hashedPassword, user.getJson());
        } else {
            jdbcTemplate.update("update user set json = ? where username = ?",
                    user.getJson(), user.getUsername());
        }
    }

    @Override
    public void removeUserByUsername(String username) {
        jdbcTemplate.update("delete from user where username = ?", username);
    }

    @Override
    public User findUserByName(String username) {
        User fetchedUser = jdbcTemplate.queryForObject("select * from user where username = ?",
                new UserRowMapper(), username);

        return fetchedUser;
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            User user = new User();
            user.setJson(resultSet.getString("json"));
            user.setPassword(resultSet.getString("password"));
            user.setUsername(resultSet.getString("username"));
            return user;
        }
    }
}
