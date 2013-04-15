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
package nl.gogognome.lib.swing.views;

import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import nl.gogognome.lib.gui.Closeable;
import nl.gogognome.lib.gui.beans.BeanFactory;
import nl.gogognome.lib.swing.WidgetFactory;
import nl.gogognome.lib.text.TextResource;
import nl.gogognome.lib.util.Factory;

/**
 * A view represents a rectangular area inside a dialog or frame. A view typically
 * implements a cohesive part of the user interface.
 *
 * <p>Views can be made visible inside a dialog (see {@link ViewDialog}),
 * a frame (see {@link ViewFrame}), tabbed pane (see {@link ViewTabbedPane}) and even
 * inside other views (see {@link ViewContainer}).
 *
 * <p>When a view is added to one of the aforementioned containers, its {@link #onInit()}
 * method will be called by the container.
 *
 * <p>The container will call {@link #onClose()} when the container removes the view.
 * The view can close itself by calling {@link #requestClose()}.
 *
 * <p>Some user interface components implement the {@link Closeable} interface.
 * If your view uses such components, call {@link #addCloseable(Closeable)}
 * for each of these components. By doing so, the deinitialize() method will be called
 * automatically for these components when the view is closed. Otherwise, you have to
 * think about deinitializing them in your onClose() implementation.
 *
 * @author Sander Kooijmans
 */
public abstract class View extends JPanel implements Closeable {

	private static final long serialVersionUID = 1L;

    private ArrayList<ViewListener> listeners = new ArrayList<ViewListener>();

    /**
     * This action closes the view. The action will be set before the view
     * is shown. The view can use it let itself be closed.
     */
    protected Action closeAction;

    private Window parentWindow;
    private JButton defaultButton;

    /** Objects to be closed when the view is closed. */
    private List<Closeable> closeables = new ArrayList<Closeable>();

    protected TextResource textResource = Factory.getInstance(TextResource.class);
    protected WidgetFactory widgetFactory = Factory.getInstance(WidgetFactory.class);
    protected BeanFactory beanFactory = Factory.getInstance(BeanFactory.class);

    /**
     * Gets the title of the view.
     * @return the title of the view
     */
    public abstract String getTitle();

    /**
     * Gets the default button of this view.
     * @return the default button of this view or <code>null</code> if this view
     *         has no default button
     */
    JButton getDefaultButton() {
        return defaultButton;
    }

    /** This method is called before the view is shown. It initializes the view. */
    public abstract void onInit();

    /** This method is called just before the view is closed. It can free resources. */
    public abstract void onClose();

    /**
     * Sets the close action.
     *
     * @param closeAction the close action
     */
    public void setCloseAction(Action closeAction) {
        this.closeAction = closeAction;
    }

    public void setParentWindow(Window parentWindow) {
        this.parentWindow = parentWindow;
    }

    public Window getParentWindow() {
    	return parentWindow;
    }

    public void addViewListener(ViewListener listener) {
        listeners.add(listener);
    }

    public void removeViewListener(ViewListener listener) {
        listeners.remove(listener);
    }

    /**
     * Sets the default button of this view.
     * @param button the default button
     */
    public void setDefaultButton(JButton button) {
        defaultButton = button;
        if (parentWindow instanceof JFrame) {
            ((JFrame) parentWindow).getRootPane().setDefaultButton(button);
        } else if (parentWindow instanceof JDialog) {
        	((JDialog) parentWindow).getRootPane().setDefaultButton(button);
        }
    }

    /**
     * Adds an object that will be closed automatically when this view is closed.
     * @param d the Closeable object
     */
    public void addCloseable(Closeable d) {
    	closeables.add(d);
    }

    /** Call this method to close the view programmatically. */
    public void requestClose() {
    	closeAction.actionPerformed(null);
    }

    /**
     * Closes the view and notifies listeners.
     */
    void doClose() {
        onClose();

        for (Closeable d : closeables) {
        	d.close();
        }

        ViewListener[] tempListeners = listeners.toArray(new ViewListener[listeners.size()]);
        for (int i = 0; i < tempListeners.length; i++) {
            tempListeners[i].onViewClosed(this);
        }
    }

	/** Initializes the view. */
    void doInit() {
        onInit();
    }

    @Override
    public void close() {
    	closeAction.actionPerformed(null);
    }
}
