package nl.gogognome.gogologbook.gui.project;

import nl.gogognome.gogologbook.gui.session.SessionChangeEvent;

public class ProjectDeletedEvent extends SessionChangeEvent {

	private final int projectId;

	public ProjectDeletedEvent(int projectId) {
		this.projectId = projectId;
	}

	public int getProjectId() {
		return projectId;
	}

}
