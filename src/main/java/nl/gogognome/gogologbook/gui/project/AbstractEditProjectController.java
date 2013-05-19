package nl.gogognome.gogologbook.gui.project;

import javax.swing.Action;

public abstract class AbstractEditProjectController {

	protected AbstractEditProjectModel model;
	protected Action closeAction;

	public AbstractEditProjectModel getModel() {
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
