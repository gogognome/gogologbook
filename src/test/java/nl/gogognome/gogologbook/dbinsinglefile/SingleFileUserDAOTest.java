package nl.gogognome.gogologbook.dbinsinglefile;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import nl.gogognome.gogologbook.entities.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.google.common.io.Files;

public class SingleFileUserDAOTest {

	private static final String INSERT_OF_ONE_USER = "insert;{\"name\":\"test\",\"id\":1}";
	private final File dbFile = new File("target/test/testdb.txt");
	private final SingleFileUserDAO userDao = new SingleFileUserDAO(dbFile);

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

	@Test
	public void testLockingMechanismWithMultipleThreads() throws Exception {
		RecordCreationThread[] threads = new RecordCreationThread[10];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new RecordCreationThread(dbFile, "Thread " + i);
			threads[i].start();
		}

		Thread.sleep(2000);

		for (int i = 0; i < threads.length; i++) {
			threads[i].setFinished(true);
			threads[i].join();
		}

		int nrRecordsCreated = 0;
		for (int i = 0; i < threads.length; i++) {
			assertNull(threads[i].getError());
			nrRecordsCreated += threads[i].getIds().size();
			for (int j = 0; j < i; j++) {
				Set<Integer> intersection = Sets.newHashSet(threads[i].getIds());
				intersection.retainAll(threads[j].getIds());
				if (!intersection.isEmpty()) {
					fail("The ids " + Joiner.on(", ").join(intersection) + " have been created by threads " + j + " and " + i);
				}
			}
		}

		assertTrue(nrRecordsCreated > 0);
	}

	private String getContentsOfDbFile() throws IOException {
		return Joiner.on("\n").join(Files.readLines(dbFile, Charsets.ISO_8859_1));
	}
}