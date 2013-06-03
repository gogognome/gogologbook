package nl.gogognome.gogologbook.gui.logmessage;

import nl.gogognome.gogologbook.entities.User;
import nl.gogognome.lib.gui.beans.ObjectFormatter;

class UserFormatter implements ObjectFormatter<User> {
	@Override
	public String format(User user) {
		return user != null ? user.name : "";
	}
}