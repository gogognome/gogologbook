package nl.gogognome.gogologbook.dbinmemory;

import static org.junit.Assert.*;

import java.util.List;

import nl.gogognome.gogologbook.dao.DAOException;
import nl.gogognome.gogologbook.entities.Project;

import org.junit.Test;

public class InMemoryProjectDAOTest {

	private final InMemoryProjectDAO projectDao = new InMemoryProjectDAO();

	@Test
	public void shouldFindCreatedProjects() {
		Project project = new Project();
		project.projectNr = "AB123";
		project.customer = "Me 'n you";
		project.street = "Sesamestreet";
		project.town = "Hilversum";
		projectDao.createProject(project);

		List<Project> foundProjects = projectDao.findAllProjects();

		assertEquals(1, foundProjects.size());
		Project foundProject = foundProjects.get(0);
		assertNotSame(project, foundProject);
		assertEquals(project.projectNr, foundProject.projectNr);
		assertEquals(project.customer, foundProject.customer);
		assertEquals(project.street, foundProject.street);
		assertEquals(project.town, foundProject.town);
	}

	@Test
	public void createTwoProjectsShouldGenerateIncreasingIds() {
		Project project1 = new Project();
		project1.projectNr = "test1";
		projectDao.createProject(project1);

		Project project2 = new Project();
		project2.projectNr = "test2";
		projectDao.createProject(project2);

		List<Project> foundMessages = projectDao.findAllProjects();

		assertEquals(2, foundMessages.size());
		Project foundproject1 = foundMessages.get(0);
		int id1 = foundproject1.id;

		Project foundproject2 = foundMessages.get(1);
		assertEquals(id1 + 1, foundproject2.id);
	}

	@Test
	public void createTwoProjectsShouldBeFoundInOrderOfCreation() {
		Project project1 = new Project();
		project1.projectNr = "AB1234";
		projectDao.createProject(project1);

		Project project2 = new Project();
		project2.projectNr = "AB4433";
		projectDao.createProject(project2);

		List<Project> foundProjects = projectDao.findAllProjects();

		assertEquals(2, foundProjects.size());
		Project foundproject1 = foundProjects.get(0);
		assertEquals(project1.projectNr, foundproject1.projectNr);

		Project foundproject2 = foundProjects.get(1);
		assertEquals(project2.projectNr, foundproject2.projectNr);
	}

	@Test(expected = DAOException.class)
	public void updateNonExistingProjectShouldThrowException() {
		Project project = new Project();
		project.projectNr = "AB1234";
		projectDao.updateProject(project);
	}

	@Test
	public void shouldUpdateExistingProject() {
		Project project = new Project();
		project.projectNr = "AB1234";
		project = projectDao.createProject(project);

		Project newProject = new Project(project.id);
		newProject.projectNr = "CD5678";
		projectDao.updateProject(newProject);

		List<Project> foundProjects = projectDao.findAllProjects();
		assertEquals(1, foundProjects.size());
		assertEquals(newProject.projectNr, foundProjects.get(0).projectNr);
	}

	@Test
	public void shouldDeleteExistingProject() {
		Project project = new Project();
		project.projectNr = "test1";
		projectDao.createProject(project);

		List<Project> foundProjects = projectDao.findAllProjects();
		projectDao.deleteProject(foundProjects.get(0).id);

		foundProjects = projectDao.findAllProjects();
		assertTrue(foundProjects.isEmpty());
	}

	@Test(expected = DAOException.class)
	public void shouldThrowExceptionWhenDeletingNonExistingProject() {
		projectDao.deleteProject(123);
	}
}
