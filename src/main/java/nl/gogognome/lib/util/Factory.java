/*
    This file is part of gogo account.

    gogo account is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    gogo account is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with gogo account.  If not, see <http://www.gnu.org/licenses/>.
*/
package nl.gogognome.lib.util;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This class implements a factory that can be used
 * for dependency injection.
 *
 * @author Sander Kooijmans
 */
public class Factory {

	private final Map<Class<?>, Class<?>> boundClasses
		= new HashMap<Class<?>, Class<?>>();

	private final Map<Class<?>, Object> singletonClasses
		= new HashMap<Class<?>, Object>();

	public static Factory instance = new Factory();

	/**
	 * Binds an implementation type to an interface type.
	 * @param interfaceType
	 * @param implementationType
	 */
	public static <T> void bindClass(Class<T> interfaceType, Class<? extends T> implementationType) {
		instance.bindClassImpl(interfaceType, implementationType);
	}

	private <T> void bindClassImpl(Class<T> interfaceType, Class<? extends T> implementationType) {
		boundClasses.put(interfaceType, implementationType);
	}

	/**
	 * Creates an instance for the specified interface type.
	 * @param <T> the interface type
	 * @param interfaceType
	 * @return the instance; never null
	 * @throws RuntimeException if no implementation type was registered
	 */
	public static <T> T createInstance(Class<T> interfaceType) {
		return instance.createInstanceImpl(interfaceType);
	}

	/**
	 * Creates an instance for the specified interface type.
	 * @param <T> the interface type
	 * @param interfaceType
	 * @param args constructor arguments
	 * @return the instance; never null
	 * @throws RuntimeException if no implementation type was registered
	 */
	public static <T> T createInstance(Class<T> interfaceType, Object... args) {
		return instance.createInstanceImpl(interfaceType, args);
	}

	private <T> T createInstanceImpl(Class<T> interfaceType) {
		Class<T> implType = (Class<T>) boundClasses.get(interfaceType);
		if (implType == null) {
			throw new RuntimeException("No implementation registered for interface "
					+ interfaceType.getName());
		}
		try {
			return implType.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Failed to create instance of " + implType.getName(), e);
		}
	}

	private <T> T createInstanceImpl(Class<T> interfaceType, Object... args) {
		Class<T> implType = (Class<T>) boundClasses.get(interfaceType);
		if (implType == null) {
			throw new RuntimeException("No implementation registered for interface "
					+ interfaceType.getName());
		}
		try {
			for (Constructor<?> constructor : implType.getConstructors()) {
				if (constructor.getParameterTypes().length == args.length) {
					try {
						return (T) constructor.newInstance(args);
					} catch (Exception e) {
						// probably wrong constructor. Try next constructor. 
					}
				}
			}
			throw new Exception("No constructor found for arguments " + Arrays.toString(args));
		} catch (Exception e) {
			throw new RuntimeException("Failed to create instance of " + implType.getName(), e);
		}
	}

	/**
	 * Registers the singleton instance for the specified interface type.
	 * @param <T> the type of the interface
	 * @param interfaceType
	 * @param singleton must not be null
	 */
	public static <T> void bindSingleton(Class<T> interfaceType, T singleton) {
		instance.bindSingletonImpl(interfaceType, singleton);
	}

	private <T> void bindSingletonImpl(Class<T> interfaceType, T singleton) {
		singletonClasses.put(interfaceType, singleton);
	}

	/**
	 * Gets the singleton instance for the specified interface type.
	 * @param <T> the type of the interface
	 * @param interfaceType
	 * @return the singleton instance; never null
	 * @throws RuntimeException if no singleton instance was registered
	 */
	public static <T> T getInstance(Class<T> interfaceType) {
		return instance.getInstanceImpl(interfaceType);
	}

	private <T> T getInstanceImpl(Class<T> interfaceType) {
		T singleton = (T) singletonClasses.get(interfaceType);
		if (singleton == null) {
			throw new RuntimeException("No singleton registered for interface " + interfaceType.getName());
		}
		return singleton;
	}
}
