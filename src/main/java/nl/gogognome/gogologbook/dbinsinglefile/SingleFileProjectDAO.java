package nl.gogognome.gogologbook.dbinsinglefile;

import java.util.List;

import nl.gogognome.gogologbook.dao.ProjectDAO;
import nl.gogognome.gogologbook.dbinmemory.InMemoryProjectDAO;
import nl.gogognome.gogologbook.entities.Project;

public class SingleFileProjectDAO implements ProjectDAO, SingleFileDatabaseDAO {

	private static final String TABLE_NAME = "Project";

	private InMemoryProjectDAO inMemoryProjectDao = new InMemoryProjectDAO();
	private final SingleFileDatabase singleFileDatabase;

	public SingleFileProjectDAO(SingleFileDatabase singleFileDatabase) {
		this.singleFileDatabase = singleFileDatabase;
		singleFileDatabase.registerDao(TABLE_NAME, this);
	}

	@Override
	public Project createProject(Project project) {
		try {
			singleFileDatabase.acquireLock();
			singleFileDatabase.initInMemDatabaseFromFile();
			project = inMemoryProjectDao.createProject(project);
			singleFileDatabase.appendInsertToFile(TABLE_NAME, project);
		} finally {
			singleFileDatabase.releaseLock();
		}
		return project;
	}

	@Override
	public void updateProject(Project project) {
		try {
			singleFileDatabase.acquireLock();
			singleFileDatabase.initInMemDatabaseFromFile();
			inMemoryProjectDao.updateProject(project);
			singleFileDatabase.appendUpdateToFile(TABLE_NAME, project);
		} finally {
			singleFileDatabase.releaseLock();
		}
	}

	@Override
	public List<Project> findAllProjects() {
		try {
			singleFileDatabase.acquireLock();
			singleFileDatabase.initInMemDatabaseFromFile();
		} finally {
			singleFileDatabase.releaseLock();
		}
		return inMemoryProjectDao.findAllProjects();
	}

	@Override
	public void deleteProject(int projectId) {
		try {
			singleFileDatabase.acquireLock();
			singleFileDatabase.initInMemDatabaseFromFile();
			inMemoryProjectDao.deleteProject(projectId);
			singleFileDatabase.appendDeleteToFile(TABLE_NAME, projectId);
		} finally {
			singleFileDatabase.releaseLock();
		}
	}

	@Override
	public void removeAllRecordsFromInMemoryDatabase() {
		inMemoryProjectDao = new InMemoryProjectDAO();
	}

	@Override
	public void createRecordInMemoryDatabase(Object record) {
		Project project = (Project) record;
		inMemoryProjectDao.createProject(project);
	}

	@Override
	public void updateRecordInMemoryDatabase(Object record) {
		Project project = (Project) record;
		inMemoryProjectDao.updateProject(project);
	}

	@Override
	public void deleteRecordFromInMemoryDatabase(int projectId) {
		inMemoryProjectDao.deleteProject(projectId);
	}

	@Override
	public Class<?> getRecordClass() {
		return Project.class;
	}

}
