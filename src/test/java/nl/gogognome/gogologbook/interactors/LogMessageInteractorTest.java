package nl.gogognome.gogologbook.interactors;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import nl.gogognome.gogologbook.dao.LogMessageDAO;
import nl.gogognome.gogologbook.entities.LogMessage;
import nl.gogognome.gogologbook.interactors.boundary.LogMessageCreateParams;
import nl.gogognome.gogologbook.interactors.boundary.LogMessageDeleteParams;
import nl.gogognome.gogologbook.interactors.boundary.LogMessageUpdateParams;
import nl.gogognome.gogologbook.util.DaoFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LogMessageInteractorTest {

    private final LogMessageInteractor logMessageInteractor = new LogMessageInteractor();
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
        logMessageInteractor.createMessage(params);
        verify(logMessageDao).createMessage(any(LogMessage.class));
    }

    @Test
    public void shouldUseDaoToUpdateLogMessage() {
        LogMessageUpdateParams params = new LogMessageUpdateParams();
        logMessageInteractor.updateMessage(params);
        verify(logMessageDao).updateMessage(any(LogMessage.class));
    }

    @Test
    public void shouldUseDaoToDeleteLogMessage() {
        LogMessageDeleteParams params = new LogMessageDeleteParams();
        params.id = 123;

        logMessageInteractor.deleteMessage(params);

        verify(logMessageDao).deleteMessage(eq(params.id));
    }
}
