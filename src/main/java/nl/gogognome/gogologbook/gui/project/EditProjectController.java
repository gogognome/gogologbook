package nl.gogognome.gogologbook.gui.project;

import nl.gogognome.gogologbook.interactors.boundary.ProjectFindResult;

public class EditProjectController extends AbstractEditProjectController {

	private final EditProjectModel model = new EditProjectModel();

	public EditProjectController(ProjectFindResult project) {
		super.model = model;

		model.projectUnderEdit = project;
		model.customerModel.setString(project.customer);
		model.projectNumberModel.setString(project.projectNr);
		model.streetModel.setString(project.street);
		model.townModel.setString(project.town);
	}

	@Override
	public void save() {
		closeAction.actionPerformed(null);
	}

}
