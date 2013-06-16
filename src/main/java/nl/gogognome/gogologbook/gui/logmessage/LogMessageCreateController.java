package nl.gogognome.gogologbook.gui.logmessage;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.Action;

import nl.gogognome.gogologbook.gui.project.ProjectChangedEvent;
import nl.gogognome.gogologbook.gui.session.SessionChangeEvent;
import nl.gogognome.gogologbook.gui.session.SessionListener;
import nl.gogognome.gogologbook.gui.session.SessionManager;
import nl.gogognome.gogologbook.interactors.CategoryInteractor;
import nl.gogognome.gogologbook.interactors.LogMessageInteractor;
import nl.gogognome.gogologbook.interactors.ProjectInteractor;
import nl.gogognome.gogologbook.interactors.UserInteractor;
import nl.gogognome.gogologbook.interactors.boundary.InteractorFactory;
import nl.gogognome.gogologbook.interactors.boundary.LogMessageCreateParams;
import nl.gogognome.lib.gui.Closeable;
import nl.gogognome.lib.swing.MessageDialog;
import nl.gogognome.lib.swing.models.AbstractModel;
import nl.gogognome.lib.swing.models.ModelChangeListener;
import nl.gogognome.lib.util.StringUtil;

import org.slf4j.LoggerFactory;

public class LogMessageCreateController implements Closeable, SessionListener, ModelChangeListener {

	private final Component parentComponent;
	private final LogMessageCreateModel model = new LogMessageCreateModel();
	private final LogMessageInteractor logMessageCreateInteractor = InteractorFactory.getInteractor(LogMessageInteractor.class);
	private final UserInteractor userInteractor = InteractorFactory.getInteractor(UserInteractor.class);
	private final ProjectInteractor projectInteractor = InteractorFactory.getInteractor(ProjectInteractor.class);
	private final CategoryInteractor categoryInteractor = InteractorFactory.getInteractor(CategoryInteractor.class);

	public LogMessageCreateController(Component parentComponent) {
		this.parentComponent = parentComponent;
		model.usersModel.setItems(userInteractor.findAllUsers());
		model.projectsModel.setItems(projectInteractor.findAllProjects());
		model.categoriesModel.setItems(categoryInteractor.findAllCategories());
		model.manuallySpecifyTimestamp.addModelChangeListener(this);
		model.manuallySpecifyTimestamp.setBoolean(false, this);
		model.manuallySpecifiedTimestamp.setDate(new Date(), this);
		enableOrDisableManuallySpecifiedTimestamp();
		SessionManager.getInstance().addSessionListener(this);
	}

	@Override
	public void close() {
		SessionManager.getInstance().removeSessionListener(this);
		model.manuallySpecifyTimestamp.removeModelChangeListener(this);
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
		params.timestamp = model.manuallySpecifyTimestamp.getBoolean() ? model.manuallySpecifiedTimestamp.getDate() : new Date();

		try {
			logMessageCreateInteractor.createMessage(params);
		} catch (Exception e) {
			LoggerFactory.getLogger(LogMessageCreateController.class).warn("Failed to log message", e);
			MessageDialog.showErrorMessage(parentComponent, "logMessageCreateView_logMessageFailed", e.getLocalizedMessage());
		}

		SessionManager.getInstance().notifyListeners(new LogMessageCreatedEvent());
	}

	private boolean validateInput() {
		if (model.manuallySpecifyTimestamp.getBoolean() && model.manuallySpecifiedTimestamp.getDate() == null) {
			MessageDialog.showWarningMessage(parentComponent, "logMessageCreateView_no_timestamp_entered");
			return false;
		}
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

	@Override
	public void modelChanged(AbstractModel changedModel) {
		enableOrDisableManuallySpecifiedTimestamp();
	}

	private void enableOrDisableManuallySpecifiedTimestamp() {
		model.manuallySpecifiedTimestamp.setEnabled(model.manuallySpecifyTimestamp.getBoolean(), this);
	}
}
