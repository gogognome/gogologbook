package nl.gogognome.gogologbook.interactors;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nl.gogognome.gogologbook.dao.ProjectDAO;
import nl.gogognome.gogologbook.entities.Project;
import nl.gogognome.gogologbook.util.DaoFactory;

public class ProjectInteractor {

	public List<Project> findAllProjects() {
		List<Project> projects = DaoFactory.getInstance(ProjectDAO.class).findAllProjects();
		Collections.sort(projects, new ProjectNrComparator());
		return projects;
	}

}

class ProjectNrComparator implements Comparator<Project> {

	@Override
	public int compare(Project project1, Project project2) {
		return project1.projectNr.compareToIgnoreCase(project2.projectNr);
	}

}