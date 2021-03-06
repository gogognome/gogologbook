package nl.gogognome.gogologbook.dbinsinglefile;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import nl.gogognome.gogologbook.dao.DAOException;
import nl.gogognome.gogologbook.entities.User;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.Files;

public class SingleFileUserDAOTest extends AbstractSingleFileDAOTest {

    private static final String INSERT_OF_ONE_USER = METADATA + "insert;User;{\"name\":\"test\",\"active\":true,\"id\":1}";
    private final SingleFileDatabase singleFileDatabase = new SingleFileDatabase(dbFile);
    private final SingleFileUserDAO userDao = new SingleFileUserDAO(singleFileDatabase);

    @Test
    public void createRecordShouldWriteRecordInDbFile() throws IOException {
        User user = new User();
        user.name = "test";
        user.active = true;
        userDao.createUser(user);

        assertEquals(INSERT_OF_ONE_USER, getContentsOfDbFile());
    }

    @Test
    public void dbShouldContainTwoRecordsAfterTwoRecordsHaveBeenCreated() throws IOException {
        User user = new User();
        user.name = "name1";
        userDao.createUser(user);

        user.name = "name2";
        userDao.createUser(user);

        List<User> users = userDao.findAllUsers();

        assertEquals(2, users.size());
    }

    @Test
    public void absentDbFileLeadsToEmptyDatabase() {
        assertFalse(dbFile.exists());

        List<User> users = userDao.findAllUsers();

        assertTrue(users.isEmpty());
    }

    @Test
    public void dbShouldHaveOneUserAfterReadingOneUserFromFile() throws IOException {
        Files.write(INSERT_OF_ONE_USER, dbFile, Charsets.ISO_8859_1);

        List<User> users = userDao.findAllUsers();

        assertFalse(users.isEmpty());
    }

    @Test(expected = DAOException.class)
    public void updateNonExistingUserShouldThrowException() {
        User user = new User(123);
        user.name = "Piet";
        userDao.updateUser(user);
    }

    @Test
    public void shouldUpdateExistingUser() {
        User user = new User();
        user.name = "Piet";
        user = userDao.createUser(user);

        User newUser = new User(user.id);
        newUser.name = "Peter";
        userDao.updateUser(newUser);

        List<User> foundUsers = userDao.findAllUsers();
        assertEquals(1, foundUsers.size());
        assertEquals(newUser.name, foundUsers.get(0).name);
    }

    @Test
    public void shouldDeleteExistingUser() {
        User user = new User();
        user.name = "Peter";
        userDao.createUser(user);

        List<User> foundUsers = userDao.findAllUsers();
        userDao.deleteUser(foundUsers.get(0).id);

        foundUsers = userDao.findAllUsers();
        assertTrue(foundUsers.isEmpty());
    }

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

    @Test(expected = DAOException.class)
    public void shouldThrowExceptionWhenDeletingNonExistingUser() {
        userDao.deleteUser(123);
    }

    private String getContentsOfDbFile() throws IOException {
        return Joiner.on("\n").join(Files.readLines(dbFile, Charsets.ISO_8859_1));
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