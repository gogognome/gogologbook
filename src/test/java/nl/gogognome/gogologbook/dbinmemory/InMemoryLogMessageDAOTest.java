package nl.gogognome.gogologbook.dbinmemory;

import static org.junit.Assert.*;

import java.util.List;

import nl.gogognome.gogologbook.dao.DAOException;
import nl.gogognome.gogologbook.entities.FilterCriteria;
import nl.gogognome.gogologbook.entities.LogMessage;
import nl.gogognome.lib.util.DateUtil;

import org.junit.Assert;
import org.junit.Test;

public class InMemoryLogMessageDAOTest {

    private final InMemoryLogMessageDAO logMessageDao = new InMemoryLogMessageDAO();

    @Test
    public void shouldFindCreatedMessages() {
        LogMessage logMessage = buildSomeLogMessage();
        logMessageDao.createMessage(logMessage);

        FilterCriteria filter = new FilterCriteria();
        List<LogMessage> foundMessages = logMessageDao.findLogMessagesByDescendingDate(filter);

        assertEquals(1, foundMessages.size());
        LogMessage foundMessage = foundMessages.get(0);
        assertNotSame(logMessage, foundMessage);
        assertAttributesEqual(logMessage, foundMessage);
    }

    private void assertAttributesEqual(LogMessage expectedLogMessage, LogMessage actualLogMessage) {
        assertEquals(expectedLogMessage.message, actualLogMessage.message);
        assertEquals(expectedLogMessage.userId, actualLogMessage.userId);
        assertEquals(expectedLogMessage.projectId, actualLogMessage.projectId);
        assertEquals(expectedLogMessage.timestamp, actualLogMessage.timestamp);
    }

    @Test
    public void createTwoLogMessagesShouldGenerateIncreasingIds() {
        LogMessage message1 = new LogMessage();
        message1.message = "test1";
        logMessageDao.createMessage(message1);

        LogMessage message2 = new LogMessage();
        message2.message = "test2";
        logMessageDao.createMessage(message2);

        FilterCriteria filter = new FilterCriteria();
        List<LogMessage> foundMessages = logMessageDao.findLogMessagesByDescendingDate(filter);

        assertEquals(2, foundMessages.size());
        LogMessage foundMessage1 = foundMessages.get(0);
        int id1 = foundMessage1.id;

        LogMessage foundMessage2 = foundMessages.get(1);
        assertEquals(id1 + 1, foundMessage2.id);
    }

    @Test
    public void logMessagesShouldBeReturnedWithDescendingDates() {
        LogMessage message1 = new LogMessage();
        message1.timestamp = DateUtil.createDate(2013, 1, 1);
        logMessageDao.createMessage(message1);

        LogMessage message2 = new LogMessage();
        message2.timestamp = DateUtil.createDate(2013, 3, 1);
        logMessageDao.createMessage(message2);

        LogMessage message3 = new LogMessage();
        message3.timestamp = DateUtil.createDate(2013, 2, 1);
        logMessageDao.createMessage(message3);

        FilterCriteria filter = new FilterCriteria();
        List<LogMessage> foundMessages = logMessageDao.findLogMessagesByDescendingDate(filter);

        assertEquals(message2.timestamp, foundMessages.get(0).timestamp);
        assertEquals(message3.timestamp, foundMessages.get(1).timestamp);
        assertEquals(message1.timestamp, foundMessages.get(2).timestamp);
    }

    @Test
    public void createTwoLogMessagesShouldBeFoundInOrderOfCreation() {
        LogMessage message1 = new LogMessage();
        message1.message = "test1";
        logMessageDao.createMessage(message1);

        LogMessage message2 = new LogMessage();
        message2.message = "test2";
        logMessageDao.createMessage(message2);

        FilterCriteria filter = new FilterCriteria();
        List<LogMessage> foundMessages = logMessageDao.findLogMessagesByDescendingDate(filter);

        assertEquals(2, foundMessages.size());
        LogMessage foundMessage1 = foundMessages.get(0);
        assertEquals(message1.message, foundMessage1.message);

        LogMessage foundMessage2 = foundMessages.get(1);
        assertEquals(message2.message, foundMessage2.message);
    }

    @Test(expected = DAOException.class)
    public void shouldFailWhenUpdatingNonExistingLogMessage() {
        LogMessage logMessage = new LogMessage(1);
        logMessageDao.updateMessage(logMessage);
    }

    @Test
    public void shouldUpdateAllFields() {
        LogMessage logMessage1 = buildSomeLogMessage();
        logMessage1 = logMessageDao.createMessage(logMessage1);

        LogMessage logMessage2 = new LogMessage(logMessage1.id);
        logMessage2.message = "test update";
        logMessage2.userId = 2;
        logMessage2.projectId = 3;
        logMessage2.timestamp = DateUtil.createDate(2013, 5, 10);

        logMessageDao.updateMessage(logMessage2);

        FilterCriteria filter = new FilterCriteria();
        List<LogMessage> foundMessages = logMessageDao.findLogMessagesByDescendingDate(filter);

        assertEquals(1, foundMessages.size());
        LogMessage foundMessage = foundMessages.get(0);
        assertNotSame(logMessage2, foundMessage);
        assertEquals(logMessage2.message, foundMessage.message);
        assertEquals(logMessage2.userId, foundMessage.userId);
        assertEquals(logMessage2.projectId, foundMessage.projectId);
        assertEquals(logMessage2.timestamp, foundMessage.timestamp);
    }

    @Test(expected = DAOException.class)
    public void deleteNonExistingLogMessageShouldFail() {
        logMessageDao.deleteMessage(1);
    }

    @Test
    public void deleteExistingLogMessageShouldDeleteLogMessage() {
        LogMessage logMessage = logMessageDao.createMessage(buildSomeLogMessage());

        logMessageDao.deleteMessage(logMessage.id);

        List<LogMessage> logMessages = logMessageDao.findLogMessagesByDescendingDate(new FilterCriteria());
        assertTrue(logMessages.isEmpty());
    }

    private LogMessage buildSomeLogMessage() {
        LogMessage logMessage = new LogMessage();
        logMessage.message = "test";
        logMessage.userId = 1;
        logMessage.projectId = 2;
        logMessage.timestamp = DateUtil.createDate(2013, 1, 1);
        return logMessage;
    }

    @Test
    public void testProjectUsed() {
        LogMessage message = new LogMessage();
        message.projectId = 2;
        logMessageDao.createMessage(message);

        assertTrue(logMessageDao.isProjectUsed(message.projectId));
        assertFalse(logMessageDao.isProjectUsed(message.projectId + 1));
    }

    @Test
    public void testUserUsed() {
        LogMessage message = new LogMessage();
        message.userId = 2;
        logMessageDao.createMessage(message);

        assertTrue(logMessageDao.isUserUsed(message.userId));
        assertFalse(logMessageDao.isUserUsed(message.userId + 1));
    }

    @Test
    public void testCategoryUsed() {
        LogMessage message = new LogMessage();
        message.category = "test";
        logMessageDao.createMessage(message);

        assertTrue(logMessageDao.isCategoryUsed("test"));
        assertFalse(logMessageDao.isCategoryUsed("bla"));
    }
}
