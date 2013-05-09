package nl.gogognome.gogologbook.dbinmemory;

import java.util.List;
import java.util.Map;

import nl.gogognome.gogologbook.dao.CategoryDAO;
import nl.gogognome.gogologbook.entities.Category;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class InMemoryCategoryDAO implements CategoryDAO {

	private final Map<Integer, Category> idToCategory = Maps.newTreeMap();

	@Override
	public Category createCategory(Category category) {
		int maxId = 0;
		for (int id : idToCategory.keySet()) {
			maxId = Math.max(maxId, id);
		}

		Category storedCategory = cloneCategory(category, maxId + 1);
		idToCategory.put(storedCategory.id, storedCategory);
		return cloneCategory(storedCategory, storedCategory.id);
	}

	@Override
	public List<Category> findAllCategories() {
		List<Category> results = Lists.newArrayListWithExpectedSize(idToCategory.size());
		for (Category category : idToCategory.values()) {
			results.add(cloneCategory(category, category.id));
		}
		return results;
	}

	private Category cloneCategory(Category origCategory, int clonedId) {
		Category clonedCategory = new Category(clonedId);
		clonedCategory.name = origCategory.name;
		return clonedCategory;
	}
}
