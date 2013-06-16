package nl.gogognome.gogologbook.dbinmemory;

import java.util.List;
import java.util.Map;

import nl.gogognome.gogologbook.dao.DAOException;
import nl.gogognome.gogologbook.dao.UserDAO;
import nl.gogognome.gogologbook.entities.User;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class InMemoryUserDAO implements UserDAO {

	private final Map<Integer, User> idToUser = Maps.newTreeMap();

	@Override
	public User createUser(User user) {
		int maxId = 0;
		for (int id : idToUser.keySet()) {
			maxId = Math.max(maxId, id);
		}

		User storedUser = cloneUser(user, maxId + 1);
		idToUser.put(storedUser.id, storedUser);
		return cloneUser(storedUser, storedUser.id);
	}

	@Override
	public void updateUser(User user) {
		if (!idToUser.containsKey(user.id)) {
			throw new DAOException("User with " + user.id + " does not exist. It cannot be updated.");
		}

		User storedUser = cloneUser(user, user.id);
		idToUser.put(storedUser.id, storedUser);
	}

	@Override
	public List<User> findAllUsers() {
		List<User> results = Lists.newArrayListWithExpectedSize(idToUser.size());
		for (User user : idToUser.values()) {
			results.add(cloneUser(user, user.id));
		}
		return results;
	}

	private User cloneUser(User origUser, int clonedId) {
		User clonedUser = new User(clonedId);
		clonedUser.name = origUser.name;
		return clonedUser;
	}
}
