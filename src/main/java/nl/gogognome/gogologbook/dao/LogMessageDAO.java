package nl.gogognome.gogologbook.dao;

import java.util.List;

import nl.gogognome.gogologbook.entities.FilterCriteria;
import nl.gogognome.gogologbook.entities.LogMessage;

public interface LogMessageDAO {

	void createMessage(LogMessage message);

	List<LogMessage> findLogMessages(FilterCriteria filter);

}
