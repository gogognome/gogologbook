package nl.gogognome.gogologbook.dbinsinglefile;

import java.io.*;
import java.util.List;
import java.util.Map;

import nl.gogognome.gogologbook.dao.LogMessageDAO;
import nl.gogognome.gogologbook.dbinmemory.InMemoryLogMessageDAO;
import nl.gogognome.gogologbook.entities.FilterCriteria;
import nl.gogognome.gogologbook.entities.LogMessage;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;

public class SingleFileLogMessageDAO implements LogMessageDAO {

	private final static String INSERT = "insert";

	private final File dbFile;

	private InMemoryLogMessageDAO inMemoryLogMessageDao = new InMemoryLogMessageDAO();

	private final static Map<File, BinarySemaphoreWithFileLock> fileToSemaphoreMap = Maps.newHashMap();

	public SingleFileLogMessageDAO(File dbFile) {
		this.dbFile = dbFile;
	}

	@Override
	public LogMessage createMessage(LogMessage message) {
		try {
			acquireLock();
			initInMemDatabaseFromFile();
			message = inMemoryLogMessageDao.createMessage(message);
			try {
				appendRecordToFile(INSERT, message);
			} catch (IOException e) {
				throw new RuntimeException("Problem occurred while writing record to the file " + dbFile.getAbsolutePath(), e);
			}
		} finally {
			releaseLock();
		}
		return message;
	}

	private void acquireLock() {
		createSemaphoreIfNeeded();
		try {
			fileToSemaphoreMap.get(dbFile).acquire();
		} catch (Exception e) {
			throw new RuntimeException("Problem occurred while releasing lock", e);
		}
	}

	private void createSemaphoreIfNeeded() {
		if (fileToSemaphoreMap.containsKey(dbFile)) {
			return;
		}

		fileToSemaphoreMap.put(dbFile, new BinarySemaphoreWithFileLock(new File(dbFile.getAbsoluteFile() + ".lock")));
	}

	private void releaseLock() {
		try {
			fileToSemaphoreMap.get(dbFile).release();
		} catch (Exception e) {
			throw new RuntimeException("Problem occurred while releasing lock", e);
		}
	}

	@Override
	public List<LogMessage> findLogMessages(FilterCriteria filter) {
		initInMemDatabaseFromFile();
		return inMemoryLogMessageDao.findLogMessages(filter);
	}

	private void appendRecordToFile(String action, LogMessage logMessage) throws IOException {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(dbFile, true));
			writer.append(action);
			writer.append(';');
			Gson gson = new Gson();
			writer.append(gson.toJson(logMessage));
			writer.newLine();
		} finally {
			writer.flush();
			writer.close();
		}
	}

	private void initInMemDatabaseFromFile() {
		BufferedReader reader = null;
		try {
			inMemoryLogMessageDao = new InMemoryLogMessageDAO();

			reader = Files.newReader(dbFile, Charsets.ISO_8859_1);
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				parseAndExecuteStatement(line);
			}
		} catch (FileNotFoundException e) {
			return;
		} catch (IOException e) {
			throw new RuntimeException("Failed to initialize database from file " + dbFile.getAbsolutePath(), e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				throw new RuntimeException("Failed to close file " + dbFile.getAbsolutePath() + " after reading", e);
			}
		}
	}

	private void parseAndExecuteStatement(String line) {
		String action = getAction(line);
		if (INSERT.equals(action)) {
			Gson gson = new Gson();
			int index = line.indexOf(';');
			String serializedLogMessage = line.substring(index + 1);
			LogMessage logMessage = gson.fromJson(serializedLogMessage, LogMessage.class);
			inMemoryLogMessageDao.createMessage(logMessage);
		}
	}

	private String getAction(String line) {
		int index = line.indexOf(';');
		if (index == -1) {
			throw new RuntimeException("Line does not contain semicolon.");
		}
		return line.substring(0, index);
	}

}
