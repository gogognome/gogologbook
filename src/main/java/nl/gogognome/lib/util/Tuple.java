/*
   Copyright 2011 Sander Kooijmans

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package nl.gogognome.lib.util;

/**
 * This class represents an immutable tuple of two objects.
 *
 * @author Sander Kooijmans
 */
public class Tuple<S,T> {

	private final S s;
	private final T t;

	public Tuple(S s, T t) {
		super();
		this.s = s;
		this.t = t;
	}

	public S getFirst() {
		return s;
	}

	public T getSecond() {
		return t;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Tuple) {
			Tuple<?,?> that = (Tuple<?,?>) obj;
			return ComparatorUtil.equals(this.getFirst(), that.getFirst())
				&& ComparatorUtil.equals(this.getSecond(), that.getSecond());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int h = 23;
		if (getFirst() != null) {
			h += 37 * getFirst().hashCode();
		}
		if (getSecond() != null) {
			h += 83 * getSecond().hashCode();
		}
		return h;
	}
}
