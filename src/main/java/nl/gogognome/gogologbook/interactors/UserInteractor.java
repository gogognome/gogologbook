package nl.gogognome.gogologbook.interactors;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nl.gogognome.gogologbook.dao.LogMessageDAO;
import nl.gogognome.gogologbook.dao.UserDAO;
import nl.gogognome.gogologbook.entities.User;
import nl.gogognome.gogologbook.interactors.boundary.CannotDeleteUserThatIsInUseException;
import nl.gogognome.gogologbook.interactors.boundary.UserCreateParams;
import nl.gogognome.gogologbook.interactors.boundary.UserUpdateParams;
import nl.gogognome.gogologbook.util.DaoFactory;

public class UserInteractor {

	private final LogMessageDAO logMessageDao = DaoFactory.getInstance(LogMessageDAO.class);
	private final UserDAO userDao = DaoFactory.getInstance(UserDAO.class);

	public void createUser(UserCreateParams params) {
		User user = new User();
		user.name = params.name;
		userDao.createUser(user);
	}

	public void updateUser(UserUpdateParams params) {
		User user = new User(params.userId);
		user.name = params.name;
		userDao.updateUser(user);
	}

	public List<User> findAllUsers() {
		List<User> users = userDao.findAllUsers();
		Collections.sort(users, new CaseInsensitiveUserNameComparator());
		return users;
	}

	public void deleteUser(int userId) throws CannotDeleteUserThatIsInUseException {
		if (logMessageDao.isUserUsed(userId)) {
			throw new CannotDeleteUserThatIsInUseException();
		}
		DaoFactory.getInstance(UserDAO.class).deleteUser(userId);
	}

}

class CaseInsensitiveUserNameComparator implements Comparator<User> {

	@Override
	public int compare(User user1, User user2) {
		return user1.name.compareToIgnoreCase(user2.name);
	}

}