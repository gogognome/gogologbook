package nl.gogognome.gogologbook.dbinsinglefile;

import java.io.*;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;

public class SingleFileDatabase {

	private final static String INSERT = "insert";

	private final File dbFile;
	private final Map<String, SingleFileDatabaseDAO> tableNameToSingleFileDatabaseDao = Maps.newHashMap();
	private final BinarySemaphoreWithFileLock semaphore; // TODO: create one semaphore per dbFile. Cache semaphore per file.
	private final Logger logger = LoggerFactory.getLogger(SingleFileDatabase.class);

	private final static Map<File, BinarySemaphoreWithFileLock> FILE_TO_SEMAPHORE = Maps.newHashMap();

	public SingleFileDatabase(File dbFile) {
		this.dbFile = dbFile;
		this.semaphore = getSemaphoreForFile(dbFile);
	}

	private static synchronized BinarySemaphoreWithFileLock getSemaphoreForFile(File dbFile) {
		BinarySemaphoreWithFileLock semaphore = FILE_TO_SEMAPHORE.get(dbFile);
		if (semaphore == null) {
			semaphore = new BinarySemaphoreWithFileLock(new File(dbFile.getAbsolutePath() + ".lock"));
			FILE_TO_SEMAPHORE.put(dbFile, semaphore);
		}
		return semaphore;
	}

	public void registerDao(String tableName, SingleFileDatabaseDAO dao) {
		tableNameToSingleFileDatabaseDao.put(tableName, dao);
	}

	public void appendInsertToFile(String tableName, Object record) {
		appendRecordToFile(tableName, INSERT, record);
	}

	private void appendRecordToFile(String tableName, String action, Object record) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(dbFile, true));
			writer.append(action);
			writer.append(';');
			writer.append(tableName);
			writer.append(';');
			Gson gson = new Gson();
			writer.append(gson.toJson(record));
			writer.newLine();
		} catch (IOException e) {
			throw new RuntimeException("Problem occurred while writing action " + action + " for table " + tableName + " to the file "
					+ dbFile.getAbsolutePath(), e);
		} finally {
			try {
				writer.flush();
				writer.close();
			} catch (IOException e) {
				logger.warn("Problem occurred while closing file " + dbFile.getAbsolutePath(), e);
			}
		}
	}

	public void initInMemDatabaseFromFile() {
		BufferedReader reader = null;
		try {
			for (SingleFileDatabaseDAO dao : tableNameToSingleFileDatabaseDao.values()) {
				dao.removeAllRecordsFromInMemoryDatabase();
			}

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
		String tableName = getTableName(line);
		if (INSERT.equals(action)) {
			Gson gson = new Gson();
			int index = line.indexOf(';');
			index = line.indexOf(';', index + 1);
			String serializedRecord = line.substring(index + 1);
			SingleFileDatabaseDAO dao = tableNameToSingleFileDatabaseDao.get(tableName);
			Class<?> clazz = dao.getRecordClass();
			Object record = gson.fromJson(serializedRecord, clazz);
			dao.createRecordInMemoryDatabase(record);
		}
	}

	private String getAction(String line) {
		int index = line.indexOf(';');
		if (index == -1) {
			throw new RuntimeException("Line does not contain semicolon: " + line);
		}
		return line.substring(0, index);
	}

	private String getTableName(String line) {
		int index = line.indexOf(';');
		if (index == -1) {
			throw new RuntimeException("Line does not contain semicolon: " + line);
		}
		int start = index + 1;
		index = line.indexOf(';', start);
		if (index == -1) {
			throw new RuntimeException("Line does not contain two semicolons: " + line);
		}

		return line.substring(start, index);
	}

	public void acquireLock() {
		try {
			semaphore.acquire();
		} catch (Exception e) {
			throw new RuntimeException("Problem occurred while acquiring lock.", e);
		}
	}

	public void releaseLock() {
		try {
			semaphore.release();
		} catch (Exception e) {
			throw new RuntimeException("Problem occurred while releasing lock.", e);
		}
	}

}
