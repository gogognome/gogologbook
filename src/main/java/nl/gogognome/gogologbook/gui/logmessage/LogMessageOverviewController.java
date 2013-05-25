package nl.gogognome.gogologbook.gui.logmessage;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

import nl.gogognome.gogologbook.gui.session.SessionChangeEvent;
import nl.gogognome.gogologbook.gui.session.SessionListener;
import nl.gogognome.gogologbook.gui.session.SessionManager;
import nl.gogognome.gogologbook.interactors.LogMessageFindInteractor;
import nl.gogognome.gogologbook.interactors.boundary.InteractorFactory;
import nl.gogognome.gogologbook.interactors.boundary.LogMessageFindParams;
import nl.gogognome.gogologbook.interactors.boundary.LogMessageFindResult;
import nl.gogognome.lib.gui.Closeable;

public class LogMessageOverviewController implements Closeable, SessionListener {

	private final LogMessageOverviewModel model = new LogMessageOverviewModel();
	private final LogMessageFindInteractor logMessageFindInteractor = InteractorFactory.getInteractor(LogMessageFindInteractor.class);

	public LogMessageOverviewController() {
		SessionManager.getInstance().addSessionListener(this);
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
		List<LogMessageFindResult> logMessages = logMessageFindInteractor.findMessages(params);
		model.logMessageTableModel.setLogMessages(logMessages);
	}

	public Action getRefreshAction() {
		return new RefreshAction();
	}

	@Override
	public void sessionChanged(SessionChangeEvent event) {
		if (event instanceof LogMessageCreatedEvent) {
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
}
