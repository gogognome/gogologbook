package nl.gogognome.gogologbook.dao;

import java.util.List;

import nl.gogognome.gogologbook.entities.User;

public interface UserDAO {

	User createUser(User user);

	void updateUser(User user);

	List<User> findAllUsers();
}
