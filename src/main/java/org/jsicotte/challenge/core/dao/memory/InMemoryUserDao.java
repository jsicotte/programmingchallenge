package org.jsicotte.challenge.core.dao.memory;

import org.jsicotte.challenge.core.User;
import org.jsicotte.challenge.core.dao.UserDao;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jsicotte on 2/21/17.
 */
public class InMemoryUserDao implements UserDao {
    private Map<String, User> users = new HashMap<>();
    private Map<String, String> passwords = new HashMap<>();

    @Override
    public User findUserByNameAndPassword(String username, String password) {
        if(username.equals("jsicotte") && password.equals("password")) {
            return new User("jsicotte");
        } else {
            User user = users.get(username);
            String fetchedPassword = passwords.get(username);
            if(user != null && password.equals(fetchedPassword)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void saveUser(User user) {
        users.put(user.getUsername(), user);
        passwords.put(user.getUsername(), user.getPassword());
    }

    @Override
    public void removeUserByUsername(String username) {
        users.remove(username);
        passwords.remove(username);
    }

    @Override
    public User findUserByName(String username) {
        return users.get(username);
    }
}
