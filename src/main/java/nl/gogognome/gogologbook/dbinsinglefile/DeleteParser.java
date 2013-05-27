package nl.gogognome.gogologbook.dbinsinglefile;

import com.google.gson.Gson;

public class DeleteParser implements Parser {

	private final SingleFileDatabaseDAORegistry daoRegistry;
	private final ParserHelper parserHelper = new ParserHelper();
	private final Gson gson = new Gson();

	public DeleteParser(SingleFileDatabaseDAORegistry daoRegistry) {
		this.daoRegistry = daoRegistry;
	}

	@Override
	public void parseAction(String line) {
		String serializedId = parserHelper.getSerializedRecord(line);

		int id = gson.fromJson(serializedId, Integer.class);
		SingleFileDatabaseDAO dao = daoRegistry.getDAOForTable(parserHelper.getTableName(line));
		dao.deleteRecordFromInMemoryDatabase(id);
	}

}
