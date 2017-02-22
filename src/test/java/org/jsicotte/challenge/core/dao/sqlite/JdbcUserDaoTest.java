package org.jsicotte.challenge.core.dao.sqlite;

import org.jsicotte.challenge.core.User;
import org.jsicotte.challenge.core.dao.UserDao;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;

public class JdbcUserDaoTest {
    private static final String TEST_USERNAME = "jsicotte";
    private static final String TEST_PASSWORD = "123456";
    private static final User TEST_USER = new User(TEST_USERNAME, TEST_PASSWORD);
    private static UserDao userDao;

    @BeforeClass
    public static void setupDatasource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:sqlite:/Users/jsicotte/Documents/workspaces/programmingtest2/user");
        userDao = new JdbcUserDao(dataSource);
        userDao.removeUserByUsername("jsicotte");
    }

    @Test
    public void testInsert() {
        userDao.saveUser(TEST_USER);

        User fetchUser = userDao.findUserByName(TEST_USERNAME);

        assertThat(fetchUser.getUsername(), equalTo(TEST_USERNAME));
    }

    @Test
    public void testUpdate() {
        String testJson = "test value";
        userDao.saveUser(TEST_USER);

        User fetchUser = userDao.findUserByName(TEST_USERNAME);

        assertThat(fetchUser.getUsername(), equalTo(TEST_USERNAME));
        assertThat(fetchUser.getJson(), equalTo(TEST_USER.getJson()));
        assertThat(fetchUser.getPassword(), not(equalTo(TEST_USER.getPassword())));

        fetchUser.setJson(testJson);
        userDao.saveUser(fetchUser);

        fetchUser = userDao.findUserByName(TEST_USERNAME);

        assertThat(fetchUser.getUsername(), equalTo(TEST_USERNAME));
        assertThat(fetchUser.getJson(), equalTo("test value"));
        assertThat(fetchUser.getPassword(), not(equalTo(testJson)));
    }

    @Test
    public void testFindByUsernameAndPassword() {
        userDao.saveUser(TEST_USER);

        User fetchedUser = userDao.findUserByNameAndPassword(TEST_USERNAME, TEST_PASSWORD);

        assertThat(fetchedUser.getUsername(), equalTo(TEST_USERNAME));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void testDelete() {
        userDao.saveUser(TEST_USER);

        User fetchUser = userDao.findUserByName(TEST_USERNAME);

        assertThat(fetchUser, notNullValue());

        userDao.removeUserByUsername(TEST_USERNAME);
        userDao.findUserByName(TEST_USERNAME);
    }
}
