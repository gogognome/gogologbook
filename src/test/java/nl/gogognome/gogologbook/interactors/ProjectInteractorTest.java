package nl.gogognome.gogologbook.interactors;

import static com.google.common.collect.Lists.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import nl.gogognome.gogologbook.dao.ProjectDAO;
import nl.gogognome.gogologbook.entities.Project;
import nl.gogognome.gogologbook.interactors.boundary.ProjectFindResult;
import nl.gogognome.gogologbook.util.DaoFactory;
import nl.gogognome.gogologbook.utils.UnitTest;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class ProjectInteractorTest extends UnitTest {

	private final ProjectDAO projectDao = mockAndRegisterDAO(ProjectDAO.class);
	private final ProjectInteractor projectInteractor = new ProjectInteractor();

	@Before
	public void registerMocks() {
		DaoFactory.register(ProjectDAO.class, projectDao);
	}

	@Test
	public void shouldUseDaoToFindAllProjects() {
		List<Project> projects = newArrayList();
		when(projectDao.findAllProjects()).thenReturn(projects);

		projectInteractor.findAllProjects();

		verify(projectDao).findAllProjects();
	}

	@Test
	public void shouldUseDaoToDeleteProject() {
		projectInteractor.deleteProject(123);

		verify(projectDao).deleteProject(eq(123));
	}

	@Test
	public void shouldSortProjectNumbersLexicographically() {
		Project projectA = createProject(1, "AB123", "Alice");
		Project projectB = createProject(2, "AB234", "bob");
		Project projectC = createProject(3, "CD001", "Charlie");

		List<Project> projects = Lists.newArrayList(projectC, projectB, projectA);
		when(projectDao.findAllProjects()).thenReturn(projects);

		List<ProjectFindResult> foundProjects = projectInteractor.findAllProjects();

		assertEquals(projectA.id, foundProjects.get(0).id);
		assertEquals(projectB.id, foundProjects.get(1).id);
		assertEquals(projectC.id, foundProjects.get(2).id);
	}

	private Project createProject(int id, String projectNr, String customer) {
		Project project = new Project(id);
		project.projectNr = projectNr;
		project.customer = customer;
		return project;
	}

}
