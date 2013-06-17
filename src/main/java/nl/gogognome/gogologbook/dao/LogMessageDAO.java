package nl.gogognome.gogologbook.dao;

import java.util.List;

import nl.gogognome.gogologbook.entities.FilterCriteria;
import nl.gogognome.gogologbook.entities.LogMessage;

public interface LogMessageDAO {

	LogMessage createMessage(LogMessage message);

	void updateMessage(LogMessage message);

	List<LogMessage> findLogMessagesByDescendingDate(FilterCriteria filter);

	boolean isProjectUsed(int projectId);

	boolean isUserUsed(int userId);

}
