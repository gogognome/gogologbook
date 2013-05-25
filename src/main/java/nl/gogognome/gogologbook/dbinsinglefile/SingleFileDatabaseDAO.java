package nl.gogognome.gogologbook.dbinsinglefile;

public interface SingleFileDatabaseDAO {

	void removeAllRecordsFromInMemoryDatabase();

	void createRecordInMemoryDatabase(Object record);

	void updateRecordInMemoryDatabase(Object record);

	Class<?> getRecordClass();

	void deleteRecordFromInMemoryDatabase(int id);

}
