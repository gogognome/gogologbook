package nl.gogognome.gogologbook.interactors;

import static org.junit.Assert.*;
import nl.gogognome.gogologbook.interactors.boundary.LogMessageFindParams;
import nl.gogognome.gogologbook.interactors.boundary.LogMessageFindResult;
import nl.gogognome.lib.util.DateUtil;

import org.junit.Test;

public class FilterPredicateTest {

	private final LogMessageFindParams params = new LogMessageFindParams();
	private final LogMessageFindResult logMessage = new LogMessageFindResult();

	@Test
	public void shouldAcceptFromTimestamp() {
		params.from = DateUtil.createDate(2013, 1, 1);
		logMessage.timestamp = DateUtil.createDate(2013, 5, 1);
		assertTrue(filterAcceptsLogMessage());
	}

	@Test
	public void shouldRejectFromTimestamp() {
		params.from = DateUtil.createDate(2013, 5, 1);
		logMessage.timestamp = DateUtil.createDate(2013, 1, 1);
		assertFalse(filterAcceptsLogMessage());
	}

	@Test
	public void shouldAcceptToTimestamp() {
		params.to = DateUtil.createDate(2013, 5, 1);
		logMessage.timestamp = DateUtil.createDate(2013, 1, 1);
		assertTrue(filterAcceptsLogMessage());
	}

	@Test
	public void shouldRejectToTimestamp() {
		params.to = DateUtil.createDate(2013, 1, 1);
		logMessage.timestamp = DateUtil.createDate(2013, 5, 1);
		assertFalse(filterAcceptsLogMessage());
	}

	@Test
	public void shouldAcceptUser() {
		params.user = "cd";
		logMessage.username = "ABCD";
		assertTrue(filterAcceptsLogMessage());
	}

	@Test
	public void shouldRejectUser() {
		params.user = "EF";
		logMessage.username = "ABCD";
		assertFalse(filterAcceptsLogMessage());
	}

	@Test
	public void shouldAcceptProject() {
		params.project = "cd";
		logMessage.projectNr = "ABCD";
		assertTrue(filterAcceptsLogMessage());
	}

	@Test
	public void shouldRejectProject() {
		params.project = "EF";
		logMessage.projectNr = "ABCD";
		assertFalse(filterAcceptsLogMessage());
	}

	@Test
	public void shouldAcceptCustomer() {
		params.customer = "cd";
		logMessage.customer = "ABCD";
		assertTrue(filterAcceptsLogMessage());
	}

	@Test
	public void shouldRejectCustomer() {
		params.customer = "EF";
		logMessage.customer = "ABCD";
		assertFalse(filterAcceptsLogMessage());
	}

	@Test
	public void shouldAcceptTown() {
		params.town = "cd";
		logMessage.town = "ABCD";
		assertTrue(filterAcceptsLogMessage());
	}

	@Test
	public void shouldRejectTown() {
		params.town = "EF";
		logMessage.town = "ABCD";
		assertFalse(filterAcceptsLogMessage());
	}

	@Test
	public void shouldAcceptCategory() {
		params.category = "cd";
		logMessage.category = "ABCD";
		assertTrue(filterAcceptsLogMessage());
	}

	@Test
	public void shouldRejectCategory() {
		params.category = "EF";
		logMessage.category = "ABCD";
		assertFalse(filterAcceptsLogMessage());
	}

	@Test
	public void shouldAcceptMessage() {
		params.message = "cd";
		logMessage.message = "ABCD";
		assertTrue(filterAcceptsLogMessage());
	}

	@Test
	public void shouldRejectMessage() {
		params.message = "EF";
		logMessage.message = "ABCD";
		assertFalse(filterAcceptsLogMessage());
	}

	private boolean filterAcceptsLogMessage() {
		return new FilterPredicate(params).apply(logMessage);
	}

}
