package nl.gogognome.gogologbook.dbinsinglefile;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import nl.gogognome.gogologbook.entities.FilterCriteria;
import nl.gogognome.gogologbook.entities.LogMessage;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.google.common.io.Files;

public class SingleFileLogMessageDAOTest extends AbstractSingleFileDAOTest {

	private static final String INSERT_OF_ONE_LOG_MESSAGE = METADATA + "insert;LogMessage;{\"userId\":0,\"projectId\":0,\"message\":\"test\",\"id\":1}";
	private final SingleFileDatabase singleFileDatabase = new SingleFileDatabase(dbFile);
	private final SingleFileLogMessageDAO logMessageDAO = new SingleFileLogMessageDAO(singleFileDatabase);

	@Test
	public void createRecordShouldWriteRecordInDbFile() throws IOException {
		LogMessage message = new LogMessage();
		message.message = "test";
		logMessageDAO.createMessage(message);

		assertEquals(INSERT_OF_ONE_LOG_MESSAGE, getContentsOfDbFile());
	}

	@Test
	public void dbShouldContainTwoRecordsAfterTwoRecordsHaveBeenCreated() throws IOException {
		LogMessage logMessage = new LogMessage();
		logMessage.message = "msg1";
		logMessageDAO.createMessage(logMessage);

		logMessage.message = "msg2";
		logMessageDAO.createMessage(logMessage);

		List<LogMessage> logMessages = logMessageDAO.findLogMessagesByDescendingDate(FilterCriteria.createFindAll());

		assertEquals(2, logMessages.size());
	}

	@Test
	public void absentDbFileLeadsToEmptyDatabase() {
		assertFalse(dbFile.exists());

		List<LogMessage> logMessages = logMessageDAO.findLogMessagesByDescendingDate(FilterCriteria.createFindAll());

		assertTrue(logMessages.isEmpty());
	}

	@Test
	public void dbShouldHaveOneLogMessageAfterReadingOneLogMessageFromFile() throws IOException {
		Files.write(INSERT_OF_ONE_LOG_MESSAGE, dbFile, Charsets.ISO_8859_1);

		List<LogMessage> logMessages = logMessageDAO.findLogMessagesByDescendingDate(FilterCriteria.createFindAll());

		assertFalse(logMessages.isEmpty());
	}

	@Test
	public void shouldContainAllAttributesAfterReadingBackLogMessage() throws IOException {
		LogMessage logMessage = new LogMessage();
		logMessage.projectId = 1;
		logMessage.userId = 2;
		logMessage.message = "test";
		logMessage.timestamp = new Date();

		logMessageDAO.createMessage(logMessage);
		List<LogMessage> createdLogMessages = logMessageDAO.findLogMessagesByDescendingDate(FilterCriteria.createFindAll());
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
		logMessageDAO.createMessage(message);

		assertTrue(logMessageDAO.isProjectUsed(message.projectId));
		assertFalse(logMessageDAO.isProjectUsed(message.projectId + 1));
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