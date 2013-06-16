package nl.gogognome.gogologbook.interactors;

import static com.google.common.collect.Lists.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import nl.gogognome.gogologbook.dao.UserDAO;
import nl.gogognome.gogologbook.entities.User;
import nl.gogognome.gogologbook.util.DaoFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.google.common.collect.Lists;

public class UserInteractorTest {

	private final UserInteractor userInteractor = new UserInteractor();
	private final UserDAO userDao = mock(UserDAO.class);

	@Before
	public void registerMocks() {
		DaoFactory.register(UserDAO.class, userDao);
	}

	@After
	public void unregisterMocks() {
		DaoFactory.clear();
	}

	@Test
	public void shouldUseDaoToCreateUser() {
		UserCreateParams params = new UserCreateParams();

		userInteractor.createUser(params);

		verify(userDao).createUser(any(User.class));
	}

	@Test
	public void shouldConvertCreateParamsToUser() {
		ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
		UserCreateParams params = new UserCreateParams();
		params.name = "Pietje Puk";

		userInteractor.createUser(params);

		verify(userDao).createUser(userCaptor.capture());
		User user = userCaptor.getValue();
		assertEquals(params.name, user.name);
	}

	@Test
	public void shouldUseDaoToUpdateUser() {
		UserUpdateParams params = new UserUpdateParams();

		userInteractor.updateUser(params);

		verify(userDao).updateUser(any(User.class));
	}

	@Test
	public void shouldConvertUpdateParamsToUser() {
		ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
		UserUpdateParams params = new UserUpdateParams();
		params.userId = 123;
		params.name = "Pietje Puk";

		userInteractor.updateUser(params);

		verify(userDao).updateUser(userCaptor.capture());
		User user = userCaptor.getValue();
		assertEquals(params.userId, user.id);
		assertEquals(params.name, user.name);
	}

	@Test
	public void shouldUseDaoToFindAllUsers() {
		List<User> users = newArrayList();
		when(userDao.findAllUsers()).thenReturn(users);

		userInteractor.findAllUsers();

		verify(userDao).findAllUsers();
	}

	@Test
	public void shouldSortUserNamesLexicographically() {
		User userA = createUser(1, "Alice");
		User userB = createUser(2, "bob");
		User userC = createUser(3, "Charlie");

		List<User> users = Lists.newArrayList(userC, userB, userA);
		when(userDao.findAllUsers()).thenReturn(users);

		List<User> foundUsers = userInteractor.findAllUsers();

		assertSame(userA, foundUsers.get(0));
		assertSame(userB, foundUsers.get(1));
		assertSame(userC, foundUsers.get(2));
	}

	private User createUser(int id, String name) {
		User user = new User(id);
		user.name = name;
		return user;
	}

}
