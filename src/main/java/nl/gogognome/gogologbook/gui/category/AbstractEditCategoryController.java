package nl.gogognome.gogologbook.gui.category;

import javax.swing.Action;

public abstract class AbstractEditCategoryController {

	protected AbstractEditCategoryModel model;
	protected Action closeAction;

	public AbstractEditCategoryModel getModel() {
		return model;
	}

	public void setCloseAction(Action closeAction) {
		this.closeAction = closeAction;
	}

	public void cancel() {
		closeAction.actionPerformed(null);
	}

	public abstract void save();

}
