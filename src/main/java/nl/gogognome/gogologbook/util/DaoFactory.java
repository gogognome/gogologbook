package nl.gogognome.gogologbook.util;

import java.util.Map;

import com.google.common.collect.Maps;


public class DaoFactory {

	private final static Map<Class<?>, Object> classToImplementation = Maps.newHashMap();

	public static <T> void register(Class<T> clazz, T implementation) {
		classToImplementation.put(clazz, implementation);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getInstance(Class<T> clazz) {
		return (T) classToImplementation.get(clazz);
	}

	public static void clear() {
		classToImplementation.clear();
	}

}
