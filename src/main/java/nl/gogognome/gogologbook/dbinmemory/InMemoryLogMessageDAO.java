package nl.gogognome.gogologbook.dbinmemory;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import nl.gogognome.gogologbook.dao.DAOException;
import nl.gogognome.gogologbook.dao.LogMessageDAO;
import nl.gogognome.gogologbook.entities.FilterCriteria;
import nl.gogognome.gogologbook.entities.LogMessage;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class InMemoryLogMessageDAO implements LogMessageDAO {

	private final Map<Integer, LogMessage> idToMessage = Maps.newTreeMap();

	@Override
	public LogMessage createMessage(LogMessage logMessage) {
		int maxId = 0;
		for (int id : idToMessage.keySet()) {
			maxId = Math.max(maxId, id);
		}

		LogMessage storedMessage = cloneLogMessage(logMessage, maxId + 1);
		idToMessage.put(storedMessage.id, storedMessage);
		return cloneLogMessage(storedMessage, storedMessage.id);
	}

	@Override
	public void updateMessage(LogMessage logMessage) {
		LogMessage oldLogMessage = idToMessage.get(logMessage.id);
		if (oldLogMessage == null) {
			throw new DAOException("Cannot update message " + logMessage.id + " because no record with this id exists.");
		}

		LogMessage storedMessage = cloneLogMessage(logMessage, logMessage.id);
		storedMessage.timestamp = oldLogMessage.timestamp;
		idToMessage.put(storedMessage.id, storedMessage);
	}

	@Override
	public List<LogMessage> findLogMessagesByDescendingDate(FilterCriteria filter) {
		List<LogMessage> results = Lists.newArrayListWithExpectedSize(idToMessage.size());
		for (LogMessage message : idToMessage.values()) {
			results.add(cloneLogMessage(message, message.id));
		}
		Collections.sort(results, new DescendingDateComparator());
		return results;
	}

	@Override
	public boolean isProjectUsed(int projectId) {
		for (LogMessage logMessage : idToMessage.values()) {
			if (logMessage.projectId == projectId) {
				return true;
			}
		}
		return false;
	}

	private LogMessage cloneLogMessage(LogMessage origMessage, int clonedId) {
		LogMessage clonedMessage = new LogMessage(clonedId);
		clonedMessage.category = origMessage.category;
		clonedMessage.message = origMessage.message;
		clonedMessage.projectId = origMessage.projectId;
		clonedMessage.timestamp = origMessage.timestamp;
		clonedMessage.userId = origMessage.userId;
		return clonedMessage;
	}

}

class DescendingDateComparator implements Comparator<LogMessage> {

	@Override
	public int compare(LogMessage lm1, LogMessage lm2) {
		if (lm1.timestamp == null && lm2.timestamp == null) {
			return 0;
		}
		if (lm1.timestamp == null) {
			return -1;
		}
		if (lm2.timestamp == null) {
			return 1;
		}
		return -lm1.timestamp.compareTo(lm2.timestamp);
	}

}