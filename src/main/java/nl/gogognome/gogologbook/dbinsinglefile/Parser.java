package nl.gogognome.gogologbook.dbinsinglefile;

public interface Parser {

	public abstract void parseAction(SingleFileDatabaseDAO dao, String serializedLine);

}