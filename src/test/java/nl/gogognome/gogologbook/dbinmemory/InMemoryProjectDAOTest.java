package nl.gogognome.gogologbook.dbinmemory;

import static org.junit.Assert.*;

import java.util.List;

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

		List<Project> foundMessages = projectDao.findAllProjects();

		assertEquals(2, foundMessages.size());
		Project foundproject1 = foundMessages.get(0);
		assertEquals(project1.projectNr, foundproject1.projectNr);

		Project foundproject2 = foundMessages.get(1);
		assertEquals(project2.projectNr, foundproject2.projectNr);
	}

}
