package nl.gogognome.gogologbook.dbinsinglefile;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ParserHelperTest {

    @Test
    public void testGetAction() {
        assertGetActionFails(null, NullPointerException.class);
        assertGetActionFails("", IllegalArgumentException.class);
        assertGetActionEquals("one;two", "one");
        assertGetActionEquals("one;two;", "one");
        assertGetActionEquals("one;two;three", "one");
        assertGetActionEquals("one;two;three four", "one");
        assertGetActionEquals("one;two;three four;five", "one");
    }

    private void assertGetActionEquals(String line, String expectedTable) {
        assertEquals(expectedTable, new ParserHelper().getAction(line));
    }

    private void assertGetActionFails(String line, Class<? extends Exception> expectedExceptionClass) {
        try {
            new ParserHelper().getAction(line);
            fail("Expected message not thrown for line " + line);
        } catch (Exception e) {
            assertEquals(expectedExceptionClass, e.getClass());
        }
    }

    @Test
    public void testGetTable() {
        assertGetTableFails(null, NullPointerException.class);
        assertGetTableFails("", IllegalArgumentException.class);
        assertGetTableFails("one;two", IllegalArgumentException.class);
        assertGetTableEquals("one;two;", "two");
        assertGetTableEquals("one;two;three", "two");
        assertGetTableEquals("one;two;three four", "two");
        assertGetTableEquals("one;two;three four;five", "two");
    }

    private void assertGetTableEquals(String line, String expectedTable) {
        assertEquals(expectedTable, new ParserHelper().getTableName(line));
    }

    private void assertGetTableFails(String line, Class<? extends Exception> expectedExceptionClass) {
        try {
            new ParserHelper().getTableName(line);
            fail("Expected message not thrown for line " + line);
        } catch (Exception e) {
            assertEquals(expectedExceptionClass, e.getClass());
        }
    }

    @Test
    public void testGetSerializedRecord() {
        assertGetSerializedRecordFails(null, NullPointerException.class);
        assertGetSerializedRecordFails("", IllegalArgumentException.class);
        assertGetSerializedRecordFails("one;two", IllegalArgumentException.class);
        assertGetSerializedRecordEquals("one;two;", "");
        assertGetSerializedRecordEquals("one;two;three", "three");
        assertGetSerializedRecordEquals("one;two;three four", "three four");
        assertGetSerializedRecordEquals("one;two;three four;five", "three four;five");
    }

    private void assertGetSerializedRecordEquals(String line, String expectedSerializedRecord) {
        assertEquals(expectedSerializedRecord, new ParserHelper().getSerializedRecord(line));
    }

    private void assertGetSerializedRecordFails(String line, Class<? extends Exception> expectedExceptionClass) {
        try {
            new ParserHelper().getSerializedRecord(line);
            fail("Expected message not thrown for line " + line);
        } catch (Exception e) {
            assertEquals(expectedExceptionClass, e.getClass());
        }
    }

}