package nl.gogognome.gogologbook.dbinsinglefile;

import java.io.*;
import java.util.List;

import nl.gogognome.gogologbook.dao.UserDAO;
import nl.gogognome.gogologbook.dbinmemory.InMemoryUserDAO;
import nl.gogognome.gogologbook.entities.User;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;

public class SingleFileUserDAO extends AbstractSingleFileDAO implements UserDAO {

	private final static String INSERT = "insert";

	private InMemoryUserDAO inMemoryUserDao = new InMemoryUserDAO();

	public SingleFileUserDAO(File dbFile) {
		super(dbFile);
	}

	@Override
	public User createUser(User user) {
		try {
			acquireLock();
			initInMemDatabaseFromFile();
			user = inMemoryUserDao.createUser(user);
			try {
				appendRecordToFile(INSERT, user);
			} catch (IOException e) {
				throw new RuntimeException("Problem occurred while writing record to the file " + dbFile.getAbsolutePath(), e);
			}
		} finally {
			releaseLock();
		}
		return user;
	}

	@Override
	public List<User> findAllUsers() {
		initInMemDatabaseFromFile();
		return inMemoryUserDao.findAllUsers();
	}

	private void appendRecordToFile(String action, User user) throws IOException {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(dbFile, true));
			writer.append(action);
			writer.append(';');
			Gson gson = new Gson();
			writer.append(gson.toJson(user));
			writer.newLine();
		} finally {
			writer.flush();
			writer.close();
		}
	}

	private void initInMemDatabaseFromFile() {
		BufferedReader reader = null;
		try {
			inMemoryUserDao = new InMemoryUserDAO();

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
			String serializedUser = line.substring(index + 1);
			User user = gson.fromJson(serializedUser, User.class);
			inMemoryUserDao.createUser(user);
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
