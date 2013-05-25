package nl.gogognome.gogologbook.dao;

import java.util.List;

import nl.gogognome.gogologbook.entities.Project;

public interface ProjectDAO {

	Project createProject(Project project);

	void updateProject(Project project);

	List<Project> findAllProjects();

	void deleteProject(int projectId);

}
