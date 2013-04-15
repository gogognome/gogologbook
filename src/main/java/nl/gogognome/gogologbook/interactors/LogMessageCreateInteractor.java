package nl.gogognome.gogologbook.interactors;

import nl.gogognome.gogologbook.dao.LogMessageDAO;
import nl.gogognome.gogologbook.entities.LogMessage;
import nl.gogognome.gogologbook.interactors.boundary.LogMessageCreateParams;
import nl.gogognome.gogologbook.util.DaoFactory;

public class LogMessageCreateInteractor {

	public void createMessage(LogMessageCreateParams params) {
		LogMessage message = new LogMessage();
		message.category = params.category;
		message.message = params.message;
		message.project = params.project;
		message.town = params.town;
		message.username = params.username;

		DaoFactory.getInstance(LogMessageDAO.class).createMessage(message);
	}

}
