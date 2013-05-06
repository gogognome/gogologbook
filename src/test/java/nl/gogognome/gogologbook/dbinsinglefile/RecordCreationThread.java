package nl.gogognome.gogologbook.dbinsinglefile;

import java.io.File;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import nl.gogognome.gogologbook.entities.LogMessage;

import com.google.common.collect.Sets;

public class RecordCreationThread extends Thread {

	private final AtomicBoolean finished = new AtomicBoolean(false);
	private final SingleFileLogMessageDAO singleFileLogMessageDAO;
	private final String message;
	private final Set<Integer> ids = Sets.newHashSetWithExpectedSize(10000);
	private String error;

	public RecordCreationThread(File dbFile, String message) {
		singleFileLogMessageDAO = new SingleFileLogMessageDAO(dbFile);
		this.message = message;
	}

	@Override
	public void run() {
		LogMessage logMessage = new LogMessage();
		logMessage.message = message;
		while (!finished.get() && error == null) {
			int id = singleFileLogMessageDAO.createMessage(logMessage).id;
			if (ids.contains(id)) {
				error = "Id " + id + " was already added by this thread!";
			}
			ids.add(id);
		}
	}

	public void setFinished(boolean finished) {
		this.finished.set(finished);
	}

	public Set<Integer> getIds() {
		return ids;
	}

	public String getError() {
		return error;
	}
}
