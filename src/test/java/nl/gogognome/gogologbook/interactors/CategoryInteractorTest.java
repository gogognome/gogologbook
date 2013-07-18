package nl.gogognome.gogologbook.interactors;

import static com.google.common.collect.Lists.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import nl.gogognome.gogologbook.dao.CategoryDAO;
import nl.gogognome.gogologbook.dao.LogMessageDAO;
import nl.gogognome.gogologbook.entities.Category;
import nl.gogognome.gogologbook.interactors.boundary.CannotDeleteCategoryThatIsInUseException;
import nl.gogognome.gogologbook.interactors.boundary.CategoryUpdateParams;
import nl.gogognome.gogologbook.util.DaoFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.google.common.collect.Lists;

public class CategoryInteractorTest {

	private final LogMessageDAO logMessageDao = mock(LogMessageDAO.class);
	private final CategoryDAO categoryDao = mock(CategoryDAO.class);
	private CategoryInteractor categoryInteractor;

	@Before
	public void registerMocks() {
		DaoFactory.register(CategoryDAO.class, categoryDao);
		DaoFactory.register(LogMessageDAO.class, logMessageDao);
		categoryInteractor = new CategoryInteractor();
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
	public void shouldUseDaoToUpdateCategory() {
		CategoryUpdateParams params = new CategoryUpdateParams();

		categoryInteractor.updateCategory(params);

		verify(categoryDao).updateCategory(any(Category.class));
	}

	@Test
	public void shouldUseDaoToDeleteCategory() throws Exception {
		when(logMessageDao.isCategoryUsed(anyString())).thenReturn(false);
		when(categoryDao.getCategory(anyInt())).thenReturn(new Category());

		categoryInteractor.deleteCategory(123);

		verify(categoryDao).deleteCategory(123);
	}

	@Test(expected = CannotDeleteCategoryThatIsInUseException.class)
	public void shouldThrowExceptionWhenProjectIsDeletedThasIsInUse() throws Exception {
		when(logMessageDao.isCategoryUsed(anyString())).thenReturn(true);
		when(categoryDao.getCategory(anyInt())).thenReturn(new Category());

		categoryInteractor.deleteCategory(123);
	}

	@Test
	public void shouldConvertUpdateParamsToCategory() {
		ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
		CategoryUpdateParams params = new CategoryUpdateParams();
		params.categoryId = 123;
		params.name = "Pietje Puk";

		categoryInteractor.updateCategory(params);

		verify(categoryDao).updateCategory(categoryCaptor.capture());
		Category category = categoryCaptor.getValue();
		assertEquals(params.categoryId, category.id);
		assertEquals(params.name, category.name);
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
