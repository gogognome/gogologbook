package nl.gogognome.gogologbook.gui;

import nl.gogognome.lib.text.TextResource;

public class UsagePrint {

	private final TextResource textResource;

	public UsagePrint(TextResource textResource) {
		this.textResource = textResource;
	}

	public void printUsage() {
		int n = 1;
		while (n != Integer.MAX_VALUE) {
			String lineId = "usage.line" + n;
			if (textResource.containsString(lineId)) {
				String line = textResource.getString(lineId);
				System.out.println(line);
				n += 1;
			} else {
				break;
			}
		}
	}

}
