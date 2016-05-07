package nl.gogognome.gogologbook.dbinsinglefile;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class ParserHelperTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void givenNoLineWhenActionIsAskedShouldThrowException() {
        expectNullPointerException();

        new ParserHelper().getAction(null);
    }

    @Test
    public void givenEmptyLineWhenActionIsAskedShouldThrowException() {
        expectIllegalArgumentException("Line does not contain semicolon: ");

        new ParserHelper().getAction("");
    }

    @Test
    public void givenLineWithoutSemicolonWhenActionIsAskedShouldThrowException() {
        expectIllegalArgumentException("Line does not contain semicolon: asdasd");

        new ParserHelper().getAction("asdasd");
    }

    @Test
    public void givenNoLineWhenSerializedRecordIsAskedShouldThrowException() {
        expectNullPointerException();

        new ParserHelper().getSerializedRecord(null);
    }

    @Test
    public void givenEmptyLineWhenSerializedRecordIsAskedShouldThrowException() {
        expectIllegalArgumentException("Line does not contain semicolon: ");

        new ParserHelper().getSerializedRecord("");
    }

    @Test
    public void givenLineWithoutSemicolonWhenSerializedRecordIsAskedShouldThrowException() {
        expectIllegalArgumentException("Line does not contain semicolon: asdasd");

        new ParserHelper().getSerializedRecord("asdasd");
    }

    @Test
    public void givenNoLineWhenTableNameIsAskedShouldThrowException() {
        expectNullPointerException();

        new ParserHelper().getTableName(null);
    }

    @Test
    public void givenEmptyLineWhenTableNameIsAskedShouldThrowException() {
        expectIllegalArgumentException("Line does not contain semicolon: ");

        new ParserHelper().getTableName("");
    }

    @Test
    public void givenLineWithoutSemicolonWhenTableNameIsAskedShouldThrowException() {
        expectIllegalArgumentException("Line does not contain semicolon: asdasd");

        new ParserHelper().getTableName("asdasd");
    }

    @Test
    public void givenLineWithOneSemicolonWhenTableNameIsAskedShouldThrowException() {
        expectIllegalArgumentException("Line does not contain two semicolons: insert;bladiable");

        new ParserHelper().getTableName("insert;bladiable");
    }

    private void expectNullPointerException() {
        exception.expect(NullPointerException.class);
    }

    private void expectIllegalArgumentException(String expectedMessage) {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(expectedMessage);
    }
}