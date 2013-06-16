package nl.gogognome.gogologbook.gui.project;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ListSelectionModel;

import nl.gogognome.gogologbook.gui.session.SessionChangeEvent;
import nl.gogognome.gogologbook.gui.session.SessionListener;
import nl.gogognome.gogologbook.gui.session.SessionManager;
import nl.gogognome.gogologbook.interactors.CannotDeleteProjectThatIsInUseException;
import nl.gogognome.gogologbook.interactors.ProjectInteractor;
import nl.gogognome.gogologbook.interactors.boundary.InteractorFactory;
import nl.gogognome.gogologbook.interactors.boundary.ProjectFindResult;
import nl.gogognome.lib.gui.Closeable;
import nl.gogognome.lib.swing.MessageDialog;
import nl.gogognome.lib.swing.views.ViewDialog;

public class ProjectsController implements Closeable, SessionListener {

	private final ProjectsModel model = new ProjectsModel();
	private final Component parent;
	private final ProjectInteractor projectInteractor = InteractorFactory.getInteractor(ProjectInteractor.class);

	public ProjectsController(Component parent) {
		this.parent = parent;
		refreshProjects();
		model.selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		SessionManager.getInstance().addSessionListener(this);
	}

	public ProjectsModel getModel() {
		return model;
	}

	@Override
	public void close() {
		SessionManager.getInstance().removeSessionListener(this);
	}

	@Override
	public void sessionChanged(SessionChangeEvent event) {
		if (event instanceof ProjectChangedEvent || event instanceof ProjectDeletedEvent) {
			refreshProjects();
		}
	}

	private void refreshProjects() {
		List<ProjectFindResult> projects = projectInteractor.findAllProjects();
		model.projectsTableModel.setProjects(projects);
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
			MessageDialog.showInfoMessage(parent, "editProject_selectRowFirst");
			return;
		}

		ProjectFindResult project = model.projectsTableModel.getRow(index);
		EditProjectView view = new EditProjectView(project);
		new ViewDialog(parent, view).showDialog();
	}

	private void onDeleteSelectedProject() {
		int index = model.selectionModel.getMaxSelectionIndex();
		if (index == -1) {
			MessageDialog.showInfoMessage(parent, "editProject_selectRowFirst");
			return;
		}

		ProjectFindResult project = model.projectsTableModel.getRow(index);
		int choice = MessageDialog.showYesNoQuestion(parent, "gen.confirmation", "editProject_confirm_delete_project", project.projectNr);
		if (choice == MessageDialog.YES_OPTION) {
			try {
				projectInteractor.deleteProject(project.id);
				SessionManager.getInstance().notifyListeners(new ProjectDeletedEvent(project.id));
			} catch (CannotDeleteProjectThatIsInUseException e) {
				MessageDialog.showWarningMessage(parent, "editProject_cannotDeleteProjectBecauseItIsUsed", project.projectNr);
			}
		}

	}
}
