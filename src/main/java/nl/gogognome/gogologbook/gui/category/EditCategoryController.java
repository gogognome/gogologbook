package nl.gogognome.gogologbook.gui.category;

import nl.gogognome.gogologbook.entities.Category;
import nl.gogognome.gogologbook.gui.session.SessionManager;
import nl.gogognome.gogologbook.interactors.CategoryInteractor;
import nl.gogognome.gogologbook.interactors.boundary.CategoryUpdateParams;
import nl.gogognome.gogologbook.interactors.boundary.InteractorFactory;
import nl.gogognome.lib.swing.MessageDialog;

import org.slf4j.LoggerFactory;

public class EditCategoryController extends AbstractEditCategoryController {

	private final CategoryInteractor categoryInteractor = InteractorFactory.getInteractor(CategoryInteractor.class);

	public EditCategoryController(Category category) {
		model = new EditCategoryModel();

		((EditCategoryModel) model).categoryUnderEdit = category;
		model.nameModel.setString(category.name);
	}

	@Override
	public void save() {
		CategoryUpdateParams params = new CategoryUpdateParams();
		params.categoryId = ((EditCategoryModel) model).categoryUnderEdit.id;
		params.name = model.nameModel.getString();

		try {
			categoryInteractor.updateCategory(params);
			closeAction.actionPerformed(null);
			SessionManager.getInstance().notifyListeners(new CategoryChangedEvent());
		} catch (Exception e) {
			LoggerFactory.getLogger(getClass()).warn("Failed to update category " + params.categoryId, e);
			MessageDialog.showErrorMessage(model.parent, "editCategory_failedToUpdateCategory", e.getMessage());
		}
	}

}
