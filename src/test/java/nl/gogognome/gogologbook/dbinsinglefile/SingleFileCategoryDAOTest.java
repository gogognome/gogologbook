package nl.gogognome.gogologbook.dbinsinglefile;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import nl.gogognome.gogologbook.entities.Category;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.Files;

public class SingleFileCategoryDAOTest extends AbstractSingleFileDAOTest {

	private static final String INSERT_OF_ONE_USER = "insert;Category;{\"name\":\"test\",\"id\":1}";
	private final SingleFileDatabase singleFileDatabase = new SingleFileDatabase(dbFile);
	private final SingleFileCategoryDAO userDao = new SingleFileCategoryDAO(singleFileDatabase);

	@Test
	public void createRecordShouldWriteRecordInDbFile() throws IOException {
		Category message = new Category();
		message.name = "test";
		userDao.createCategory(message);

		assertEquals(INSERT_OF_ONE_USER, getContentsOfDbFile());
	}

	@Test
	public void dbShouldContainTwoRecordsAfterTwoRecordsHaveBeenCreated() throws IOException {
		Category user = new Category();
		user.name = "name1";
		userDao.createCategory(user);

		user.name = "name2";
		userDao.createCategory(user);

		List<Category> users = userDao.findAllCategories();

		assertEquals(2, users.size());
	}

	@Test
	public void absentDbFileLeadsToEmptyDatabase() {
		assertFalse(dbFile.exists());

		List<Category> users = userDao.findAllCategories();

		assertTrue(users.isEmpty());
	}

	@Test
	public void dbShouldHaveOneCategoryAfterReadingOneCategoryFromFile() throws IOException {
		Files.write(INSERT_OF_ONE_USER, dbFile, Charsets.ISO_8859_1);

		List<Category> users = userDao.findAllCategories();

		assertFalse(users.isEmpty());
	}

	private String getContentsOfDbFile() throws IOException {
		return Joiner.on("\n").join(Files.readLines(dbFile, Charsets.ISO_8859_1));
	}
}