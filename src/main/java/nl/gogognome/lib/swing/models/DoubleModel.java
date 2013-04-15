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
package nl.gogognome.lib.swing.models;

import nl.gogognome.lib.util.ComparatorUtil;

/**
 * This class implements a model for a <code>Double</code>.
 *
 * @author Sander Kooijmans
 */
public class DoubleModel extends AbstractModel {

    private Double value;

    /**
     * Checks whether the value of the model is null.
     * @return true if the value is null; false otherwise
     */
    public boolean isNull() {
    	return value == null;
    }

    /**
     * Gets the value of the model.
     * @return the value (possibly null)
     */
    public Double getDouble() {
        return value;
    }

    /**
     * Sets the double of this model.
     * @param newDouble the new value of the double (null is allowed)
     */
    public void setDouble(Double newDouble) {
    	setDouble(newDouble, null);
    }

    /**
     * Sets the double of this model.
     * @param newValue the new value of the double
     * @param source the model change listener that sets the double. It will not
     *         get notified. It may be <code>null</code>.
     */
    public void setDouble(Double newValue, ModelChangeListener source) {
        Double oldValue = this.value;
        if (!ComparatorUtil.equals(oldValue, newValue)) {
            this.value= newValue;
            notifyListeners(source);
        }
    }
}
