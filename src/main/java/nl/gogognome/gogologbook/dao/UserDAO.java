package nl.gogognome.gogologbook.dao;

import java.util.List;

import nl.gogognome.gogologbook.entities.User;

public interface UserDAO {

	public User createUser(User user);

	public List<User> findAllUsers();
}
