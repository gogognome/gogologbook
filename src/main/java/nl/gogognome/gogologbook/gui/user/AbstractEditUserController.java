package nl.gogognome.gogologbook.gui.user;

import javax.swing.Action;

public abstract class AbstractEditUserController {

	protected AbstractEditUserModel model;
	protected Action closeAction;

	public AbstractEditUserModel getModel() {
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
