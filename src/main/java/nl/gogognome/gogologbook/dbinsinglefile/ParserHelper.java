package nl.gogognome.gogologbook.dbinsinglefile;

public class ParserHelper {

	public String getSerializedRecord(String line) {
		int index = getSecondSemiColonIndex(line);
		return line.substring(index + 1);
	}

	public String getAction(String line) {
		int index = getFirstSemiColonIndex(line);
		return line.substring(0, index);
	}

	public String getTableName(String line) {
		return line.substring(getFirstSemiColonIndex(line) + 1, getSecondSemiColonIndex(line));
	}

	private int getFirstSemiColonIndex(String line) {
        return getNextSemiColonIndex(line, 0);
	}

	private int getSecondSemiColonIndex(String line) {
        return getNextSemiColonIndex(line, getFirstSemiColonIndex(line) + 1);
	}

    private int getNextSemiColonIndex(String line, int startFromIndex) {
        int index = line.indexOf(';', startFromIndex);
        if (index == -1) {
            throw new IllegalArgumentException("Line does not contain two semicolons: " + line);
        }
        return index;
    }

}
