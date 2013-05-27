package nl.gogognome.gogologbook.dbinsinglefile;

import com.google.gson.Gson;

public class InsertParser implements Parser {

	private final SingleFileDatabaseDAORegistry daoRegistry;
	private final ParserHelper parserHelper = new ParserHelper();
	private final Gson gson = new Gson();

	public InsertParser(SingleFileDatabaseDAORegistry daoRegistry) {
		this.daoRegistry = daoRegistry;
	}

	@Override
	public void parseAction(String line) {
		SingleFileDatabaseDAO dao = getSingleFileDatabaseDAO(line);
		Class<?> clazz = dao.getRecordClass();
		Object record = gson.fromJson(parserHelper.getSerializedRecord(line), clazz);
		dao.createRecordInMemoryDatabase(record);
	}

	private SingleFileDatabaseDAO getSingleFileDatabaseDAO(String line) {
		String tableName = parserHelper.getTableName(line);
		SingleFileDatabaseDAO dao = daoRegistry.getDAOForTable(tableName);
		return dao;
	}

}
