package nl.gogognome.gogologbook.gui.category;

import nl.gogognome.gogologbook.entities.Category;

public class EditCategoryView extends AbstractEditCategoryView {

	private static final long serialVersionUID = 1L;

	public EditCategoryView(Category category) {
		controller = new EditCategoryController(category);
		model = controller.getModel();
	}

	@Override
	public String getTitle() {
		return textResource.getString("edit_category_title");
	}

}
