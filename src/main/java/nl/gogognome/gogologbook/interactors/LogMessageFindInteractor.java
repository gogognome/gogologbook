package nl.gogognome.gogologbook.interactors;

import java.util.List;

import nl.gogognome.gogologbook.dao.LogMessageDAO;
import nl.gogognome.gogologbook.entities.FilterCriteria;
import nl.gogognome.gogologbook.entities.LogMessage;
import nl.gogognome.gogologbook.interactors.boundary.LogMessageFindParams;
import nl.gogognome.gogologbook.interactors.boundary.LogMessageFindResult;
import nl.gogognome.gogologbook.util.DaoFactory;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class LogMessageFindInteractor {

	public List<LogMessageFindResult> findMessages(LogMessageFindParams params) {
		FilterCriteria filter = new FilterCriteria();
		List<LogMessage> messages = DaoFactory.getInstance(LogMessageDAO.class).findLogMessages(filter);
		return Lists.newArrayList(Iterables.transform(messages, new MessageToResult()));
	}

}

class MessageToResult implements Function<LogMessage, LogMessageFindResult> {

	public LogMessageFindResult apply(LogMessage message) {
		LogMessageFindResult result = new LogMessageFindResult();
		result.category = message.category;
		result.message = message.message;
		result.project = message.project;
		result.town = message.town;
		result.username = message.username;
		return result;
	}

}