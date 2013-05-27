package nl.gogognome.gogologbook.dbinsinglefile;

public class ParserHelper {

	public String getSerializedRecord(String line) {
		int index = line.indexOf(';');
		index = line.indexOf(';', index + 1);
		String serializedRecord = line.substring(index + 1);
		return serializedRecord;
	}

	public String getAction(String line) {
		int index = line.indexOf(';');
		if (index == -1) {
			throw new RuntimeException("Line does not contain semicolon: " + line);
		}
		return line.substring(0, index);
	}

	public String getTableName(String line) {
		int index = line.indexOf(';');
		if (index == -1) {
			throw new RuntimeException("Line does not contain semicolon: " + line);
		}
		int start = index + 1;
		index = line.indexOf(';', start);
		if (index == -1) {
			throw new RuntimeException("Line does not contain two semicolons: " + line);
		}

		return line.substring(start, index);
	}

}
