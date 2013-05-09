package nl.gogognome.gogologbook.interactors;

import static com.google.common.collect.Lists.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import nl.gogognome.gogologbook.dao.CategoryDAO;
import nl.gogognome.gogologbook.entities.Category;
import nl.gogognome.gogologbook.util.DaoFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class CategoryInteractorTest {

	private final CategoryInteractor categoryInteractor = new CategoryInteractor();
	private final CategoryDAO categoryDao = mock(CategoryDAO.class);

	@Before
	public void registerMocks() {
		DaoFactory.register(CategoryDAO.class, categoryDao);
	}

	@After
	public void unregisterMocks() {
		DaoFactory.clear();
	}

	@Test
	public void shouldUseDaoToFindAllCategories() {
		List<Category> categorys = newArrayList();
		when(categoryDao.findAllCategories()).thenReturn(categorys);

		categoryInteractor.findAllCategories();

		verify(categoryDao).findAllCategories();
	}

	@Test
	public void shouldSortCategoryNamesLexicographically() {
		Category categoryA = createCategory(1, "Alice");
		Category categoryB = createCategory(2, "bob");
		Category categoryC = createCategory(3, "Charlie");

		List<Category> categorys = Lists.newArrayList(categoryC, categoryB, categoryA);
		when(categoryDao.findAllCategories()).thenReturn(categorys);

		List<Category> foundCategories = categoryInteractor.findAllCategories();

		assertSame(categoryA, foundCategories.get(0));
		assertSame(categoryB, foundCategories.get(1));
		assertSame(categoryC, foundCategories.get(2));
	}

	private Category createCategory(int id, String name) {
		Category category = new Category(id);
		category.name = name;
		return category;
	}

}
