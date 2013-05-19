package nl.gogognome.gogologbook.gui;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

import nl.gogognome.gogologbook.interactors.ProjectInteractor;
import nl.gogognome.gogologbook.interactors.boundary.ProjectFindResult;

public class ProjectController {

	private final ProjectsModel model = new ProjectsModel();

	public ProjectController() {
		List<ProjectFindResult> projects = new ProjectInteractor().findAllProjects();
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
		// TODO Auto-generated method stub

	}

	private void onDeleteSelectedProject() {
		// TODO Auto-generated method stub

	}

	private void onEditSelectedProject() {
		// TODO Auto-generated method stub

	}

}
