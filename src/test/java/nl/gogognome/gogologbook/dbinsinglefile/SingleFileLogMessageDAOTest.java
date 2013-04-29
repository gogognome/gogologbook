package nl.gogognome.gogologbook.dbinsinglefile;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import nl.gogognome.gogologbook.entities.FilterCriteria;
import nl.gogognome.gogologbook.entities.LogMessage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.Files;

public class SingleFileLogMessageDAOTest {

	private static final String INSERT_OF_ONE_LOG_MESSAGE = "insert;{\"message\":\"test\",\"id\":1}";
	private final File dbFile = new File("target/test/testdb.txt");
	private final SingleFileLogMessageDAO logMessageDAO = new SingleFileLogMessageDAO(dbFile);

	@Before
	public void setUp() throws InterruptedException {
		dbFile.getParentFile().mkdirs();
		deleteDbFile();
	}

	@After
	public void tearDown() throws InterruptedException {
		deleteDbFile();
	}

	private void deleteDbFile() throws InterruptedException {
		while (dbFile.exists()) {
			dbFile.delete();
			if (dbFile.exists()) {
				Thread.sleep(100);
			}
		}
	}

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

		List<LogMessage> logMessages = logMessageDAO.findLogMessages(FilterCriteria.createFindAll());

		assertEquals(2, logMessages.size());
	}

	@Test
	public void absentDbFileLeadsToEmptyDatabase() {
		assertFalse(dbFile.exists());

		List<LogMessage> logMessages = logMessageDAO.findLogMessages(FilterCriteria.createFindAll());

		assertTrue(logMessages.isEmpty());
	}

	@Test
	public void dbShouldHaveOneLogMessageAfterReadingOneLogMessageFromFile() throws IOException {
		Files.write(INSERT_OF_ONE_LOG_MESSAGE, dbFile, Charsets.ISO_8859_1);

		List<LogMessage> logMessages = logMessageDAO.findLogMessages(FilterCriteria.createFindAll());

		assertFalse(logMessages.isEmpty());
	}

	private String getContentsOfDbFile() throws IOException {
		return Joiner.on("\n").join(Files.readLines(dbFile, Charsets.ISO_8859_1));
	}
}
