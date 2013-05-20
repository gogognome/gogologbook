package nl.gogognome.gogologbook.interactors;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nl.gogognome.gogologbook.dao.ProjectDAO;
import nl.gogognome.gogologbook.entities.Project;
import nl.gogognome.gogologbook.interactors.boundary.ProjectCreateParams;
import nl.gogognome.gogologbook.interactors.boundary.ProjectFindResult;
import nl.gogognome.gogologbook.util.DaoFactory;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class ProjectInteractor {

	public void createProject(ProjectCreateParams params) {
		Project project = new Project();
		project.customer = params.customer;
		project.projectNr = params.projectNr;
		project.street = params.street;
		project.town = params.town;

		DaoFactory.getInstance(ProjectDAO.class).createProject(project);
	}

	public List<ProjectFindResult> findAllProjects() {
		List<Project> projects = DaoFactory.getInstance(ProjectDAO.class).findAllProjects();

		List<ProjectFindResult> results = Lists.newArrayList(Iterables.transform(projects, new ProjectToProjectFindResult()));
		Collections.sort(results, new ProjectNrComparator());

		return results;
	}

}

class ProjectToProjectFindResult implements Function<Project, ProjectFindResult> {

	@Override
	public ProjectFindResult apply(Project project) {
		ProjectFindResult result = new ProjectFindResult();
		result.id = project.id;
		result.customer = project.customer;
		result.projectNr = project.projectNr;
		result.street = project.street;
		result.town = project.town;
		return result;
	}
}

class ProjectNrComparator implements Comparator<ProjectFindResult> {

	@Override
	public int compare(ProjectFindResult project1, ProjectFindResult project2) {
		return project1.projectNr.compareToIgnoreCase(project2.projectNr);
	}

}