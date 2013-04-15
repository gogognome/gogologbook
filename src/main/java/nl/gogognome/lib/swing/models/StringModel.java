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
 * This class implements a model for a <code>String</code>.
 *
 * @author Sander Kooijmans
 */
public class StringModel extends AbstractModel {

    private String string;

    public String getString() {
        return string;
    }

    /**
     * Sets the string of this model.
     * @param newString the new value of the string
     */
    public void setString(String newString) {
    	setString(newString, null);
    }

    /**
     * Sets the string of this model.
     * @param newString the new value of the string
     * @param source the model change listener that sets the string. It will not
     *         get notified. It may be <code>null</code>.
     */
    public void setString(String newString, ModelChangeListener source) {
        String oldString = this.string;
        if (!ComparatorUtil.equals(oldString, newString)) {
            this.string = newString;
            notifyListeners(source);
        }
    }
}
