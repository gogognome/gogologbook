package nl.gogognome.gogologbook.dbinsinglefile;

import com.google.gson.Gson;

public class InsertParser implements Parser {

	private final Gson gson = new Gson();

	@Override
	public void parseSerializedLineAndExecuteActionInDAO(SingleFileDatabaseDAO dao, String serializedRecord) {
		Class<?> clazz = dao.getRecordClass();
		Object record = gson.fromJson(serializedRecord, clazz);
		dao.createRecordInMemoryDatabase(record);
	}

	@Override
	public String serializeObject(Object record) {
		return gson.toJson(record);
	}

}
