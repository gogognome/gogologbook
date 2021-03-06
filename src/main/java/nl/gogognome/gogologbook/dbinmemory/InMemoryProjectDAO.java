package nl.gogognome.gogologbook.dbinmemory;

import java.util.List;
import java.util.Map;

import nl.gogognome.gogologbook.dao.DAOException;
import nl.gogognome.gogologbook.dao.ProjectDAO;
import nl.gogognome.gogologbook.entities.Project;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class InMemoryProjectDAO implements ProjectDAO {

	private final Map<Integer, Project> idToProject = Maps.newTreeMap();

	@Override
	public Project createProject(Project project) {
		int maxId = 0;
		for (int id : idToProject.keySet()) {
			maxId = Math.max(maxId, id);
		}

		Project storedProject = cloneProject(project, maxId + 1);
		idToProject.put(storedProject.id, storedProject);
		return cloneProject(storedProject, storedProject.id);
	}

	@Override
	public void updateProject(Project project) {
		if (!idToProject.containsKey(project.id)) {
			throw new DAOException("Project with " + project.id + " does not exist. It cannot be updated.");
		}

		Project storedProject = cloneProject(project, project.id);
		idToProject.put(storedProject.id, storedProject);
	}

	@Override
	public List<Project> findAllProjects() {
		List<Project> results = Lists.newArrayListWithExpectedSize(idToProject.size());
		for (Project project : idToProject.values()) {
			results.add(cloneProject(project, project.id));
		}
		return results;
	}

	private Project cloneProject(Project origProject, int clonedId) {
		Project clonedProject = new Project(clonedId);
		clonedProject.projectNr = origProject.projectNr;
		clonedProject.customer = origProject.customer;
		clonedProject.street = origProject.street;
		clonedProject.town = origProject.town;
		return clonedProject;
	}

	@Override
	public void deleteProject(int projectId) {
		if (!idToProject.containsKey(projectId)) {
			throw new DAOException("Trying to delete non-existing project " + projectId);
		}
		idToProject.remove(projectId);
	}
}
