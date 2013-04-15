package nl.gogognome.gogologbook.usecases;

import nl.gogognome.gogologbook.usecases.model.LogMessageCreateParams;

import org.junit.Test;

public class LogMessageCreateInteractorTest {

	private final LogMessageCreateInteractor logMessageCreateInteractor = new LogMessageCreateInteractor();

	@Test
	public void shouldAddValidMessage() {
		LogMessageCreateParams params = new LogMessageCreateParams();
		logMessageCreateInteractor.createMessage(params);
	}
}
