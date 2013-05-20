package nl.gogognome.gogologbook.dao;

import java.util.List;

import nl.gogognome.gogologbook.entities.Project;

public interface ProjectDAO {

	public Project createProject(Project project);

	public List<Project> findAllProjects();

	public void deleteProject(int projectId);
}
