package nl.gogognome.gogologbook.gui.category;

import nl.gogognome.gogologbook.gui.session.SessionChangeEvent;

public class CategoryDeletedEvent extends SessionChangeEvent {

	private final int deleteCategoryId;

	public CategoryDeletedEvent(int categoryId) {
		this.deleteCategoryId = categoryId;
	}

	public int getDeleteCategoryId() {
		return deleteCategoryId;
	}
}
