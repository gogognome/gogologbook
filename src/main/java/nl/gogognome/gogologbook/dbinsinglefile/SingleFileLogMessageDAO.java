package nl.gogognome.gogologbook.dbinsinglefile;

import java.util.List;

import nl.gogognome.gogologbook.dao.DAOException;
import nl.gogognome.gogologbook.dao.LogMessageDAO;
import nl.gogognome.gogologbook.dbinmemory.InMemoryLogMessageDAO;
import nl.gogognome.gogologbook.entities.FilterCriteria;
import nl.gogognome.gogologbook.entities.LogMessage;

public class SingleFileLogMessageDAO implements LogMessageDAO, SingleFileDatabaseDAO {

	private static final String TABLE_NAME = "LogMessage";

	private InMemoryLogMessageDAO inMemoryLogMessageDao = new InMemoryLogMessageDAO();
	private final SingleFileDatabase singleFileDatabase;

	public SingleFileLogMessageDAO(SingleFileDatabase singleFileDatabase) {
		this.singleFileDatabase = singleFileDatabase;
		singleFileDatabase.registerDao(TABLE_NAME, this);
	}

	@Override
	public LogMessage createMessage(LogMessage message) {
		try {
			singleFileDatabase.acquireLock();
			singleFileDatabase.initInMemDatabaseFromFile();
			message = inMemoryLogMessageDao.createMessage(message);
			singleFileDatabase.appendInsertToFile(TABLE_NAME, message);
		} finally {
			singleFileDatabase.releaseLock();
		}
		return message;
	}

	@Override
	public List<LogMessage> findLogMessages(FilterCriteria filter) {
		try {
			singleFileDatabase.acquireLock();
			singleFileDatabase.initInMemDatabaseFromFile();
		} finally {
			singleFileDatabase.releaseLock();
		}
		return inMemoryLogMessageDao.findLogMessages(filter);
	}

	@Override
	public void removeAllRecordsFromInMemoryDatabase() {
		inMemoryLogMessageDao = new InMemoryLogMessageDAO();
	}

	@Override
	public void createRecordInMemoryDatabase(Object record) {
		LogMessage logMessage = (LogMessage) record;
		inMemoryLogMessageDao.createMessage(logMessage);
	}

	@Override
	public void updateRecordInMemoryDatabase(Object record) {
		throw new DAOException("Unsupported operation");
	}

	@Override
	public void deleteRecordFromInMemoryDatabase(int id) {
		throw new DAOException("Not implemented yet");
	}

	@Override
	public Class<?> getRecordClass() {
		return LogMessage.class;
	}
}
