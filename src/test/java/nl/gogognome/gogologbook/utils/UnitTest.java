package nl.gogognome.gogologbook.utils;

import static org.mockito.Mockito.*;
import nl.gogognome.gogologbook.util.DaoFactory;

import org.junit.After;

public class UnitTest {

	public <T> T mockAndRegisterDAO(Class<T> daoClass) {
		T daoMock = mock(daoClass);
		DaoFactory.register(daoClass, daoMock);
		return daoMock;
	}

	@After
	public void unregisterMocks() {
		DaoFactory.clear();
	}
}
