package nl.gogognome.gogologbook.entities;

import nl.gogognome.gogologbook.util.AbstractEntity;

public class LogMessage extends AbstractEntity {

	public LogMessage() {
		super();
	}

	public LogMessage(int id) {
		super(id);
	}

	public int userId;
	public int projectId;
	public String category;
	public String message;

}
