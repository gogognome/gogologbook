package nl.gogognome.gogologbook.interactors;

public class CannotDeleteProjectThatIsInUseException extends Exception {

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
