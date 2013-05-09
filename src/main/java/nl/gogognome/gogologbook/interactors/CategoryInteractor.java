package nl.gogognome.gogologbook.interactors;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nl.gogognome.gogologbook.dao.CategoryDAO;
import nl.gogognome.gogologbook.entities.Category;
import nl.gogognome.gogologbook.util.DaoFactory;

public class CategoryInteractor {

	public List<Category> findAllCategories() {
		List<Category> categorys = DaoFactory.getInstance(CategoryDAO.class).findAllCategories();
		Collections.sort(categorys, new CaseInsensitiveCategoryNameComparator());
		return categorys;
	}

}

class CaseInsensitiveCategoryNameComparator implements Comparator<Category> {

	@Override
	public int compare(Category category1, Category category2) {
		return category1.name.compareToIgnoreCase(category2.name);
	}

}