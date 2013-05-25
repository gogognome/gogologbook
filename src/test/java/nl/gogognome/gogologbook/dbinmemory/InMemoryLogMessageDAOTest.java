package nl.gogognome.gogologbook.dbinmemory;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import nl.gogognome.gogologbook.entities.FilterCriteria;
import nl.gogognome.gogologbook.entities.LogMessage;

import org.junit.Test;

public class InMemoryLogMessageDAOTest {

	private final InMemoryLogMessageDAO logMessageDao = new InMemoryLogMessageDAO();

	@Test
	public void shouldFindCreatedMessages() {
		LogMessage message = new LogMessage();
		message.message = "test";
		message.userId = 1;
		message.projectId = 2;
		message.timestamp = new Date();
		logMessageDao.createMessage(message);

		FilterCriteria filter = new FilterCriteria();
		List<LogMessage> foundMessages = logMessageDao.findLogMessages(filter);

		assertEquals(1, foundMessages.size());
		LogMessage foundMessage = foundMessages.get(0);
		assertNotSame(message, foundMessage);
		assertEquals(message.message, foundMessage.message);
		assertEquals(message.userId, foundMessage.userId);
		assertEquals(message.projectId, foundMessage.projectId);
		assertEquals(message.timestamp, foundMessage.timestamp);
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
		List<LogMessage> foundMessages = logMessageDao.findLogMessages(filter);

		assertEquals(2, foundMessages.size());
		LogMessage foundMessage1 = foundMessages.get(0);
		int id1 = foundMessage1.id;

		LogMessage foundMessage2 = foundMessages.get(1);
		assertEquals(id1 + 1, foundMessage2.id);
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
		List<LogMessage> foundMessages = logMessageDao.findLogMessages(filter);

		assertEquals(2, foundMessages.size());
		LogMessage foundMessage1 = foundMessages.get(0);
		assertEquals(message1.message, foundMessage1.message);

		LogMessage foundMessage2 = foundMessages.get(1);
		assertEquals(message2.message, foundMessage2.message);
	}

	@Test
	public void testProjectUsed() {
		LogMessage message = new LogMessage();
		message.projectId = 2;
		logMessageDao.createMessage(message);

		assertTrue(logMessageDao.isProjectUsed(message.projectId));
		assertFalse(logMessageDao.isProjectUsed(message.projectId + 1));
	}
}
