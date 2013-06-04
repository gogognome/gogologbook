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
import nl.gogognome.lib.text.StringMatcher;
import nl.gogognome.lib.util.DateUtil;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class LogMessageFindInteractor {

	private final Map<Integer, User> idToUSer = Maps.newHashMap();
	private final Map<Integer, Project> idToProject = Maps.newHashMap();

	public List<LogMessageFindResult> findLogMessagesByDescendingDate(LogMessageFindParams params) {
		FilterCriteria filter = new FilterCriteria();
		List<LogMessage> messages = DaoFactory.getInstance(LogMessageDAO.class).findLogMessagesByDescendingDate(filter);
		initIdToProjectMap();
		initIdToUsers();
		Iterable<LogMessageFindResult> unfilteredLogMessages = Iterables.transform(messages, new MessageToResult());
		return Lists.newArrayList(Iterables.filter(unfilteredLogMessages, new FilterPredicate(params)));
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

class FilterPredicate implements Predicate<LogMessageFindResult> {

	private final LogMessageFindParams params;
	private final StringMatcher userMatcher;
	private final StringMatcher projectMatcher;
	private final StringMatcher customerMatcher;
	private final StringMatcher townMatcher;
	private final StringMatcher categoryMatcher;
	private final StringMatcher messageMatcher;

	public FilterPredicate(LogMessageFindParams params) {
		this.params = params;
		userMatcher = createMatcherIfNotNull(params.user);
		projectMatcher = createMatcherIfNotNull(params.project);
		customerMatcher = createMatcherIfNotNull(params.customer);
		townMatcher = createMatcherIfNotNull(params.town);
		categoryMatcher = createMatcherIfNotNull(params.category);
		messageMatcher = createMatcherIfNotNull(params.message);
	}

	private StringMatcher createMatcherIfNotNull(String s) {
		return s != null ? new StringMatcher(s, true) : null;
	}

	@Override
	public boolean apply(LogMessageFindResult logMessage) {
		if (logMessage.timestamp != null) {
			if (params.from != null && DateUtil.compareDayOfYear(params.from, logMessage.timestamp) > 0) {
				return false;
			}
			if (params.to != null && DateUtil.compareDayOfYear(params.to, logMessage.timestamp) < 0) {
				return false;
			}
		}
		if (userMatcher != null && userMatcher.match(logMessage.username) == -1) {
			return false;
		}
		if (projectMatcher != null && projectMatcher.match(logMessage.projectNr) == -1) {
			return false;
		}
		if (customerMatcher != null && customerMatcher.match(logMessage.customer) == -1) {
			return false;
		}
		if (townMatcher != null && townMatcher.match(logMessage.town) == -1) {
			return false;
		}
		if (categoryMatcher != null && categoryMatcher.match(logMessage.category) == -1) {
			return false;
		}
		if (messageMatcher != null && messageMatcher.match(logMessage.message) == -1) {
			return false;
		}

		return true;
	}
}