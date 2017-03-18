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
    public LogMessage createMessage(LogMessage logMessage) {
        try {
            singleFileDatabase.acquireLock();
            singleFileDatabase.initInMemDatabaseFromFile();
            logMessage = inMemoryLogMessageDao.createMessage(logMessage);
            singleFileDatabase.appendInsertToFile(TABLE_NAME, logMessage);
        } finally {
            singleFileDatabase.releaseLock();
        }
        return logMessage;
    }

    @Override
    public void updateMessage(LogMessage logMessage) {
        try {
            singleFileDatabase.acquireLock();
            singleFileDatabase.initInMemDatabaseFromFile();
            inMemoryLogMessageDao.updateMessage(logMessage);
            singleFileDatabase.appendUpdateToFile(TABLE_NAME, logMessage);
        } finally {
            singleFileDatabase.releaseLock();
        }
    }

    @Override
    public void deleteMessage(int logMessageId) {
        try {
            singleFileDatabase.acquireLock();
            singleFileDatabase.initInMemDatabaseFromFile();
            inMemoryLogMessageDao.deleteMessage(logMessageId);
            singleFileDatabase.appendDeleteToFile(TABLE_NAME, logMessageId);
        } finally {
            singleFileDatabase.releaseLock();
        }
    }

    @Override
    public List<LogMessage> findLogMessagesByDescendingDate(FilterCriteria filter) {
        try {
            singleFileDatabase.acquireLock();
            singleFileDatabase.initInMemDatabaseFromFile();
        } finally {
            singleFileDatabase.releaseLock();
        }
        return inMemoryLogMessageDao.findLogMessagesByDescendingDate(filter);
    }

    @Override
    public boolean isProjectUsed(int projectId) {
        try {
            singleFileDatabase.acquireLock();
            singleFileDatabase.initInMemDatabaseFromFile();
        } finally {
            singleFileDatabase.releaseLock();
        }
        return inMemoryLogMessageDao.isProjectUsed(projectId);
    }

    @Override
    public boolean isUserUsed(int userId) {
        try {
            singleFileDatabase.acquireLock();
            singleFileDatabase.initInMemDatabaseFromFile();
        } finally {
            singleFileDatabase.releaseLock();
        }
        return inMemoryLogMessageDao.isUserUsed(userId);
    }

    @Override
    public boolean isCategoryUsed(String categoryName) {
        try {
            singleFileDatabase.acquireLock();
            singleFileDatabase.initInMemDatabaseFromFile();
        } finally {
            singleFileDatabase.releaseLock();
        }
        return inMemoryLogMessageDao.isCategoryUsed(categoryName);
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
        LogMessage logMessage = (LogMessage) record;
        inMemoryLogMessageDao.updateMessage(logMessage);
    }

    @Override
    public void deleteRecordFromInMemoryDatabase(int logMessageId) {
        inMemoryLogMessageDao.deleteMessage(logMessageId);
    }

    @Override
    public Class<?> getRecordClass() {
        return LogMessage.class;
    }
}
