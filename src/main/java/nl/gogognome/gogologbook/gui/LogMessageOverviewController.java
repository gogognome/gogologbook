package nl.gogognome.gogologbook.gui;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

import nl.gogognome.gogologbook.interactors.LogMessageFindInteractor;
import nl.gogognome.gogologbook.interactors.boundary.InteractorFactory;
import nl.gogognome.gogologbook.interactors.boundary.LogMessageFindParams;
import nl.gogognome.gogologbook.interactors.boundary.LogMessageFindResult;

public class LogMessageOverviewController {

	private final LogMessageOverviewModel model = new LogMessageOverviewModel();
	private final LogMessageFindInteractor logMessageFindInteractor = InteractorFactory.getInteractor(LogMessageFindInteractor.class);

	public LogMessageOverviewController() {
		refresh();
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

	private class RefreshAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			refresh();
		}

	}
}
