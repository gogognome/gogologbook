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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * This class implements a container for a view.
 *
 * <p>If a view is added to a dialog, then closing the view will automatically close the dialog.
 * If a wizzard-like dialog must be made, in which a sequence of views are shown, then add
 * a <code>ViewContainer</code> to the dialog and show the sequences of views in this
 * <code>ViewContainer</code>. After showing the last view, close the <code>ViewContainer</code>
 * to close the dialog.
 */
public class ViewContainer extends View {

    /** The view that is shown in this <code>ViewContainer</code>. */
    private View view;

    /** The title of this <code>ViewContainer</code>. */
    private String title;

    /**
     * Constructor.
     * @param title the title of this <code>ViewContainer</code>
     */
    public ViewContainer(String title) {
        this.title = title;
    }

    /**
     * Gets the title of this view.
     * @return the title
     */
    @Override
	public String getTitle() {
        return title;
    }

    /** This method is called when the <code>ViewContainer</code> is closed. */
    @Override
	public void onClose() {
        removeView();
    }

    /** This method is called when the <code>ViewContainer</code> is initialized. */
    @Override
	public void onInit() {
        setLayout(new BorderLayout());
    }

    /**
     * Sets the view on this <code>ViewContainer</code>.
     * If there is another view set on this view, that view will be closed first.
     * @param view the view
     */
    public void setView(View view) {
        removeView();
        this.view = view;
        view.setParentWindow(getParentWindow());
        view.setCloseAction(new AbstractAction() {
            @Override
			public void actionPerformed(ActionEvent e) {
                removeView();
            }
        });
        view.doInit();
        add(view, BorderLayout.CENTER);
        validate();
    }

    /** Removes the view from this <code>ViewContainer</code>. */
    public void removeView() {
        View oldView = view;
        view = null;
        if (oldView != null) {
            remove(oldView);
            oldView.doClose();
        }
    }
}
