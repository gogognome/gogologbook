package nl.gogognome.gogologbook.gui.project;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

import nl.gogognome.gogologbook.interactors.ProjectInteractor;
import nl.gogognome.gogologbook.interactors.boundary.InteractorFactory;
import nl.gogognome.gogologbook.interactors.boundary.ProjectFindResult;
import nl.gogognome.lib.swing.views.ViewDialog;

public class ProjectController {

	private final ProjectsModel model = new ProjectsModel();
	private final Component parent;
	private final ProjectInteractor projectInteractor = InteractorFactory.getInteractor(ProjectInteractor.class);

	public ProjectController(Component parent) {
		this.parent = parent;
		List<ProjectFindResult> projects = projectInteractor.findAllProjects();
		model.projectsTableModel.setProjects(projects);
	}

	public ProjectsModel getModel() {
		return model;
	}

	public Action getAddAction() {
		return new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onAddNewProject();
			}
		};
	}

	public Action getEditAction() {
		return new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onEditSelectedProject();
			}
		};
	}

	public Action getDeleteAction() {
		return new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onDeleteSelectedProject();
			}
		};
	}

	private void onAddNewProject() {
		AddNewProjectView view = new AddNewProjectView();
		new ViewDialog(parent, view).showDialog();
	}

	private void onEditSelectedProject() {
		EditProjectView view = new EditProjectView(null);
		new ViewDialog(parent, view).showDialog();
	}

	private void onDeleteSelectedProject() {
		// TODO Auto-generated method stub

	}
}
