package nl.gogognome.gogologbook.interactors;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import nl.gogognome.gogologbook.dao.LogMessageDAO;
import nl.gogognome.gogologbook.entities.FilterCriteria;
import nl.gogognome.gogologbook.entities.LogMessage;
import nl.gogognome.gogologbook.interactors.boundary.LogMessageFindParams;
import nl.gogognome.gogologbook.interactors.boundary.LogMessageFindResult;
import nl.gogognome.gogologbook.util.DaoFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class LogMessageFindInteractorTest {

	private final LogMessageFindInteractor logMessageFindInteractor = new LogMessageFindInteractor();
	private final LogMessageDAO logMessageDao = mock(LogMessageDAO.class);

	@Before
	public void registerMocks() {
		DaoFactory.register(LogMessageDAO.class, logMessageDao);
	}

	@After
	public void unregisterMocks() {
		DaoFactory.clear();
	}

	@Test
	public void shouldUseDaoToFindMessages() {
		List<LogMessage> messages = Lists.newArrayList(new LogMessage(), new LogMessage());
		when(logMessageDao.findLogMessages(any(FilterCriteria.class))).thenReturn(messages);
		LogMessageFindParams params = new LogMessageFindParams();

		logMessageFindInteractor.findMessages(params);

		verify(logMessageDao).findLogMessages(any(FilterCriteria.class));
	}

	@Test
	public void shouldConvertEntityToLogResult() {
		LogMessage logMessage = new LogMessage(123);
		logMessage.category = "cat";
		logMessage.message = "This is a test";
		logMessage.project = "proj";
		logMessage.town = "Tilburg";
		logMessage.username = "Sander";
		List<LogMessage> messages = Lists.newArrayList(logMessage);
		when(logMessageDao.findLogMessages(any(FilterCriteria.class))).thenReturn(messages);
		LogMessageFindParams params = new LogMessageFindParams();

		List<LogMessageFindResult> results = logMessageFindInteractor.findMessages(params);

		assertEquals(1, results.size());
		LogMessageFindResult result = results.get(0);
		assertEquals(logMessage.category, result.category);
		assertEquals(logMessage.id, result.id);
		assertEquals(logMessage.message, result.message);
		assertEquals(logMessage.project, result.project);
		assertEquals(logMessage.town, result.town);
		assertEquals(logMessage.username, result.username);
	}

}
