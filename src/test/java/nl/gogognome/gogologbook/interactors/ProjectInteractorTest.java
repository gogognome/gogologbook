package nl.gogognome.gogologbook.interactors;

import static com.google.common.collect.Lists.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import nl.gogognome.gogologbook.dao.ProjectDAO;
import nl.gogognome.gogologbook.entities.Project;
import nl.gogognome.gogologbook.util.DaoFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class ProjectInteractorTest {

	private final ProjectInteractor projectInteractor = new ProjectInteractor();
	private final ProjectDAO projectDao = mock(ProjectDAO.class);

	@Before
	public void registerMocks() {
		DaoFactory.register(ProjectDAO.class, projectDao);
	}

	@After
	public void unregisterMocks() {
		DaoFactory.clear();
	}

	@Test
	public void shouldUseDaoToFindAllProjects() {
		List<Project> projects = newArrayList();
		when(projectDao.findAllProjects()).thenReturn(projects);

		projectInteractor.findAllProjects();

		verify(projectDao).findAllProjects();
	}

	@Test
	public void shouldSortProjectNumbersLexicographically() {
		Project projectA = createProject(1, "AB123", "Alice");
		Project projectB = createProject(2, "AB234", "bob");
		Project projectC = createProject(3, "CD001", "Charlie");

		List<Project> projects = Lists.newArrayList(projectC, projectB, projectA);
		when(projectDao.findAllProjects()).thenReturn(projects);

		List<Project> foundProjects = projectInteractor.findAllProjects();

		assertSame(projectA, foundProjects.get(0));
		assertSame(projectB, foundProjects.get(1));
		assertSame(projectC, foundProjects.get(2));
	}

	private Project createProject(int id, String projectNr, String customer) {
		Project project = new Project(id);
		project.projectNr = projectNr;
		project.customer = customer;
		return project;
	}

}
