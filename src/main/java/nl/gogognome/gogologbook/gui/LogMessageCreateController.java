package nl.gogognome.gogologbook.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import nl.gogognome.gogologbook.interactors.CategoryInteractor;
import nl.gogognome.gogologbook.interactors.LogMessageCreateInteractor;
import nl.gogognome.gogologbook.interactors.ProjectInteractor;
import nl.gogognome.gogologbook.interactors.UserInteractor;
import nl.gogognome.gogologbook.interactors.boundary.LogMessageCreateParams;
import nl.gogognome.lib.text.TextResource;
import nl.gogognome.lib.util.Factory;

import org.slf4j.LoggerFactory;

public class LogMessageCreateController {

	private final LogMessageCreateModel model = new LogMessageCreateModel();
	private final TextResource textResource = Factory.getInstance(TextResource.class);

	public LogMessageCreateController() {
		model.usersModel.setItems(new UserInteractor().findAllUsers());
		model.projectsModel.setItems(new ProjectInteractor().findAllProjects());
		model.categoriesModel.setItems(new CategoryInteractor().findAllCategories());
	}

	public LogMessageCreateModel getModel() {
		return model;
	}

	public Action getCreateAction() {
		return new CreateLogMessageAction();
	}

	public void createLogMessage() {
		LogMessageCreateParams params = new LogMessageCreateParams();
		params.category = model.categoriesModel.getSelectedItem().name;
		params.message = model.messageModel.getString();
		params.projectId = model.projectsModel.getSelectedItem().id;
		params.userId = model.usersModel.getSelectedItem().id;

		try {
			new LogMessageCreateInteractor().createMessage(params);
			model.resultModel.setString(textResource.getString("logMessageCreateView_logMessageAdded"));
		} catch (Exception e) {
			model.resultModel.setString(textResource.getString("logMessageCreateView_logMessageFailed", e.getLocalizedMessage()));
			LoggerFactory.getLogger(LogMessageCreateController.class).warn("Failed to log message", e);
		}
	}

	private class CreateLogMessageAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			createLogMessage();
		}
	}
}
