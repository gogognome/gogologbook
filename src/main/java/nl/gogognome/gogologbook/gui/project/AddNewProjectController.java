package nl.gogognome.gogologbook.gui.project;

import nl.gogognome.gogologbook.gui.session.SessionManager;
import nl.gogognome.gogologbook.interactors.ProjectInteractor;
import nl.gogognome.gogologbook.interactors.boundary.InteractorFactory;
import nl.gogognome.gogologbook.interactors.boundary.ProjectCreateParams;
import nl.gogognome.lib.swing.MessageDialog;

import org.slf4j.LoggerFactory;

public class AddNewProjectController extends AbstractEditProjectController {

	private final ProjectInteractor projectInteractor = InteractorFactory.getInteractor(ProjectInteractor.class);

	public AddNewProjectController() {
		model = new AddNewProjectModel();
	}

	@Override
	public void save() {
		ProjectCreateParams params = new ProjectCreateParams();
		params.customer = model.customerModel.getString();
		params.projectNr = model.projectNumberModel.getString();
		params.street = model.streetModel.getString();
		params.town = model.townModel.getString();

		try {
			projectInteractor.createProject(params);
			closeAction.actionPerformed(null);
			SessionManager.getInstance().notifyListeners(new ProjectChangedEvent());
		} catch (Exception e) {
			LoggerFactory.getLogger(getClass()).warn("Failed to create project", e);
			MessageDialog.showErrorMessage(model.parent, "editProject_failedToCreateProject", e.getMessage());
		}
	}
}
