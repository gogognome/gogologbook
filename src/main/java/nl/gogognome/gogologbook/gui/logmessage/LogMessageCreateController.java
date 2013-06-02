package nl.gogognome.gogologbook.gui.logmessage;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import nl.gogognome.gogologbook.gui.project.ProjectChangedEvent;
import nl.gogognome.gogologbook.gui.session.SessionChangeEvent;
import nl.gogognome.gogologbook.gui.session.SessionListener;
import nl.gogognome.gogologbook.gui.session.SessionManager;
import nl.gogognome.gogologbook.interactors.CategoryInteractor;
import nl.gogognome.gogologbook.interactors.LogMessageCreateInteractor;
import nl.gogognome.gogologbook.interactors.ProjectInteractor;
import nl.gogognome.gogologbook.interactors.UserInteractor;
import nl.gogognome.gogologbook.interactors.boundary.InteractorFactory;
import nl.gogognome.gogologbook.interactors.boundary.LogMessageCreateParams;
import nl.gogognome.lib.gui.Closeable;
import nl.gogognome.lib.swing.MessageDialog;
import nl.gogognome.lib.text.TextResource;
import nl.gogognome.lib.util.Factory;
import nl.gogognome.lib.util.StringUtil;

import org.slf4j.LoggerFactory;

public class LogMessageCreateController implements Closeable, SessionListener {

	private final Component parentComponent;
	private final LogMessageCreateModel model = new LogMessageCreateModel();
	private final TextResource textResource = Factory.getInstance(TextResource.class);
	private final LogMessageCreateInteractor logMessageCreateInteractor = InteractorFactory.getInteractor(LogMessageCreateInteractor.class);
	private final UserInteractor userInteractor = InteractorFactory.getInteractor(UserInteractor.class);
	private final ProjectInteractor projectInteractor = InteractorFactory.getInteractor(ProjectInteractor.class);
	private final CategoryInteractor categoryInteractor = InteractorFactory.getInteractor(CategoryInteractor.class);

	public LogMessageCreateController(Component parentComponent) {
		this.parentComponent = parentComponent;
		model.usersModel.setItems(userInteractor.findAllUsers());
		model.projectsModel.setItems(projectInteractor.findAllProjects());
		model.categoriesModel.setItems(categoryInteractor.findAllCategories());
		SessionManager.getInstance().addSessionListener(this);
	}

	@Override
	public void close() {
		SessionManager.getInstance().removeSessionListener(this);
	}

	public LogMessageCreateModel getModel() {
		return model;
	}

	public Action getCreateAction() {
		return new CreateLogMessageAction();
	}

	public void createLogMessage() {
		if (!validateInput()) {
			return;
		}
		LogMessageCreateParams params = new LogMessageCreateParams();
		params.category = model.categoriesModel.getSelectedItem().name;
		params.message = StringUtil.nullToEmptyString(model.messageModel.getString()).trim();
		params.projectId = model.projectsModel.getSelectedItem().id;
		params.userId = model.usersModel.getSelectedItem().id;

		try {
			logMessageCreateInteractor.createMessage(params);
		} catch (Exception e) {
			LoggerFactory.getLogger(LogMessageCreateController.class).warn("Failed to log message", e);
			MessageDialog.showErrorMessage(parentComponent, "logMessageCreateView_logMessageFailed", e.getLocalizedMessage());
		}

		SessionManager.getInstance().notifyListeners(new LogMessageCreatedEvent());
	}

	private boolean validateInput() {
		if (model.usersModel.getSelectedItem() == null) {
			MessageDialog.showWarningMessage(parentComponent, "logMessageCreateView_no_user_selected");
			return false;
		}
		if (model.projectsModel.getSelectedItem() == null) {
			MessageDialog.showWarningMessage(parentComponent, "logMessageCreateView_no_project_selected");
			return false;
		}
		if (model.categoriesModel.getSelectedItem() == null) {
			MessageDialog.showWarningMessage(parentComponent, "logMessageCreateView_no_category_selected");
			return false;
		}
		if (StringUtil.isNullOrEmpty(model.messageModel.getString())) {
			MessageDialog.showWarningMessage(parentComponent, "logMessageCreateView_no_message_entered");
			return false;
		}
		return true;
	}

	@Override
	public void sessionChanged(SessionChangeEvent event) {
		if (event instanceof ProjectChangedEvent) {
			model.projectsModel.setItems(projectInteractor.findAllProjects());
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
