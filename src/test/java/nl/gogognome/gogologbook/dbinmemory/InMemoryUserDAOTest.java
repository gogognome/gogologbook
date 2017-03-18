package nl.gogognome.gogologbook.dbinmemory;

import static org.junit.Assert.*;

import java.util.List;

import nl.gogognome.gogologbook.dao.DAOException;
import nl.gogognome.gogologbook.entities.User;

import org.junit.Test;

public class InMemoryUserDAOTest {

    private final InMemoryUserDAO userDao = new InMemoryUserDAO();

    @Test
    public void findAllUsersShouldFindActiveUser() {
        User user = createActiveUser();

        List<User> foundUsers = userDao.findAllUsers();

        assertEquals(1, foundUsers.size());
        User foundUser = foundUsers.get(0);
        assertNotSame(user, foundUser);
        assertEquals(user.name, foundUser.name);
        assertTrue(user.active);
    }

    @Test
    public void findAllUsersShouldFindInActiveUser() {
       User user = createInactiveUser();

        List<User> foundUsers = userDao.findAllUsers();

        assertEquals(1, foundUsers.size());
        User foundUser = foundUsers.get(0);
        assertNotSame(user, foundUser);
        assertEquals(user.name, foundUser.name);
        assertFalse(user.active);
    }

    @Test
    public void findAllActiveUsersShouldNotFindInactiveUser() {
        createInactiveUser();

        List<User> foundUsers = userDao.findAllActiveUsers();

        assertEquals(0, foundUsers.size());
    }

    @Test
    public void findAllActiveUsersShouldFindActiveUser() {
        User user = createActiveUser();

        List<User> foundUsers = userDao.findAllActiveUsers();

        assertEquals(1, foundUsers.size());
        User foundUser = foundUsers.get(0);
        assertNotSame(user, foundUser);
        assertEquals(user.name, foundUser.name);
        assertTrue(user.active);
    }

    @Test
    public void createTwoUsersShouldGenerateIncreasingIds() {
        User user1 = new User();
        user1.name = "test1";
        userDao.createUser(user1);

        User user2 = new User();
        user2.name = "test2";
        userDao.createUser(user2);

        List<User> foundMessages = userDao.findAllUsers();

        assertEquals(2, foundMessages.size());
        User founduser1 = foundMessages.get(0);
        int id1 = founduser1.id;

        User founduser2 = foundMessages.get(1);
        assertEquals(id1 + 1, founduser2.id);
    }

    @Test
    public void createTwoUsersShouldBeFoundInOrderOfCreation() {
        User user1 = new User();
        user1.name = "test1";
        userDao.createUser(user1);

        User user2 = new User();
        user2.name = "test2";
        userDao.createUser(user2);

        List<User> foundMessages = userDao.findAllUsers();

        assertEquals(2, foundMessages.size());
        User founduser1 = foundMessages.get(0);
        assertEquals(user1.name, founduser1.name);

        User founduser2 = foundMessages.get(1);
        assertEquals(user2.name, founduser2.name);
    }

    @Test
    public void shouldDeleteExistingUser() {
        createActiveUser();

        List<User> foundUsers = userDao.findAllUsers();
        userDao.deleteUser(foundUsers.get(0).id);

        foundUsers = userDao.findAllUsers();
        assertTrue(foundUsers.isEmpty());
    }

    @Test(expected = DAOException.class)
    public void shouldThrowExceptionWhenDeletingNonExistingUser() {
        userDao.deleteUser(123);
    }

    private User createActiveUser() {
        User user = new User();
        user.name = "Piet Puk";
        user.active = true;
        userDao.createUser(user);
        return user;
    }

    private User createInactiveUser() {
        User user = new User();
        user.name = "Piet Puk";
        user.active = false;
        return userDao.createUser(user);
    }

}
