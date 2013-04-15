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

import java.util.Date;

import com.google.common.base.Objects;

/**
 * This class implements a model for a <code>Date</code>.
 *
 * @author Sander Kooijmans
 */
public class DateModel extends AbstractModel {

    private Date date;

    public DateModel() {
    	this(null);
    }

    public DateModel(Date date) {
    	this.date = date;
    }

    public Date getDate() {
        return date;
    }

    /**
     * Sets the date of this model.
     * @param newDate the new value of the date
     */
    public void setDate(Date newDate) {
    	setDate(newDate, null);
    }

    /**
     * Sets the date of this model.
     * @param newDate the new value of the date
     * @param source the model change listener that sets the date. It will not
     *         get notified. It may be <code>null</code>.
     */
    public void setDate(Date newDate, ModelChangeListener source) {
        Date oldDate = this.date;
        if (!Objects.equal(oldDate, newDate)) {
            this.date = newDate;
            notifyListeners(source);
        }
    }
}
