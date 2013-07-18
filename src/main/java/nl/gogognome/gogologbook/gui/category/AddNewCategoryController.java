package nl.gogognome.gogologbook.gui.category;

import nl.gogognome.gogologbook.gui.session.SessionManager;
import nl.gogognome.gogologbook.interactors.CategoryInteractor;
import nl.gogognome.gogologbook.interactors.boundary.CategoryCreateParams;
import nl.gogognome.gogologbook.interactors.boundary.InteractorFactory;
import nl.gogognome.lib.swing.MessageDialog;

import org.slf4j.LoggerFactory;

public class AddNewCategoryController extends AbstractEditCategoryController {

	private final CategoryInteractor CategoryInteractor = InteractorFactory.getInteractor(CategoryInteractor.class);

	public AddNewCategoryController() {
		model = new AddNewCategoryModel();
	}

	@Override
	public void save() {
		CategoryCreateParams params = new CategoryCreateParams();
		params.name = model.nameModel.getString();

		try {
			CategoryInteractor.createCategory(params);
			closeAction.actionPerformed(null);
			SessionManager.getInstance().notifyListeners(new CategoryChangedEvent());
		} catch (Exception e) {
			LoggerFactory.getLogger(getClass()).warn("Failed to create category", e);
			MessageDialog.showErrorMessage(model.parent, "editCategory_failedToCreateCategory", e.getMessage());
		}
	}

}
