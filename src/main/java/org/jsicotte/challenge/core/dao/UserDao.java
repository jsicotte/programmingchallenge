package org.jsicotte.challenge.core.dao;

import org.jsicotte.challenge.core.User;

/**
 * Created by jsicotte on 2/21/17.
 */
public interface UserDao {
    User findUserByNameAndPassword(String username, String password);
    void saveUser(User user);
    void removeUserByUsername(String username);
    User findUserByName(String username);
}
