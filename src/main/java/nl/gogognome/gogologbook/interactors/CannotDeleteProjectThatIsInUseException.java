package nl.gogognome.gogologbook.interactors;

import nl.gogognome.gogologbook.dao.DAOException;

public class CannotDeleteProjectThatIsInUseException extends DAOException {

	public CannotDeleteProjectThatIsInUseException() {
		super();
	}

	public CannotDeleteProjectThatIsInUseException(String message, Throwable cause) {
		super(message, cause);
	}

	public CannotDeleteProjectThatIsInUseException(String message) {
		super(message);
	}

	public CannotDeleteProjectThatIsInUseException(Throwable cause) {
		super(cause);
	}

}
