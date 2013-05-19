package nl.gogognome.gogologbook.gui.project;

public class AddNewProjectView extends AbstractEditProjectView {

	private static final long serialVersionUID = 1L;

	public AddNewProjectView() {
		controller = new AddNewProjectController();
		model = controller.getModel();
	}

	@Override
	public String getTitle() {
		return textResource.getString("add_project_title");
	}
}
