package nl.gogognome.gogologbook.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import nl.gogognome.gogologbook.interactors.LogMessageCreateInteractor;
import nl.gogognome.gogologbook.interactors.boundary.LogMessageCreateParams;
import nl.gogognome.lib.text.TextResource;
import nl.gogognome.lib.util.Factory;

import org.slf4j.LoggerFactory;

public class LogMessageCreateController {

	private final LogMessageCreateModel model = new LogMessageCreateModel();
	private final TextResource textResource = Factory.getInstance(TextResource.class);

	public LogMessageCreateModel getModel() {
		return model;
	}

	public Action getCreateAction() {
		return new CreateLogEntryAction();
	}

	private void onCreateLogEntry() {
		LogMessageCreateParams params = new LogMessageCreateParams();
		params.category = model.categoryModel.getString();
		params.message = model.messageModel.getString();
		params.project = model.projectModel.getString();
		params.town = model.townModel.getString();
		params.username = model.usernameModel.getString();

		try {
			new LogMessageCreateInteractor().createMessage(params);
			model.resultModel.setString(textResource.getString("logMessageCreateView_logEntryAdded"));
		} catch (Exception e) {
			model.resultModel.setString(textResource.getString("logMessageCreateView_logEntryFailed", e.getLocalizedMessage()));
			LoggerFactory.getLogger(LogMessageCreateController.class).warn("Failed to log message", e);
		}
	}

	private class CreateLogEntryAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			onCreateLogEntry();
		}
	}
}