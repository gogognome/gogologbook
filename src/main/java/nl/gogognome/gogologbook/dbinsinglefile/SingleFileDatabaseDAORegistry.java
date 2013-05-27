package nl.gogognome.gogologbook.dbinsinglefile;

import java.util.Map;

import com.google.common.collect.Maps;

public class SingleFileDatabaseDAORegistry {

	private final Map<String, SingleFileDatabaseDAO> tableNameToSingleFileDatabaseDao = Maps.newHashMap();

	public void registerDao(String tableName, SingleFileDatabaseDAO dao) {
		tableNameToSingleFileDatabaseDao.put(tableName, dao);
	}

	public SingleFileDatabaseDAO getDAOForTable(String tableName) {
		return tableNameToSingleFileDatabaseDao.get(tableName);
	}

	public Iterable<SingleFileDatabaseDAO> getAllDAOs() {
		return tableNameToSingleFileDatabaseDao.values();
	}

}
