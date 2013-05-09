package nl.gogognome.gogologbook.dbinsinglefile;

public interface SingleFileDatabaseDAO {

	void removeAllRecordsFromInMemoryDatabase();

	void createRecordInMemoryDatabase(Object record);

	Class<?> getRecordClass();

}
