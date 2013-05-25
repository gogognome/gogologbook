package nl.gogognome.gogologbook.dbinsinglefile;

import java.util.List;

import nl.gogognome.gogologbook.dao.CategoryDAO;
import nl.gogognome.gogologbook.dao.DAOException;
import nl.gogognome.gogologbook.dbinmemory.InMemoryCategoryDAO;
import nl.gogognome.gogologbook.entities.Category;

public class SingleFileCategoryDAO implements CategoryDAO, SingleFileDatabaseDAO {

	private static final String TABLE_NAME = "Category";

	private InMemoryCategoryDAO inMemoryCategoryDao = new InMemoryCategoryDAO();
	private final SingleFileDatabase singleFileDatabase;

	public SingleFileCategoryDAO(SingleFileDatabase singleFileDatabase) {
		this.singleFileDatabase = singleFileDatabase;
		singleFileDatabase.registerDao(TABLE_NAME, this);
	}

	@Override
	public Category createCategory(Category category) {
		try {
			singleFileDatabase.acquireLock();
			singleFileDatabase.initInMemDatabaseFromFile();
			category = inMemoryCategoryDao.createCategory(category);
			singleFileDatabase.appendInsertToFile(TABLE_NAME, category);
		} finally {
			singleFileDatabase.releaseLock();
		}
		return category;
	}

	@Override
	public List<Category> findAllCategories() {
		try {
			singleFileDatabase.acquireLock();
			singleFileDatabase.initInMemDatabaseFromFile();
		} finally {
			singleFileDatabase.releaseLock();
		}
		return inMemoryCategoryDao.findAllCategories();
	}

	@Override
	public void removeAllRecordsFromInMemoryDatabase() {
		inMemoryCategoryDao = new InMemoryCategoryDAO();
	}

	@Override
	public void createRecordInMemoryDatabase(Object record) {
		Category category = (Category) record;
		inMemoryCategoryDao.createCategory(category);
	}

	@Override
	public void updateRecordInMemoryDatabase(Object record) {
		throw new DAOException("Unsupported operation");
	}

	@Override
	public void deleteRecordFromInMemoryDatabase(int id) {
		throw new DAOException("Not implemented yet");
	}

	@Override
	public Class<?> getRecordClass() {
		return Category.class;
	}

}
