package nl.gogognome.gogologbook.dbinsinglefile;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import nl.gogognome.gogologbook.dao.DAOException;
import nl.gogognome.gogologbook.entities.FilterCriteria;
import nl.gogognome.gogologbook.entities.LogMessage;
import nl.gogognome.lib.util.DateUtil;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.google.common.io.Files;

public class SingleFileLogMessageDAOTest extends AbstractSingleFileDAOTest {

	private static final String INSERT_OF_ONE_LOG_MESSAGE = METADATA + "insert;LogMessage;{\"userId\":0,\"projectId\":0,\"message\":\"test\",\"id\":1}";
	private final SingleFileDatabase singleFileDatabase = new SingleFileDatabase(dbFile);
	private final SingleFileLogMessageDAO logMessageDao = new SingleFileLogMessageDAO(singleFileDatabase);

	@Test
	public void createRecordShouldWriteRecordInDbFile() throws IOException {
		LogMessage message = new LogMessage();
		message.message = "test";
		logMessageDao.createMessage(message);

		assertEquals(INSERT_OF_ONE_LOG_MESSAGE, getContentsOfDbFile());
	}

	@Test
	public void dbShouldContainTwoRecordsAfterTwoRecordsHaveBeenCreated() throws IOException {
		LogMessage logMessage = new LogMessage();
		logMessage.message = "msg1";
		logMessageDao.createMessage(logMessage);

		logMessage.message = "msg2";
		logMessageDao.createMessage(logMessage);

		List<LogMessage> logMessages = logMessageDao.findLogMessagesByDescendingDate(FilterCriteria.createFindAll());

		assertEquals(2, logMessages.size());
	}

	@Test(expected = DAOException.class)
	public void shouldFailWhenUpdatingNonExistingLogMessage() {
		LogMessage logMessage = new LogMessage(1);
		logMessageDao.updateMessage(logMessage);
	}

	@Test
	public void shouldUpdateAllFields() {
		LogMessage logMessage1 = createSomeLogMessage();
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

	private LogMessage createSomeLogMessage() {
		LogMessage logMessage = new LogMessage();
		logMessage.message = "test";
		logMessage.userId = 1;
		logMessage.projectId = 2;
		logMessage.timestamp = DateUtil.createDate(2013, 1, 1);
		return logMessage;
	}

	@Test
	public void absentDbFileLeadsToEmptyDatabase() {
		assertFalse(dbFile.exists());

		List<LogMessage> logMessages = logMessageDao.findLogMessagesByDescendingDate(FilterCriteria.createFindAll());

		assertTrue(logMessages.isEmpty());
	}

	@Test
	public void dbShouldHaveOneLogMessageAfterReadingOneLogMessageFromFile() throws IOException {
		Files.write(INSERT_OF_ONE_LOG_MESSAGE, dbFile, Charsets.ISO_8859_1);

		List<LogMessage> logMessages = logMessageDao.findLogMessagesByDescendingDate(FilterCriteria.createFindAll());

		assertFalse(logMessages.isEmpty());
	}

	@Test
	public void shouldContainAllAttributesAfterReadingBackLogMessage() throws IOException {
		LogMessage logMessage = new LogMessage();
		logMessage.projectId = 1;
		logMessage.userId = 2;
		logMessage.message = "test";
		logMessage.timestamp = new Date();

		logMessageDao.createMessage(logMessage);
		List<LogMessage> createdLogMessages = logMessageDao.findLogMessagesByDescendingDate(FilterCriteria.createFindAll());
		LogMessage createdLogMessage = createdLogMessages.get(0);

		assertEquals(logMessage.projectId, createdLogMessage.projectId);
		assertEquals(logMessage.userId, createdLogMessage.userId);
		assertEquals(logMessage.message, createdLogMessage.message);
		assertTrue(Math.abs(logMessage.timestamp.getTime() - createdLogMessage.timestamp.getTime()) < 1000);
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
	public void testLockingMechanismWithMultipleThreads() throws Exception {
		RecordCreationThread[] threads = new RecordCreationThread[10];
		for (int i = 0; i < threads.length; i++) {
			SingleFileDatabase singleFileDatabase = new SingleFileDatabase(dbFile);
			threads[i] = new RecordCreationThread(singleFileDatabase, "Thread " + i);
			threads[i].start();
		}

		Thread.sleep(2000);

		for (int i = 0; i < threads.length; i++) {
			threads[i].setFinished(true);
		}

		for (int i = 0; i < threads.length; i++) {
			threads[i].join();
		}

		int nrRecordsCreated = 0;
		for (int i = 0; i < threads.length; i++) {
			assertNull(threads[i].getError());
			nrRecordsCreated += threads[i].getIds().size();
			for (int j = 0; j < i; j++) {
				Set<Integer> intersection = Sets.newHashSet(threads[i].getIds());
				intersection.retainAll(threads[j].getIds());
				if (!intersection.isEmpty()) {
					fail("The ids " + Joiner.on(", ").join(intersection) + " have been created by threads " + j + " and " + i);
				}
			}
		}

		assertTrue(nrRecordsCreated > 0);
	}

	private String getContentsOfDbFile() throws IOException {
		return Joiner.on("\n").join(Files.readLines(dbFile, Charsets.ISO_8859_1));
	}
}