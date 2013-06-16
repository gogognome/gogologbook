package nl.gogognome.gogologbook.interactors;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nl.gogognome.gogologbook.dao.UserDAO;
import nl.gogognome.gogologbook.entities.User;
import nl.gogognome.gogologbook.util.DaoFactory;

public class UserInteractor {

	public void createUser(UserCreateParams params) {
		User user = new User();
		user.name = params.name;
		DaoFactory.getInstance(UserDAO.class).createUser(user);
	}

	public void updateUser(UserUpdateParams params) {
		User user = new User(params.userId);
		user.name = params.name;
		DaoFactory.getInstance(UserDAO.class).updateUser(user);
	}

	public List<User> findAllUsers() {
		List<User> users = DaoFactory.getInstance(UserDAO.class).findAllUsers();
		Collections.sort(users, new CaseInsensitiveUserNameComparator());
		return users;
	}

}

class CaseInsensitiveUserNameComparator implements Comparator<User> {

	@Override
	public int compare(User user1, User user2) {
		return user1.name.compareToIgnoreCase(user2.name);
	}

}