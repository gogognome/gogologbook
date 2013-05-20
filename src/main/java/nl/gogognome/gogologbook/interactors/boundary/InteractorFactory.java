package nl.gogognome.gogologbook.interactors.boundary;

import java.util.Map;

import com.google.common.collect.Maps;

public class InteractorFactory {

	private static Map<Class<?>, Object> classToInstance = Maps.newHashMap();

	@SuppressWarnings("unchecked")
	public static <T> T getInteractor(Class<T> interactorClass) {
		T interactor = (T) classToInstance.get(interactorClass);
		if (interactor == null) {
			throw new InteractorException("No interactor registered for class " + interactorClass.getName());
		}
		return interactor;
	}

	public static <T> void registerInteractor(Class<T> interactorClass, T interactor) {
		classToInstance.put(interactorClass, interactor);
	}

	public static class InteractorException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public InteractorException(String message) {
			super(message);
		}

	}
}
