package nl.gogognome.gogologbook.gui.project;

import nl.gogognome.gogologbook.gui.session.SessionManager;
import nl.gogognome.gogologbook.interactors.ProjectInteractor;
import nl.gogognome.gogologbook.interactors.boundary.InteractorFactory;
import nl.gogognome.gogologbook.interactors.boundary.ProjectFindResult;
import nl.gogognome.gogologbook.interactors.boundary.ProjectUpdateParams;
import nl.gogognome.lib.swing.MessageDialog;

import org.slf4j.LoggerFactory;

public class EditProjectController extends AbstractEditProjectController {

	private final ProjectInteractor projectInteractor = InteractorFactory.getInteractor(ProjectInteractor.class);

	public EditProjectController(ProjectFindResult project) {
		model = new EditProjectModel();

		((EditProjectModel) model).projectUnderEdit = project;
		model.customerModel.setString(project.customer);
		model.projectNumberModel.setString(project.projectNr);
		model.streetModel.setString(project.street);
		model.townModel.setString(project.town);
	}

	@Override
	public void save() {
		ProjectUpdateParams params = new ProjectUpdateParams();
		params.id = ((EditProjectModel) model).projectUnderEdit.id;
		params.customer = model.customerModel.getString();
		params.projectNr = model.projectNumberModel.getString();
		params.street = model.streetModel.getString();
		params.town = model.townModel.getString();

		try {
			projectInteractor.updateProject(params);
			closeAction.actionPerformed(null);
			SessionManager.getInstance().notifyListeners(new ProjectChangedEvent());
		} catch (Exception e) {
			LoggerFactory.getLogger(getClass()).warn("Failed to update project " + params.id, e);
			MessageDialog.showErrorMessage(model.parent, "editProject_failedToUpdateProject", e.getMessage());
		}
	}

}
