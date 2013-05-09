package nl.gogognome.gogologbook.dao;

import java.util.List;

import nl.gogognome.gogologbook.entities.Category;

public interface CategoryDAO {

	public Category createCategory(Category category);

	public List<Category> findAllCategories();
}
