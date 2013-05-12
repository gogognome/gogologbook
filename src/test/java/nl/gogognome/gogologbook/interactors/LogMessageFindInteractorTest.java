package nl.gogognome.gogologbook.interactors;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import nl.gogognome.gogologbook.dao.LogMessageDAO;
import nl.gogognome.gogologbook.dao.ProjectDAO;
import nl.gogognome.gogologbook.dao.UserDAO;
import nl.gogognome.gogologbook.entities.FilterCriteria;
import nl.gogognome.gogologbook.entities.LogMessage;
import nl.gogognome.gogologbook.entities.Project;
import nl.gogognome.gogologbook.entities.User;
import nl.gogognome.gogologbook.interactors.boundary.LogMessageFindParams;
import nl.gogognome.gogologbook.interactors.boundary.LogMessageFindResult;
import nl.gogognome.gogologbook.util.DaoFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class LogMessageFindInteractorTest {

	private static final int PROJECT_ID = 123;
	private static final int USER_ID = 456;

	private final LogMessageFindInteractor logMessageFindInteractor = new LogMessageFindInteractor();
	private final LogMessageDAO logMessageDao = mock(LogMessageDAO.class);
	private final ProjectDAO projectDao = mock(ProjectDAO.class);
	private final UserDAO userDao = mock(UserDAO.class);

	private Project project;
	private User user;

	@Before
	public void setUp() {
		DaoFactory.register(LogMessageDAO.class, logMessageDao);
		DaoFactory.register(ProjectDAO.class, projectDao);
		DaoFactory.register(UserDAO.class, userDao);

		project = new Project(PROJECT_ID);
		project.projectNr = "PP12345";
		project.customer = "Piet Puk";
		project.street = "Avenue Parmentier 12";
		project.town = "Paris";

		user = new User(USER_ID);
		user.name = "Sander";

		when(projectDao.findAllProjects()).thenReturn(Lists.newArrayList(project));
		when(userDao.findAllUsers()).thenReturn(Lists.newArrayList(user));
	}

	@After
	public void unregisterMocks() {
		DaoFactory.clear();
	}

	@Test
	public void shouldUseDaoToFindMessages() {
		List<LogMessage> messages = Lists.newArrayList(createLogMessage(1), createLogMessage(2));
		when(logMessageDao.findLogMessages(any(FilterCriteria.class))).thenReturn(messages);
		LogMessageFindParams params = new LogMessageFindParams();

		logMessageFindInteractor.findMessages(params);

		verify(logMessageDao).findLogMessages(any(FilterCriteria.class));
	}

	@Test
	public void shouldConvertEntityToLogResult() {
		LogMessage logMessage = createLogMessage(1);
		List<LogMessage> messages = Lists.newArrayList(logMessage);
		when(logMessageDao.findLogMessages(any(FilterCriteria.class))).thenReturn(messages);
		LogMessageFindParams params = new LogMessageFindParams();

		List<LogMessageFindResult> results = logMessageFindInteractor.findMessages(params);

		assertEquals(1, results.size());
		LogMessageFindResult result = results.get(0);
		assertEquals(logMessage.category, result.category);
		assertEquals(logMessage.id, result.id);
		assertEquals(logMessage.message, result.message);
		assertEquals(project.projectNr, result.projectNr);
		assertEquals(project.customer, result.customer);
		assertEquals(project.street, result.street);
		assertEquals(project.town, result.town);
		assertEquals(user.name, result.username);
	}

	private LogMessage createLogMessage(int id) {
		LogMessage logMessage = new LogMessage(id);
		logMessage.category = "cat";
		logMessage.message = "This is a test";
		logMessage.projectId = PROJECT_ID;
		logMessage.userId = USER_ID;
		return logMessage;
	}

}
