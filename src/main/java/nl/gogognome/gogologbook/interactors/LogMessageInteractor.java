package nl.gogognome.gogologbook.interactors;

import nl.gogognome.gogologbook.dao.LogMessageDAO;
import nl.gogognome.gogologbook.entities.LogMessage;
import nl.gogognome.gogologbook.interactors.boundary.LogMessageCreateParams;
import nl.gogognome.gogologbook.interactors.boundary.LogMessageUpdateParams;
import nl.gogognome.gogologbook.util.DaoFactory;

public class LogMessageInteractor {

	public void createMessage(LogMessageCreateParams params) {
		LogMessage message = new LogMessage();
		message.category = params.category;
		message.message = params.message;
		message.projectId = params.projectId;
		message.userId = params.userId;
		message.timestamp = params.timestamp;

		DaoFactory.getInstance(LogMessageDAO.class).createMessage(message);
	}

	public void updateMessage(LogMessageUpdateParams params) {
		LogMessage message = new LogMessage(params.id);
		message.category = params.category;
		message.message = params.message;
		message.projectId = params.projectId;
		message.timestamp = params.timestamp;
		message.userId = params.userId;

		DaoFactory.getInstance(LogMessageDAO.class).updateMessage(message);
	}

}
