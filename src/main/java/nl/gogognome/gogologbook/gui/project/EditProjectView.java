package nl.gogognome.gogologbook.gui.project;

import nl.gogognome.gogologbook.interactors.boundary.ProjectFindResult;

public class EditProjectView extends AbstractEditProjectView {

	private static final long serialVersionUID = 1L;

	public EditProjectView(ProjectFindResult project) {
		controller = new EditProjectController(project);
		model = controller.getModel();
	}

	@Override
	public String getTitle() {
		return textResource.getString("edit_project_title");
	}

}
