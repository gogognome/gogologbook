package nl.gogognome.gogologbook.dbinsinglefile;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import nl.gogognome.gogologbook.entities.Project;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.Files;

public class SingleFileProjectDAOTest extends AbstractSingleFileDAOTest {

	private static final String INSERT_OF_ONE_PROJECT = "insert;Project;{\"projectNr\":\"AB123\",\"street\":\"Sesamestreet\",\"town\":\"Hilversum\",\"id\":1}";
	private final SingleFileDatabase singleFileDatabase = new SingleFileDatabase(dbFile);
	private final SingleFileProjectDAO projectDao = new SingleFileProjectDAO(singleFileDatabase);

	@Test
	public void createRecordShouldWriteRecordInDbFile() throws IOException {
		Project message = new Project();
		message.projectNr = "AB123";
		message.street = "Sesamestreet";
		message.town = "Hilversum";
		projectDao.createProject(message);

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

	private String getContentsOfDbFile() throws IOException {
		return Joiner.on("\n").join(Files.readLines(dbFile, Charsets.ISO_8859_1));
	}
}