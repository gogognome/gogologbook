package nl.gogognome.gogologbook.gui.category;


public class AddNewCategoryView extends AbstractEditCategoryView {

	private static final long serialVersionUID = 1L;

	public AddNewCategoryView() {
		controller = new AddNewCategoryController();
		model = controller.getModel();
	}

	@Override
	public String getTitle() {
		return textResource.getString("add_category_title");
	}
}
