package nl.gogognome.gogologbook.gui.logmessage;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

import nl.gogognome.gogologbook.entities.Category;
import nl.gogognome.gogologbook.entities.User;
import nl.gogognome.gogologbook.gui.session.SessionManager;
import nl.gogognome.gogologbook.interactors.CategoryInteractor;
import nl.gogognome.gogologbook.interactors.LogMessageInteractor;
import nl.gogognome.gogologbook.interactors.ProjectInteractor;
import nl.gogognome.gogologbook.interactors.UserInteractor;
import nl.gogognome.gogologbook.interactors.boundary.InteractorFactory;
import nl.gogognome.gogologbook.interactors.boundary.LogMessageFindResult;
import nl.gogognome.gogologbook.interactors.boundary.LogMessageUpdateParams;
import nl.gogognome.gogologbook.interactors.boundary.ProjectFindResult;
import nl.gogognome.lib.swing.MessageDialog;
import nl.gogognome.lib.util.StringUtil;

import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;

public class LogMessageEditController {

	private final Component parentComponent;
	private final LogMessageEditModel model = new LogMessageEditModel();
	private final LogMessageInteractor logMessageInteractor = InteractorFactory.getInteractor(LogMessageInteractor.class);
	private final UserInteractor userInteractor = InteractorFactory.getInteractor(UserInteractor.class);
	private final ProjectInteractor projectInteractor = InteractorFactory.getInteractor(ProjectInteractor.class);
	private final CategoryInteractor categoryInteractor = InteractorFactory.getInteractor(CategoryInteractor.class);
	private Action closeAction;

	public LogMessageEditController(Component parentComponent) {
		this.parentComponent = parentComponent;

		model.usersModel.setItems(userInteractor.findAllUsers());
		model.projectsModel.setItems(projectInteractor.findAllProjects());
		model.categoriesModel.setItems(categoryInteractor.findAllCategories());
	}

	public LogMessageEditModel getModel() {
		return model;
	}

	public void setLogMessageUnderEdit(LogMessageFindResult logMessageUnderEdit) {
		model.logMessageUnderEdit = logMessageUnderEdit;

		model.timestampModel.setDate(logMessageUnderEdit.timestamp);

		List<User> users = model.usersModel.getItems();
		for (int i = 0; i < users.size(); i++) {
			if (Objects.equal(users.get(i).name, logMessageUnderEdit.username)) {
				model.usersModel.setSelectedIndex(i, null);
				break;
			}
		}

		List<ProjectFindResult> projects = model.projectsModel.getItems();
		for (int i = 0; i < projects.size(); i++) {
			if (Objects.equal(projects.get(i).projectNr, logMessageUnderEdit.projectNr)) {
				model.projectsModel.setSelectedIndex(i, null);
				break;
			}
		}

		List<Category> categories = model.categoriesModel.getItems();
		for (int i = 0; i < categories.size(); i++) {
			if (Objects.equal(categories.get(i).name, logMessageUnderEdit.category)) {
				model.categoriesModel.setSelectedIndex(i, null);
				break;
			}
		}

		model.messageModel.setString(logMessageUnderEdit.message);
	}

	public void setCloseAction(Action closeAction) {
		this.closeAction = closeAction;
	}

	public Action getOkAction() {
		return new UpdateLogMessageAction();
	}

	public void updateLogMessage() {
		if (!validateInput()) {
			return;
		}
		LogMessageUpdateParams params = new LogMessageUpdateParams();
		params.id = model.logMessageUnderEdit.id;
		params.category = model.categoriesModel.getSelectedItem().name;
		params.message = StringUtil.nullToEmptyString(model.messageModel.getString()).trim();
		params.projectId = model.projectsModel.getSelectedItem().id;
		params.timestamp = model.timestampModel.getDate();
		params.userId = model.usersModel.getSelectedItem().id;

		try {
			logMessageInteractor.updateMessage(params);
			closeAction.actionPerformed(null);
		} catch (Exception e) {
			LoggerFactory.getLogger(LogMessageCreateController.class).warn("Failed to log message", e);
			MessageDialog.showErrorMessage(parentComponent, "logMessageEditView_logEntryUpdateFailed", e.getLocalizedMessage());
		}

		SessionManager.getInstance().notifyListeners(new LogMessageUpdateEvent());
	}

	private boolean validateInput() {
		if (model.timestampModel.getDate() == null) {
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

	private class UpdateLogMessageAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			updateLogMessage();
		}
	}

}
