package nl.gogognome.gogologbook.dbinmemory;

import static org.junit.Assert.*;

import java.util.List;

import nl.gogognome.gogologbook.entities.Category;

import org.junit.Test;

public class InMemoryCategoryDAOTest {

	private final InMemoryCategoryDAO categoryDao = new InMemoryCategoryDAO();

	@Test
	public void shouldFindCreatedCategories() {
		Category category = new Category();
		category.name = "Customer";
		categoryDao.createCategory(category);

		List<Category> foundCategories = categoryDao.findAllCategories();

		assertEquals(1, foundCategories.size());
		Category foundCategory = foundCategories.get(0);
		assertNotSame(category, foundCategory);
		assertEquals(category.name, foundCategory.name);
	}

	@Test
	public void createTwoCategoriesShouldGenerateIncreasingIds() {
		Category category1 = new Category();
		category1.name = "cat1";
		categoryDao.createCategory(category1);

		Category category2 = new Category();
		category2.name = "cat2";
		categoryDao.createCategory(category2);

		List<Category> foundMessages = categoryDao.findAllCategories();

		assertEquals(2, foundMessages.size());
		Category foundcategory1 = foundMessages.get(0);
		int id1 = foundcategory1.id;

		Category foundcategory2 = foundMessages.get(1);
		assertEquals(id1 + 1, foundcategory2.id);
	}

	@Test
	public void createTwoCategoriesShouldBeFoundInOrderOfCreation() {
		Category category1 = new Category();
		category1.name = "test1";
		categoryDao.createCategory(category1);

		Category category2 = new Category();
		category2.name = "test2";
		categoryDao.createCategory(category2);

		List<Category> foundMessages = categoryDao.findAllCategories();

		assertEquals(2, foundMessages.size());
		Category foundCategory1 = foundMessages.get(0);
		assertEquals(category1.name, foundCategory1.name);

		Category foundCategory2 = foundMessages.get(1);
		assertEquals(category2.name, foundCategory2.name);
	}

}
