package nl.gogognome.gogologbook.gui.logmessage;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ListSelectionModel;

import nl.gogognome.gogologbook.gui.project.ProjectChangedEvent;
import nl.gogognome.gogologbook.gui.session.SessionChangeEvent;
import nl.gogognome.gogologbook.gui.session.SessionListener;
import nl.gogognome.gogologbook.gui.session.SessionManager;
import nl.gogognome.gogologbook.interactors.LogMessageFindInteractor;
import nl.gogognome.gogologbook.interactors.boundary.InteractorFactory;
import nl.gogognome.gogologbook.interactors.boundary.LogMessageFindParams;
import nl.gogognome.gogologbook.interactors.boundary.LogMessageFindResult;
import nl.gogognome.lib.gui.Closeable;
import nl.gogognome.lib.swing.MessageDialog;
import nl.gogognome.lib.swing.views.ViewDialog;

public class LogMessageOverviewController implements Closeable, SessionListener {

	private final LogMessageOverviewModel model = new LogMessageOverviewModel();
	private final LogMessageFindInteractor logMessageFindInteractor = InteractorFactory.getInteractor(LogMessageFindInteractor.class);
	private final Component parentComponent;

	public LogMessageOverviewController(Component parentComponent) {
		SessionManager.getInstance().addSessionListener(this);
		this.parentComponent = parentComponent;
		model.selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		refresh();
	}

	@Override
	public void close() {
		SessionManager.getInstance().removeSessionListener(this);
	}

	public LogMessageOverviewModel getModel() {
		return model;
	}

	public void refresh() {
		LogMessageFindParams params = new LogMessageFindParams();
		List<LogMessageFindResult> logMessages = logMessageFindInteractor.findLogMessagesByDescendingDate(params);
		model.logMessageTableModel.setLogMessages(logMessages);
	}

	public void editLogMessage() {
		int index = model.selectionModel.getMinSelectionIndex();
		if (index == -1) {
			MessageDialog.showInfoMessage(parentComponent, "logMessageOverview_selectRowFirst");
			return;
		}
		LogMessageEditView view = new LogMessageEditView(model.logMessageTableModel.getRow(index));
		new ViewDialog(parentComponent, view).showDialog();
	}

	public Action getRefreshAction() {
		return new RefreshAction();
	}

	public Action getEditAction() {
		return new EditAction();
	}

	@Override
	public void sessionChanged(SessionChangeEvent event) {
		if (event instanceof LogMessageCreatedEvent || event instanceof LogMessageUpdateEvent || event instanceof ProjectChangedEvent) {
			refresh();
		}
	}

	private class RefreshAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			refresh();
		}
	}

	private class EditAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			editLogMessage();
		}
	}
}
