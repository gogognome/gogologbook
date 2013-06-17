package nl.gogognome.gogologbook.gui.user;

import nl.gogognome.gogologbook.gui.session.SessionChangeEvent;

public class UserDeletedEvent extends SessionChangeEvent {

	private final int deletedUserId;

	public UserDeletedEvent(int userId) {
		this.deletedUserId = userId;
	}

	public int getDeletedUserId() {
		return deletedUserId;
	}
}
