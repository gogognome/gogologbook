package nl.gogognome.gogologbook.interactors;

import static com.google.common.collect.Lists.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import nl.gogognome.gogologbook.dao.LogMessageDAO;
import nl.gogognome.gogologbook.dao.ProjectDAO;
import nl.gogognome.gogologbook.entities.Project;
import nl.gogognome.gogologbook.interactors.boundary.CannotDeleteProjectThatIsInUseException;
import nl.gogognome.gogologbook.interactors.boundary.ProjectCreateParams;
import nl.gogognome.gogologbook.interactors.boundary.ProjectFindResult;
import nl.gogognome.gogologbook.interactors.boundary.ProjectUpdateParams;
import nl.gogognome.gogologbook.util.DaoFactory;
import nl.gogognome.gogologbook.utils.UnitTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.google.common.collect.Lists;

public class ProjectInteractorTest extends UnitTest {

	private final LogMessageDAO logMessageDao = mockAndRegisterDAO(LogMessageDAO.class);
	private final ProjectDAO projectDao = mockAndRegisterDAO(ProjectDAO.class);
	private final ProjectInteractor projectInteractor = new ProjectInteractor();

	@Before
	public void registerMocks() {
		DaoFactory.register(ProjectDAO.class, projectDao);
	}

	@Test
	public void shouldUseDaoToCreateProject() {
		ProjectCreateParams params = new ProjectCreateParams();

		projectInteractor.createProject(params);

		verify(projectDao).createProject(any(Project.class));
	}

	@Test
	public void shouldConvertCreateParamsToProject() {
		ArgumentCaptor<Project> projectCaptor = ArgumentCaptor.forClass(Project.class);
		ProjectCreateParams params = new ProjectCreateParams();
		params.customer = "Custoemr";
		params.projectNr = "Projectnr";
		params.street = "Street";
		params.town = "Town";

		projectInteractor.createProject(params);

		verify(projectDao).createProject(projectCaptor.capture());
		Project project = projectCaptor.getValue();
		assertEquals(params.customer, project.customer);
		assertEquals(params.projectNr, project.projectNr);
		assertEquals(params.street, project.street);
		assertEquals(params.town, project.town);
	}

	@Test
	public void shouldUseDaoToUpdateProject() {
		ProjectUpdateParams params = new ProjectUpdateParams();

		projectInteractor.updateProject(params);

		verify(projectDao).updateProject(any(Project.class));
	}

	@Test
	public void shouldConvertUpdateParamsToProject() {
		ArgumentCaptor<Project> projectCaptor = ArgumentCaptor.forClass(Project.class);
		ProjectUpdateParams params = new ProjectUpdateParams();
		params.id = 123;
		params.customer = "Custoemr";
		params.projectNr = "Projectnr";
		params.street = "Street";
		params.town = "Town";

		projectInteractor.updateProject(params);

		verify(projectDao).updateProject(projectCaptor.capture());
		Project project = projectCaptor.getValue();
		assertEquals(params.id, project.id);
		assertEquals(params.customer, project.customer);
		assertEquals(params.projectNr, project.projectNr);
		assertEquals(params.street, project.street);
		assertEquals(params.town, project.town);
	}

	@Test
	public void shouldUseDaoToFindAllProjects() {
		List<Project> projects = newArrayList();
		when(projectDao.findAllProjects()).thenReturn(projects);

		projectInteractor.findAllProjects();

		verify(projectDao).findAllProjects();
	}

	@Test
	public void shouldUseDaoToDeleteProject() throws Exception {
		when(logMessageDao.isProjectUsed(anyInt())).thenReturn(false);
		projectInteractor.deleteProject(123);

		verify(projectDao).deleteProject(eq(123));
	}

	@Test(expected = CannotDeleteProjectThatIsInUseException.class)
	public void shouldThrowExceptionWhenProjectIsDeletedThasIsInUse() throws Exception {
		when(logMessageDao.isProjectUsed(anyInt())).thenReturn(true);
		projectInteractor.deleteProject(123);
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
