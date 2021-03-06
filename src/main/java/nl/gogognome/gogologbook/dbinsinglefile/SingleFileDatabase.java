package nl.gogognome.gogologbook.dbinsinglefile;

import java.io.*;
import java.util.Map;

import nl.gogognome.gogologbook.dao.DAOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;

public class SingleFileDatabase {

	private final static String INSERT = "insert";
	private final static String UPDATE = "update";
	private final static String DELETE = "delete";

	private final File dbFile;
	private final BinarySemaphoreWithFileLock semaphore;
	private final Logger logger = LoggerFactory.getLogger(SingleFileDatabase.class);
	private final ParserHelper parserHelper = new ParserHelper();
	private final SingleFileDatabaseDAORegistry daoRegistry = new SingleFileDatabaseDAORegistry();
	private final Gson gson = new Gson();

	private final Map<String, Parser> actionToParser = ImmutableMap.of(
			INSERT, new InsertParser(),
			UPDATE, new UpdateParser(),
			DELETE, new DeleteParser());

	private final Metadata metadata;

	private final static Map<File, BinarySemaphoreWithFileLock> FILE_TO_SEMAPHORE = Maps.newHashMap();

	public SingleFileDatabase(File dbFile) {
		this.dbFile = dbFile;
		this.semaphore = getSemaphoreForFile(dbFile);
		this.metadata = new Metadata();
		this.metadata.databaseVersion = 1;
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
		daoRegistry.registerDao(tableName, dao);
	}

	public void appendInsertToFile(String tableName, Object record) {
		appendRecordToFile(tableName, INSERT, record);
	}

	public void appendUpdateToFile(String tableName, Object record) {
		appendRecordToFile(tableName, UPDATE, record);
	}

	public void appendDeleteToFile(String tableName, int id) {
		appendRecordToFile(tableName, DELETE, id);
	}

	private void appendRecordToFile(String tableName, String action, Object record) {
		ensureMetadataIsWritten();
		BufferedWriter writer = null;
		try {
			writer = createBufferedWriter();
			writer.append(action);
			writer.append(';');
			writer.append(tableName);
			writer.append(';');
			Parser parser = actionToParser.get(action);
			writer.append(parser.serializeObject(record));
			writer.newLine();
		} catch (IOException e) {
			throw new RuntimeException("Problem occurred while writing action " + action + " for table " + tableName + " to the file "
					+ dbFile.getAbsolutePath(), e);
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				logger.warn("Problem occurred while closing file " + dbFile.getAbsolutePath(), e);
			}
		}
	}

	private BufferedWriter createBufferedWriter() throws FileNotFoundException {
		return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dbFile, true), Charsets.ISO_8859_1));
	}

	private void ensureMetadataIsWritten() {
		if (dbFile.length() == 0) {
			BufferedWriter writer = null;
			try {
				writer = createBufferedWriter();
				writer.append(gson.toJson(metadata));
				writer.newLine();
			} catch (IOException e) {
				throw new RuntimeException("Problem occurred while writing metadata to the file " + dbFile.getAbsolutePath(), e);
			} finally {
				try {
					if (writer != null) {
						writer.close();
					}
				} catch (IOException e) {
					logger.warn("Problem occurred while closing file " + dbFile.getAbsolutePath(), e);
				}
			}
		}
	}

	public void initInMemDatabaseFromFile() {
		BufferedReader reader = null;
		try {
			reader = Files.newReader(dbFile, Charsets.ISO_8859_1);
			String serializedMetadata = reader.readLine();
			if (serializedMetadata != null) {
				Metadata fileMetadata = gson.fromJson(serializedMetadata, Metadata.class);
				if (fileMetadata.databaseVersion != metadata.databaseVersion) {
					throw new RuntimeException("File cotains database version " + fileMetadata.databaseVersion + " while version " +
							metadata.databaseVersion + " is expected.");
				}
			}
			for (SingleFileDatabaseDAO dao : daoRegistry.getAllDAOs()) {
				dao.removeAllRecordsFromInMemoryDatabase();
			}

			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				line = line.trim();
                if (!line.isEmpty()) {
                    parseAndExecuteStatement(line);
                }
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
		String action = parserHelper.getAction(line);
		Parser parser = actionToParser.get(action);
		if (parser == null) {
			throw new DAOException("Unknown action in database file: " + action);
		}

		SingleFileDatabaseDAO dao = getSingleFileDatabaseDAO(line);
		String serializedRecord = parserHelper.getSerializedRecord(line);
		parser.parseSerializedLineAndExecuteActionInDAO(dao, serializedRecord);
	}

	private SingleFileDatabaseDAO getSingleFileDatabaseDAO(String line) {
		String tableName = parserHelper.getTableName(line);
		SingleFileDatabaseDAO dao = daoRegistry.getDAOForTable(tableName);
		return dao;
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
