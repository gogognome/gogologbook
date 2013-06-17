package nl.gogognome.gogologbook.interactors;

import nl.gogognome.gogologbook.interactors.boundary.LogMessageFindParams;
import nl.gogognome.gogologbook.interactors.boundary.LogMessageFindResult;
import nl.gogognome.lib.text.StringMatcher;
import nl.gogognome.lib.util.DateUtil;

import com.google.common.base.Predicate;

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