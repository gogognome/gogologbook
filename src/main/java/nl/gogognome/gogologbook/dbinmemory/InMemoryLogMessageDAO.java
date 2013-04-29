package nl.gogognome.gogologbook.dbinmemory;

import java.util.List;
import java.util.Map;

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
	public List<LogMessage> findLogMessages(FilterCriteria filter) {
		List<LogMessage> results = Lists.newArrayListWithExpectedSize(idToMessage.size());
		for (LogMessage message : idToMessage.values()) {
			results.add(cloneLogMessage(message, message.id));
		}
		return results;
	}

	private LogMessage cloneLogMessage(LogMessage origMessage, int clonedId) {
		LogMessage clonedMessage = new LogMessage(clonedId);
		clonedMessage.category = origMessage.category;
		clonedMessage.message = origMessage.message;
		clonedMessage.project = origMessage.project;
		clonedMessage.town = origMessage.town;
		clonedMessage.username = origMessage.username;
		return clonedMessage;
	}

}
