package nl.gogognome.gogologbook.dbinsinglefile;

import java.io.File;

import org.junit.After;
import org.junit.Before;

public class AbstractSingleFileDAOTest {

	protected final File dbFile = new File("target/test/testdb.txt");

	@Before
	public void setUp() throws InterruptedException {
		dbFile.getParentFile().mkdirs();
		deleteDbFile();
	}

	@After
	public void tearDown() throws InterruptedException {
		deleteDbFile();
	}

	private void deleteDbFile() throws InterruptedException {
		while (dbFile.exists()) {
			dbFile.delete();
			if (dbFile.exists()) {
				Thread.sleep(100);
			}
		}
	}

}
