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

import java.util.LinkedList;

/**
 * This class is the base class for all models. It maintains the list of listeners.
 *
 * <p>A model can be disabled or enabled. By default it is enabled. Disabling the model
 * has no effect on the model itself, but a disabled model that is linked to a bean
 * will disable the input component of the bean to prevent the user from changing the model.
 * The disabled model can still be changed programmatically.
 *
 * @author Sander Kooijmans
 */
public class AbstractModel {

	private boolean enabled = true;

    /** Contains the subscribed listeners. */
    private LinkedList<ModelChangeListener> listeners = new LinkedList<ModelChangeListener>();

    /**
     * Adds a model change listener to this model.
     * @param listener the listener
     */
    public void addModelChangeListener(ModelChangeListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a model change listener from this model.
     * @param listener the listener
     */
    public void removeModelChangeListener(ModelChangeListener listener) {
        listeners.remove(listener);
    }

    /**
     * Enables or disables the model. Notifies all listeners about the change.
     * @param enabled true to enabled; false to disable
     * @param source if not <code>null</code>, then this indicates the
     *         listener that initiated this notification. If the listener
     *         is subcribed, it will not get notified by this method.
     */
    public void setEnabled(boolean enabled, ModelChangeListener source) {
    	this.enabled = enabled;
    	notifyListeners(source);
    }

    /**
     * Checks whether this model is enabled.
     * @return true if this model is enabled; false if this model is disabled
     */
    public boolean isEnabled() {
		return enabled;
	}

    /**
     * Notifies the subscribed listeners about a change in this model.
     * @param source if not <code>null</code>, then this indicates the
     *         listener that initiated this notification. If the listener
     *         is subcribed, it will not get notified by this method.
     */
    protected void notifyListeners(ModelChangeListener source) {
        for (ModelChangeListener listener : listeners) {
            if (listener != source) {
                listener.modelChanged(this);
            }
        }
    }
}
