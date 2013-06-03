package nl.gogognome.gogologbook.gui.logmessage;

import nl.gogognome.gogologbook.entities.Category;
import nl.gogognome.lib.gui.beans.ObjectFormatter;

class CategoryFormatter implements ObjectFormatter<Category> {
	@Override
	public String format(Category category) {
		return category != null ? category.name : "";
	}
}