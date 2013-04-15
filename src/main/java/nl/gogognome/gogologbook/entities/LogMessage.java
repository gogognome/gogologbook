package nl.gogognome.gogologbook.entities;

import nl.gogognome.gogologbook.util.AbstractEntity;

public class LogMessage extends AbstractEntity {

	public LogMessage() {
		super();
	}

	public LogMessage(int id) {
		super(id);
	}

	public String username;
	public String project;
	public String town;
	public String category;
	public String message;

}
