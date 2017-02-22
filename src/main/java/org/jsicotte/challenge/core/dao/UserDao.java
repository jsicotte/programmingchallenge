package org.jsicotte.challenge.core.dao;

import org.jsicotte.challenge.core.User;

/**
 * DAO Interface for user management
 */
public interface UserDao {
    User findUserByNameAndPassword(String username, String password);
    void saveUser(User user);
    void removeUserByUsername(String username);
    User findUserByName(String username);
}
