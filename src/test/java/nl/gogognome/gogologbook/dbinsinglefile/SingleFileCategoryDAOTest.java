package nl.gogognome.gogologbook.dbinsinglefile;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import nl.gogognome.gogologbook.dao.DAOException;
import nl.gogognome.gogologbook.entities.Category;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.Files;

public class SingleFileCategoryDAOTest extends AbstractSingleFileDAOTest {

	private static final String INSERT_OF_ONE_CATEGORY = METADATA + "insert;Category;{\"name\":\"test\",\"id\":1}";
	private final SingleFileDatabase singleFileDatabase = new SingleFileDatabase(dbFile);
	private final SingleFileCategoryDAO categoryDao = new SingleFileCategoryDAO(singleFileDatabase);

	@Test
	public void createRecordShouldWriteRecordInDbFile() throws IOException {
		Category message = new Category();
		message.name = "test";
		categoryDao.createCategory(message);

		assertEquals(INSERT_OF_ONE_CATEGORY, getContentsOfDbFile());
	}

	@Test
	public void dbShouldContainTwoRecordsAfterTwoRecordsHaveBeenCreated() throws IOException {
		Category category = new Category();
		category.name = "name1";
		categoryDao.createCategory(category);

		category.name = "name2";
		categoryDao.createCategory(category);

		List<Category> categories = categoryDao.findAllCategories();

		assertEquals(2, categories.size());
	}

	@Test(expected = DAOException.class)
	public void getNonExistingCategoryShouldFail() throws IOException {
		categoryDao.getCategory(123);
	}

	@Test
	public void shouldGetCategory() throws IOException {
		Category category = new Category();
		category.name = "name1";
		category = categoryDao.createCategory(category);

		Category actualCategory = categoryDao.getCategory(category.id);

		assertCategoryEquals(category, actualCategory);
	}

	@Test(expected = DAOException.class)
	public void updateNonExistingCategoryShouldFail() throws IOException {
		Category category = new Category(123);
		category.name = "name1";
		categoryDao.updateCategory(category);
	}

	@Test
	public void shouldUpdateExistingCategory() throws IOException {
		Category category = new Category();
		category.name = "name1";
		category = categoryDao.createCategory(category);

		category.name = "name2";
		categoryDao.updateCategory(category);

		Category actualCategory = categoryDao.getCategory(category.id);

		assertCategoryEquals(category, actualCategory);
	}

	@Test(expected = DAOException.class)
	public void deleteNonExistingCategoryShouldFail() throws IOException {
		categoryDao.deleteCategory(123);
	}

	@Test
	public void shouldDeleteExistingCategory() throws IOException {
		Category category = new Category();
		category.name = "name1";
		category = categoryDao.createCategory(category);

		categoryDao.deleteCategory(category.id);

		List<Category> categories = categoryDao.findAllCategories();

		assertTrue(categories.isEmpty());
	}

	@Test
	public void absentDbFileLeadsToEmptyDatabase() {
		assertFalse(dbFile.exists());

		List<Category> categories = categoryDao.findAllCategories();

		assertTrue(categories.isEmpty());
	}

	@Test
	public void dbShouldHaveOneCategoryAfterReadingOneCategoryFromFile() throws IOException {
		Files.write(INSERT_OF_ONE_CATEGORY, dbFile, Charsets.ISO_8859_1);

		List<Category> categories = categoryDao.findAllCategories();

		assertFalse(categories.isEmpty());
	}

	private String getContentsOfDbFile() throws IOException {
		return Joiner.on("\n").join(Files.readLines(dbFile, Charsets.ISO_8859_1));
	}

	private void assertCategoryEquals(Category expectedCategory, Category actualCategory) {
		assertEquals(expectedCategory.id, actualCategory.id);
		assertEquals(expectedCategory.name, actualCategory.name);
	}

}