package nl.gogognome.gogologbook.gui.category;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ListSelectionModel;

import nl.gogognome.gogologbook.entities.Category;
import nl.gogognome.gogologbook.gui.session.SessionChangeEvent;
import nl.gogognome.gogologbook.gui.session.SessionListener;
import nl.gogognome.gogologbook.gui.session.SessionManager;
import nl.gogognome.gogologbook.interactors.CategoryInteractor;
import nl.gogognome.gogologbook.interactors.boundary.CannotDeleteCategoryThatIsInUseException;
import nl.gogognome.gogologbook.interactors.boundary.InteractorFactory;
import nl.gogognome.lib.gui.Closeable;
import nl.gogognome.lib.swing.MessageDialog;
import nl.gogognome.lib.swing.views.ViewDialog;

public class CategoriesController implements Closeable, SessionListener {

	private final CategoriesModel model = new CategoriesModel();
	private final Component parent;
	private final CategoryInteractor categoryInteractor = InteractorFactory.getInteractor(CategoryInteractor.class);

	public CategoriesController(Component parent) {
		this.parent = parent;
		refreshCategories();
		model.selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		SessionManager.getInstance().addSessionListener(this);
	}

	public CategoriesModel getModel() {
		return model;
	}

	@Override
	public void close() {
		SessionManager.getInstance().removeSessionListener(this);
	}

	@Override
	public void sessionChanged(SessionChangeEvent event) {
		if (event instanceof CategoryChangedEvent || event instanceof CategoryDeletedEvent) {
			refreshCategories();
		}
	}

	private void refreshCategories() {
		List<Category> categories = categoryInteractor.findAllCategories();
		model.categoriesTableModel.setCategories(categories);
	}

	public Action getAddAction() {
		return new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onAddNewCategory();
			}
		};
	}

	public Action getEditAction() {
		return new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onEditSelectedCategory();
			}
		};
	}

	public Action getDeleteAction() {
		return new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onDeleteSelectedCategory();
			}
		};
	}

	private void onAddNewCategory() {
		AddNewCategoryView view = new AddNewCategoryView();
		new ViewDialog(parent, view).showDialog();
	}

	private void onEditSelectedCategory() {
		int index = model.selectionModel.getMaxSelectionIndex();
		if (index == -1) {
			MessageDialog.showInfoMessage(parent, "editCategory_selectRowFirst");
			return;
		}

		Category category = model.categoriesTableModel.getRow(index);
		EditCategoryView view = new EditCategoryView(category);
		new ViewDialog(parent, view).showDialog();
	}

	private void onDeleteSelectedCategory() {
		int index = model.selectionModel.getMaxSelectionIndex();
		if (index == -1) {
			MessageDialog.showInfoMessage(parent, "editCategory_selectRowFirst");
			return;
		}

		Category category = model.categoriesTableModel.getRow(index);
		int choice = MessageDialog.showYesNoQuestion(parent, "gen.confirmation", "editCategory_confirm_delete_category", category.name);
		if (choice == MessageDialog.YES_OPTION) {
			try {
				categoryInteractor.deleteCategory(category.id);
				SessionManager.getInstance().notifyListeners(new CategoryDeletedEvent(category.id));
			} catch (CannotDeleteCategoryThatIsInUseException e) {
				MessageDialog.showWarningMessage(parent, "editCategory_cannotDeleteCategoryBecauseItIsUsed", category.name);
			}
		}

	}

}
