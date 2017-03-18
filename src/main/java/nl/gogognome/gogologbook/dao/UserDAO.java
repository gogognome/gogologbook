package nl.gogognome.gogologbook.dao;

import java.util.List;

import nl.gogognome.gogologbook.entities.User;

public interface UserDAO {

	User createUser(User user);
	void updateUser(User user);
	void deleteUser(int userId);

	List<User> findAllUsers();
	List<User> findAllActiveUsers();
}
