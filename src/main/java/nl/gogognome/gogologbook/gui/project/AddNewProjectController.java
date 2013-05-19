package nl.gogognome.gogologbook.gui.project;

public class AddNewProjectController extends AbstractEditProjectController {

	public AddNewProjectController() {
		model = new AddNewProjectModel();
	}

	@Override
	public void save() {
		closeAction.actionPerformed(null);
	}
}
