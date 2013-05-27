package nl.gogognome.gogologbook.dbinsinglefile;

public interface Parser {

	void parseSerializedLineAndExecuteActionInDAO(SingleFileDatabaseDAO dao, String serializedLine);

	String serializeObject(Object record);

}