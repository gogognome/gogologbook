package nl.gogognome.gogologbook.dbinsinglefile;

import com.google.gson.Gson;

public class UpdateParser implements Parser {

	private final Gson gson = new Gson();

	@Override
	public void parseAction(SingleFileDatabaseDAO dao, String serializedRecord) {
		Class<?> clazz = dao.getRecordClass();
		Object record = gson.fromJson(serializedRecord, clazz);
		dao.updateRecordInMemoryDatabase(record);
	}

}
