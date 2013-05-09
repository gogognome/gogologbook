package nl.gogognome.gogologbook.dbinsinglefile;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import nl.gogognome.gogologbook.entities.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.Files;

public class SingleFileUserDAOTest {

	private static final String INSERT_OF_ONE_USER = "insert;User;{\"name\":\"test\",\"id\":1}";
	private final File dbFile = new File("target/test/testdb.txt");
	private final SingleFileDatabase singleFileDatabase = new SingleFileDatabase(dbFile);
	private final SingleFileUserDAO userDao = new SingleFileUserDAO(singleFileDatabase);

	@Before
	public void setUp() throws InterruptedException {
		dbFile.getParentFile().mkdirs();
		deleteDbFile();
	}

	@After
	public void tearDown() throws InterruptedException {
		deleteDbFile();
	}

	private void deleteDbFile() throws InterruptedException {
		while (dbFile.exists()) {
			dbFile.delete();
			if (dbFile.exists()) {
				Thread.sleep(100);
			}
		}
	}

	@Test
	public void createRecordShouldWriteRecordInDbFile() throws IOException {
		User message = new User();
		message.name = "test";
		userDao.createUser(message);

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

	private String getContentsOfDbFile() throws IOException {
		return Joiner.on("\n").join(Files.readLines(dbFile, Charsets.ISO_8859_1));
	}
}