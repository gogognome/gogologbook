package nl.gogognome.gogologbook.dao;

import java.util.List;

import nl.gogognome.gogologbook.entities.Category;

public interface CategoryDAO {

	public Category createCategory(Category category);

	public void updateCategory(Category category);

	public void deleteCategory(int categoryId);

	public List<Category> findAllCategories();

	public Category getCategory(int categoryId);

}
