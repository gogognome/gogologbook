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


/**
 * This class implements a model for a <code>Boolean</code>.
 *
 * @author Sander Kooijmans
 */
public class BooleanModel extends AbstractModel {

    private boolean value;

    public boolean getBoolean() {
        return value;
    }

    /**
     * Sets the boolean of this model.
     * @param newValue the new value of the boolean
     */
    public void setBoolean(boolean newValue) {
    	setBoolean(newValue, null);
    }

    /**
     * Sets the boolean of this model.
     * @param newValue the new value of the boolean
     * @param source the model change listener that sets the boolean. It will not
     *         get notified. It may be <code>null</code>.
     */
    public void setBoolean(boolean newValue, ModelChangeListener source) {
        boolean oldValue = this.value;
        if (oldValue != newValue) {
            this.value = newValue;
            notifyListeners(source);
        }
    }
}
