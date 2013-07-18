package nl.gogognome.gogologbook.interactors;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nl.gogognome.gogologbook.dao.CategoryDAO;
import nl.gogognome.gogologbook.dao.LogMessageDAO;
import nl.gogognome.gogologbook.entities.Category;
import nl.gogognome.gogologbook.interactors.boundary.CannotDeleteCategoryThatIsInUseException;
import nl.gogognome.gogologbook.interactors.boundary.CategoryCreateParams;
import nl.gogognome.gogologbook.interactors.boundary.CategoryUpdateParams;
import nl.gogognome.gogologbook.util.DaoFactory;

public class CategoryInteractor {

	private final LogMessageDAO logMessageDao = DaoFactory.getInstance(LogMessageDAO.class);
	private final CategoryDAO categoryDao = DaoFactory.getInstance(CategoryDAO.class);

	public void createCategory(CategoryCreateParams params) {
		Category category = new Category();
		category.name = params.name;
		categoryDao.createCategory(category);
	}

	public void updateCategory(CategoryUpdateParams params) {
		Category category = new Category(params.categoryId);
		category.name = params.name;
		categoryDao.updateCategory(category);
	}

	public List<Category> findAllCategories() {
		List<Category> categorys = categoryDao.findAllCategories();
		Collections.sort(categorys, new CaseInsensitiveCategoryNameComparator());
		return categorys;
	}

	public void deleteCategory(int categoryId) throws CannotDeleteCategoryThatIsInUseException {
		Category category = categoryDao.getCategory(categoryId);
		if (logMessageDao.isCategoryUsed(category.name)) {
			throw new CannotDeleteCategoryThatIsInUseException();
		}
		categoryDao.deleteCategory(categoryId);
	}
}

class CaseInsensitiveCategoryNameComparator implements Comparator<Category> {

	@Override
	public int compare(Category category1, Category category2) {
		return category1.name.compareToIgnoreCase(category2.name);
	}

}