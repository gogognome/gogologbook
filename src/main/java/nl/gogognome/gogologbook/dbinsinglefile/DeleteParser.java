package nl.gogognome.gogologbook.dbinsinglefile;

import com.google.gson.Gson;

public class DeleteParser implements Parser {

	private final Gson gson = new Gson();

	@Override
	public void parseSerializedLineAndExecuteActionInDAO(SingleFileDatabaseDAO dao, String serializedRecord) {
		int id = gson.fromJson(serializedRecord, Integer.class);
		dao.deleteRecordFromInMemoryDatabase(id);
	}

	@Override
	public String serializeObject(Object record) {
		return gson.toJson(record);
	}

}
