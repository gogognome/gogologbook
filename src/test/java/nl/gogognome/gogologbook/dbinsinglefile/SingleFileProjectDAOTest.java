package nl.gogognome.gogologbook.dbinsinglefile;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import nl.gogognome.gogologbook.dao.DAOException;
import nl.gogognome.gogologbook.entities.Project;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.Files;

public class SingleFileProjectDAOTest extends AbstractSingleFileDAOTest {

	private static final String INSERT_OF_ONE_PROJECT = "insert;Project;{\"projectNr\":\"AB123\",\"customer\":\"Me \\u0027n you\",\"street\":\"Sesamestreet\",\"town\":\"Hilversum\",\"id\":1}";
	private final SingleFileDatabase singleFileDatabase = new SingleFileDatabase(dbFile);
	private final SingleFileProjectDAO projectDao = new SingleFileProjectDAO(singleFileDatabase);

	@Test
	public void createRecordShouldWriteRecordInDbFile() throws IOException {
		Project project = new Project();
		project.projectNr = "AB123";
		project.customer = "Me 'n you";
		project.street = "Sesamestreet";
		project.town = "Hilversum";
		projectDao.createProject(project);

		assertEquals(INSERT_OF_ONE_PROJECT, getContentsOfDbFile());
	}

	@Test
	public void dbShouldContainTwoRecordsAfterTwoRecordsHaveBeenCreated() throws IOException {
		Project project = new Project();
		project.projectNr = "AB123";
		projectDao.createProject(project);

		project.projectNr = "AB543";
		projectDao.createProject(project);

		List<Project> projects = projectDao.findAllProjects();

		assertEquals(2, projects.size());
	}

	@Test
	public void absentDbFileLeadsToEmptyDatabase() {
		assertFalse(dbFile.exists());

		List<Project> projects = projectDao.findAllProjects();

		assertTrue(projects.isEmpty());
	}

	@Test
	public void dbShouldHaveOneProjectAfterReadingOneProjectFromFile() throws IOException {
		Files.write(INSERT_OF_ONE_PROJECT, dbFile, Charsets.ISO_8859_1);

		List<Project> projects = projectDao.findAllProjects();

		assertFalse(projects.isEmpty());
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

	private String getContentsOfDbFile() throws IOException {
		return Joiner.on("\n").join(Files.readLines(dbFile, Charsets.ISO_8859_1));
	}
}