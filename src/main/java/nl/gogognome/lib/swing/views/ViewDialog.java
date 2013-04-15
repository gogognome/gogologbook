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
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import nl.gogognome.lib.swing.SwingUtils;

/**
 * This class implements a <code>JDialog</code> that can hold a <code>View</code>.
 *
 * @author Sander Kooijmans
 */
public class ViewDialog {

    /** The view shown in this dialog. */
    private View view;

    /** The actual dialog holding the view. */
    private JDialog dialog;

    /**
     * The bounds of the parent frame or dialog at the moment this dialog
     * was created.
     */
    private Rectangle parentBounds;

    /** The action to close this dialog. */
    private Action closeAction;

    /**
     * Constructor.
     * @param parentComponent the parent component of the dialog
     * @param view the view to be shown in this dialog
     */
    public ViewDialog(Component parentComponent, View view) {
    	Container owner = SwingUtils.getTopLevelContainer(parentComponent);
        if (owner instanceof JDialog) {
            initDialog(new JDialog((JDialog)owner, view.getTitle(), true), view, owner.getBounds());
        } else  if (owner instanceof JFrame){
            initDialog(new JDialog((JFrame)owner, view.getTitle(), true), view, owner.getBounds());
        } else {
            throw new IllegalArgumentException("The owner must be a JDialog or a JFrame, but was: " + owner.getClass());
        }
    }

    /**
     * Initializes the dialog
     * @param dialog the dialog
     * @param view the view to be shown in the dialog
     */
    private void initDialog(JDialog dialog, View view, Rectangle parentBounds) {
        this.dialog = dialog;
        this.parentBounds = parentBounds;
        setView(view);

        dialog.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { dispose(); } }
        );

        dialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESC_ACTION");
        dialog.getRootPane().getActionMap().put("ESC_ACTION", closeAction);
    }

    /**
     * Shows the dialog.
     */
    final public void showDialog() {
        // Show the dialog.
        dialog.pack();
        Dimension d = dialog.getPreferredSize();
        dialog.setLocation( parentBounds.x + (parentBounds.width-d.width)/2,
            parentBounds.y + (parentBounds.height-d.height)/2 );
        dialog.setVisible(true);
    }

    /**
     * Sets the view on this dialog.
     * @param view the view
     */
    private void setView(View view) {
        this.view = view;
        closeAction = new AbstractAction() {
            @Override
			public void actionPerformed(ActionEvent event) {
                dispose();
            }
        };

        view.setParentWindow(dialog);
        view.setCloseAction(closeAction);
        view.doInit();
        dialog.getContentPane().add(view, BorderLayout.CENTER);

        JButton defaultButton = view.getDefaultButton();
        if (defaultButton != null) {
            dialog.getRootPane().setDefaultButton(defaultButton);
        }
    }

    /** Disposes the dialog. */
    public void dispose() {
        view.doClose();
        dialog.dispose();
    }
}
