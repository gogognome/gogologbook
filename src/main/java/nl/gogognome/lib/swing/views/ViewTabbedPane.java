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

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nl.gogognome.lib.swing.WidgetFactory;
import nl.gogognome.lib.util.Factory;

/**
 * This class implements a tabbed pane that can hold <code>View</code>s.
 *
 * @author Sander Kooijmans
 */
public class ViewTabbedPane extends JTabbedPane {

	private static final long serialVersionUID = 1L;

    private final ArrayList<View> views = new ArrayList<View>();

    private final JFrame parentFrame;

    private boolean changeInProgress;

    /**
     * Constructor.
     * @param parentFrame the frame that contains this tabbed pane.
     */
    public ViewTabbedPane(JFrame parentFrame) {
        super();
        this.parentFrame = parentFrame;
        model.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent changeEvent) {
				userSelectedView();
			}
		});
    }

    /**
     * Adds a view to the tabbed pane.
     * @param view the view to be added
     */
    public void openView(final View view) {
    	changeInProgress = true;
    	try {
	        Action closeAction = new CloseAction(view);

	        view.setCloseAction(closeAction);
	        view.setParentWindow(parentFrame);
	        view.doInit();
	        addTab(view.getTitle(), view);
    		setTabComponentAt(getTabCount() - 1, new CloseableTab(view, closeAction));
	        views.add(view);
    	} finally {
    		changeInProgress = false;
    	}
    }

	/**
     * Removes a view from the tabbed pane.
     * @param view the view to be removed
     */
    public void closeView(View view) {
    	changeInProgress = true;
    	try {
	        if (views.contains(view)) {
	            view.doClose();
	            remove(view);
	            views.remove(view);
	        }
    	} finally {
    		changeInProgress = false;
    	}
    }

    /**
     * Select the tab that contains the specified view.
     * @param view the view
     */
    public void selectView(final View view) {
    	changeInProgress = true;
    	try {
	        int index = getIndexOfView(view);
	        if (index != -1) {
	            setSelectedIndex(index);
	            setDefaultButtonForView(view);
	            view.requestFocusInWindow();
	        }
    	} finally {
    		changeInProgress = false;
    	}
    }

    /** Closes all views in the tabbed pane. */
    public void closeAllViews() {
    	changeInProgress = true;
    	try {
	        for (Iterator<View> iter = views.iterator(); iter.hasNext();) {
	            View view = iter.next();
	            view.doClose();
	            super.remove(view);
	        }
	        views.clear();
    	} finally {
    		changeInProgress = false;
    	}
    }

    /**
     * Checks whether the specified view is present in this tabbed pane.
     * @param view the view
     * @return <code>true</code> if the specified view is present;
     *         <code>false</code> otherwise
     */
    public boolean hasView(View view) {
        return getIndexOfView(view) != -1;
    }

    public void remove(View view) {
    	changeInProgress = true;
    	try {
	        int index = getIndexOfView(view);
	        if (index != -1) {
	            remove(index);
	        }
    	} finally {
    		changeInProgress = false;
    	}
    }

    /**
     * This method can be called by the UI when the user closes the tab.
     * @param index index of the tab to be closed
     */
    @Override
	public void remove(int index) {
        View view = (View)getComponentAt(index);
        view.doClose();
        views.remove(view);
        super.remove(index);
    }

    /**
     * Sets the default button for the view.
     * @param view the view
     */
    private void setDefaultButtonForView(View view) {
        Container topLevelContainer = getTopLevelAncestor();
        if (topLevelContainer instanceof JFrame) {
            ((JFrame) topLevelContainer).getRootPane().setDefaultButton(view.getDefaultButton());
        }
    }

    /**
     * Gets the index of the view in the tabbed pane.
     * @param view the view
     * @return the index or -1 if the view is not part of this tabbed pane
     */
    private int getIndexOfView(View view) {
        int count = getTabCount();
        for (int i=0; i<count; i++) {
            if (getComponentAt(i).equals(view)) {
                return i;
            }
        }
        return -1;
    }

    private void userSelectedView() {
    	if (!changeInProgress) {
	        int index = getSelectedIndex();
	        if (index != -1) {
	        	View view = views.get(index);
	            setDefaultButtonForView(view);
	        }
    	}
    }

    private final class CloseAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private final View view;

		private CloseAction(View view) {
			this.view = view;
		}

		@Override
		public void actionPerformed(ActionEvent event) {
		    remove(view);
		}
	}

	private class CloseableTab extends JPanel {

    	CloseableTab(View view, Action closeAction) {
    		setOpaque(false);
    		setLayout(new FlowLayout());

    		WidgetFactory wf = Factory.getInstance(WidgetFactory.class);

    		JLabel label = new JLabel(view.getTitle());
    		label.setOpaque(false);

    		add(label);
    		add(wf.createIconButton("gen.closeTab", closeAction, 16));
    	}
    }
}

