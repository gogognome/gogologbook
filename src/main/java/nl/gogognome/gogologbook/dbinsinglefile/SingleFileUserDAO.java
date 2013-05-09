package nl.gogognome.gogologbook.dbinsinglefile;

import java.util.List;

import nl.gogognome.gogologbook.dao.UserDAO;
import nl.gogognome.gogologbook.dbinmemory.InMemoryUserDAO;
import nl.gogognome.gogologbook.entities.User;

public class SingleFileUserDAO implements UserDAO, SingleFileDatabaseDAO {

	private static final String TABLE_NAME = "User";

	private InMemoryUserDAO inMemoryUserDao = new InMemoryUserDAO();
	private final SingleFileDatabase singleFileDatabase;

	public SingleFileUserDAO(SingleFileDatabase singleFileDatabase) {
		this.singleFileDatabase = singleFileDatabase;
		singleFileDatabase.registerDao(TABLE_NAME, this);
	}

	@Override
	public User createUser(User user) {
		try {
			singleFileDatabase.acquireLock();
			singleFileDatabase.initInMemDatabaseFromFile();
			user = inMemoryUserDao.createUser(user);
			singleFileDatabase.appendInsertToFile(TABLE_NAME, user);
		} finally {
			singleFileDatabase.releaseLock();
		}
		return user;
	}

	@Override
	public List<User> findAllUsers() {
		try {
			singleFileDatabase.acquireLock();
			singleFileDatabase.initInMemDatabaseFromFile();
		} finally {
			singleFileDatabase.releaseLock();
		}
		return inMemoryUserDao.findAllUsers();
	}

	@Override
	public void removeAllRecordsFromInMemoryDatabase() {
		inMemoryUserDao = new InMemoryUserDAO();
	}

	@Override
	public void createRecordInMemoryDatabase(Object record) {
		User user = (User) record;
		inMemoryUserDao.createUser(user);
	}

	@Override
	public Class<?> getRecordClass() {
		return User.class;
	}

}
