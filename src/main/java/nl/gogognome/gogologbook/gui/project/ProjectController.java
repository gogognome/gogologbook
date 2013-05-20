package nl.gogognome.gogologbook.gui.project;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ListSelectionModel;

import nl.gogognome.gogologbook.interactors.ProjectInteractor;
import nl.gogognome.gogologbook.interactors.boundary.InteractorFactory;
import nl.gogognome.gogologbook.interactors.boundary.ProjectFindResult;
import nl.gogognome.lib.swing.MessageDialog;
import nl.gogognome.lib.swing.views.ViewDialog;

public class ProjectController {

	private final ProjectsModel model = new ProjectsModel();
	private final Component parent;
	private final ProjectInteractor projectInteractor = InteractorFactory.getInteractor(ProjectInteractor.class);

	public ProjectController(Component parent) {
		this.parent = parent;
		List<ProjectFindResult> projects = projectInteractor.findAllProjects();
		model.projectsTableModel.setProjects(projects);
		model.selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
		int index = model.selectionModel.getMaxSelectionIndex();
		if (index == -1) {
			MessageDialog.showInfoMessage(parent, "editProjects_selectRowFirst");
			return;
		}

		ProjectFindResult project = model.projectsTableModel.getRow(index);
		EditProjectView view = new EditProjectView(project);
		new ViewDialog(parent, view).showDialog();
	}

	private void onDeleteSelectedProject() {
		int index = model.selectionModel.getMaxSelectionIndex();
		if (index == -1) {
			MessageDialog.showInfoMessage(parent, "editProjects_selectRowFirst");
			return;
		}

		ProjectFindResult project = model.projectsTableModel.getRow(index);
		int choice = MessageDialog.showYesNoQuestion(parent, "gen.confirmation", "editProjects_confirm_delete_project", project.projectNr);
		if (choice == MessageDialog.YES_OPTION) {
			projectInteractor.deleteProject(project.id);
		}

	}
}
