package nl.gogognome.gogologbook.gui.session;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class Session {

	private final Logger logger = LoggerFactory.getLogger(Session.class);
	private final List<SessionListener> sessionListeners = Lists.newArrayList();

	public void addSessionListener(SessionListener listener) {
		sessionListeners.add(listener);
		logger.debug("Added listener " + listener);
	}

	public void removeSessionListener(SessionListener listener) {
		sessionListeners.remove(listener);
		logger.debug("Removed listener " + listener);
	}

	public void notifyListeners(SessionChangeEvent event) {
		logger.debug("Notifying " + sessionListeners.size() + " about event " + event);
		for (SessionListener listener : Lists.newArrayList(sessionListeners)) {
			try {
				listener.sessionChanged(event);
			} catch (Exception e) {
				logger.warn("Ignored exception from listener: " + e.getLocalizedMessage(), e);
			}
		}
	}
}
