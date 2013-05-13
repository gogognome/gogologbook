package nl.gogognome.gogologbook.interactors;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import nl.gogognome.gogologbook.dao.LogMessageDAO;
import nl.gogognome.gogologbook.entities.LogMessage;
import nl.gogognome.gogologbook.interactors.boundary.LogMessageCreateParams;
import nl.gogognome.gogologbook.util.DaoFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class LogMessageCreateInteractorTest {

	private final LogMessageCreateInteractor logMessageCreateInteractor = new LogMessageCreateInteractor();
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
	public void shouldUseDaoToCreateLogMessage() {
		LogMessageCreateParams params = new LogMessageCreateParams();
		logMessageCreateInteractor.createMessage(params);
		verify(logMessageDao).createMessage(any(LogMessage.class));
	}

	@Test
	public void shouldAddTimestampToLogMessage() {
		ArgumentCaptor<LogMessage> logMessageArgument = ArgumentCaptor.forClass(LogMessage.class);
		LogMessageCreateParams params = new LogMessageCreateParams();

		logMessageCreateInteractor.createMessage(params);

		verify(logMessageDao).createMessage(logMessageArgument.capture());
		assertNotNull(logMessageArgument.getValue().timestamp);
	}
}
