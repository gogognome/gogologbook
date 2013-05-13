package nl.gogognome.gogologbook.interactors;

import java.util.List;
import java.util.Map;

import nl.gogognome.gogologbook.dao.LogMessageDAO;
import nl.gogognome.gogologbook.dao.ProjectDAO;
import nl.gogognome.gogologbook.dao.UserDAO;
import nl.gogognome.gogologbook.entities.FilterCriteria;
import nl.gogognome.gogologbook.entities.LogMessage;
import nl.gogognome.gogologbook.entities.Project;
import nl.gogognome.gogologbook.entities.User;
import nl.gogognome.gogologbook.interactors.boundary.LogMessageFindParams;
import nl.gogognome.gogologbook.interactors.boundary.LogMessageFindResult;
import nl.gogognome.gogologbook.util.DaoFactory;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class LogMessageFindInteractor {

	private final Map<Integer, User> idToUSer = Maps.newHashMap();
	private final Map<Integer, Project> idToProject = Maps.newHashMap();

	public List<LogMessageFindResult> findMessages(LogMessageFindParams params) {
		FilterCriteria filter = new FilterCriteria();
		List<LogMessage> messages = DaoFactory.getInstance(LogMessageDAO.class).findLogMessages(filter);
		initIdToProjectMap();
		initIdToUsers();
		return Lists.newArrayList(Iterables.transform(messages, new MessageToResult()));
	}

	private void initIdToProjectMap() {
		List<Project> projects = DaoFactory.getInstance(ProjectDAO.class).findAllProjects();
		for (Project p : projects) {
			idToProject.put(p.id, p);
		}
	}

	private void initIdToUsers() {
		List<User> users = DaoFactory.getInstance(UserDAO.class).findAllUsers();
		for (User u : users) {
			idToUSer.put(u.id, u);
		}
	}

	class MessageToResult implements Function<LogMessage, LogMessageFindResult> {
		@Override
		public LogMessageFindResult apply(LogMessage logMessage) {
			Project project = idToProject.get(logMessage.projectId);
			User user = idToUSer.get(logMessage.userId);
			LogMessageFindResult result = new LogMessageFindResult();
			result.category = logMessage.category;
			result.id = logMessage.id;
			result.message = logMessage.message;
			result.timestamp = logMessage.timestamp;
			result.projectNr = project.projectNr;
			result.customer = project.customer;
			result.street = project.street;
			result.town = project.town;
			result.username = user.name;
			return result;
		}
	}
}
