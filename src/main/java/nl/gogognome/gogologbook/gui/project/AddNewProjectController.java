package nl.gogognome.gogologbook.gui.project;

import nl.gogognome.gogologbook.interactors.ProjectInteractor;
import nl.gogognome.gogologbook.interactors.boundary.ProjectCreateParams;
import nl.gogognome.lib.swing.MessageDialog;

public class AddNewProjectController extends AbstractEditProjectController {

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
			new ProjectInteractor().createProject(params);
		} catch (Exception e) {
			MessageDialog.showErrorMessage(model.parent, e, "");
		}
		closeAction.actionPerformed(null);
	}
}
