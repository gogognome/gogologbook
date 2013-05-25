package nl.gogognome.gogologbook.gui.session;


public class SessionManager {

	private final static Session instance = new Session();

	public static Session getInstance() {
		return instance;
	}

}
