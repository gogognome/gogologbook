package nl.gogognome.gogologbook.dbinsinglefile;

import com.google.common.base.Charsets;
import nl.gogognome.gogologbook.entities.LogMessage;
import nl.gogognome.gogologbook.entities.User;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.Collections;

public class SingleFileDatabaseTest extends AbstractSingleFileDAOTest {

    private final SingleFileDatabase singleFileDatabase = new SingleFileDatabase(dbFile);

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void whenFileContainsWrongVersionNumberReadingShouldThrowsException() throws IOException {
        Files.write(dbFile.toPath(), Collections.singletonList("{\"databaseVersion\":0}\n"), Charsets.ISO_8859_1);

        exception.expect(RuntimeException.class);
        exception.expectMessage("File cotains database version 0 while version 1 is expected.");

        singleFileDatabase.initInMemDatabaseFromFile();
    }

    @Test
    public void whenFileContainsEmptyLinesTheseShouldBeIgnored() throws IOException {
        singleFileDatabase.initInMemDatabaseFromFile();
        new SingleFileUserDAO(singleFileDatabase).createUser(new User());

        Files.write(dbFile.toPath(), Collections.singletonList("\n"), StandardOpenOption.APPEND);

        singleFileDatabase.initInMemDatabaseFromFile();
    }
}
