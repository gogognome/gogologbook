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
package nl.gogognome.lib.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import nl.gogognome.lib.swing.views.View;
import nl.gogognome.lib.text.TextResource;
import nl.gogognome.lib.util.Factory;

/**
 * This abstract class implements the framework for a dialog with one or more buttons.
 * Subclasses may use the protected methods and should generally override the
 * methods <tt>handleCancel()</tt> and <tt>handleButton()</tt>.
 */
public abstract class DialogWithButtons implements ActionListener, KeyListener, FocusListener
{
	/** The string id for the OK button. */
	public final static String[] BT_OK = new String[] {"gen.ok"};

	/** The string ids for the OK and Cancel buttons. */
	public final static String[] BT_OK_CANCEL = new String[] { "gen.ok", "gen.cancel" };

	/**
	 * The bounds of the parent frame or dialog at the moment this dialog
	 * was created.
	 */
	private Rectangle parentBounds;

	/** The dialog shown to the user. */
	private JDialog dialog;

	/** The component supplied by the subclass. */
	private Component component;

	/** The buttons. */
	private JButton[] buttons;

	/**
	 * After the dialog has been hidden, this field contains the number of the
	 * button that was pressed or -1 if none of the buttons was pressed.
	 */
	private int selectedButton;

	/**
	 * Creates a dialog with one or more buttons.
	 * @param frame the frame that owns this dialog.
	 * @param titleId the id of the title string
	 * @param buttonIds the ids of the buttons.
	 */
	protected DialogWithButtons(Frame frame, String titleId, String[] buttonIds) {
		initDialog(new JDialog(frame,
				Factory.getInstance(TextResource.class).getString(titleId), true), buttonIds, frame.getBounds());
	}

	/**
	 * Creates a dialog with one or more buttons.
	 * @param dialog the dialog that owns this dialog.
	 * @param titleId the id of the title string
	 * @param buttonIds the ids of the buttons.
	 */
	protected DialogWithButtons(Dialog dialog, String titleId, String[] buttonIds) {
		initDialog(new JDialog(dialog,
				Factory.getInstance(TextResource.class).getString(titleId), true), buttonIds,
			dialog.getBounds());
	}

    /**
     * Creates a dialog with one or more buttons.
     * @param view the view that owns this dialog.
     * @param titleId the id of the title string
     * @param buttonIds the ids of the buttons.
     */
    protected DialogWithButtons(View view, String titleId, String[] buttonIds) {
        initDialog(new JDialog(view.getParentWindow(),
        		Factory.getInstance(TextResource.class).getString(titleId), Dialog.ModalityType.APPLICATION_MODAL),
            buttonIds, view.getParentWindow().getBounds());
    }

	/**
	 * Initializes the dialog. The buttons are added to the dialog and the dialog is
	 * centered in its parent dialog or frame.
	 *
	 * @param dialog the dialog to be initialized.
	 * @param buttonIds the ids of the button labels.
	 * @param parentBounds the bounds of the parent dialog or window.
	 */
	private void initDialog(JDialog dialog, String buttonIds[], Rectangle parentBounds) {
		this.dialog = dialog;
		this.parentBounds = parentBounds;
		buttons = new JButton[buttonIds.length];
		for (int i=0; i<buttonIds.length; i++) {
			buttons[i] = Factory.getInstance(WidgetFactory.class).createButton(buttonIds[i], null);
			buttons[i].addActionListener(this);
		}
		selectedButton = -1;
	}

	/**
	 * This method is called when the subclass has initiazlized its contents.
	 * @param c the component containing the contents.
	 */
	final protected void componentInitialized(Component c) {
		component = c;

		GridBagLayout gbl = new GridBagLayout();
        JPanel panel = new JPanel(gbl);
        panel.add(component, SwingUtils.createPanelGBConstraints(0, 0));
		dialog.getContentPane().add(panel, BorderLayout.CENTER);

		// The buttons are added to a ButtonPanel.
		ButtonPanel buttonPanel = new ButtonPanel(SwingConstants.CENTER);
		for (int i=0; i<buttons.length; i++) {
			buttonPanel.add(buttons[i]);
		}

		dialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		// Set the default button
		if (buttons.length > 0) {
		    setDefaultButton(buttons[0]);
        }

		dialog.setResizable(false);

		dialog.addWindowListener(new WindowAdapter() {
			@Override
            public void windowClosing(WindowEvent e) { handleCancel(); } }
		);

		addListeners(dialog);
	}

	/**
	 * Sets the default button of this dialog.
	 * @param button the default button of this dialog
	 */
	public void setDefaultButton(JButton button) {
	    dialog.getRootPane().setDefaultButton(button);
	}

	/**
	 * Gets the default button of this dialog.
	 * @return the default button
	 */
	public JButton getDefaultButton() {
	    return dialog.getRootPane().getDefaultButton();
	}

	/**
	 * Adds key listeners to the specified component and all of its sub-components.
	 * @param c the component
	 */
	private void addListeners( Component c )
	{
		c.addKeyListener(this);
		if (c instanceof JTextField)
		{
			c.addFocusListener(this);
		}
		if (c instanceof Container)
		{
			Container container = (Container)c;
			Component[] components = container.getComponents();
			for (int i=0; i<components.length; i++)
			{
				addListeners(components[i]);
			}
		}
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
	 * Handles the cancel event. In this particular implementation, this method is
	 * called when the dialog is closed not by pressing one of the buttons.
	 */
	protected void handleCancel()
	{
		hideDialog();
	}

	/**
	 * Handles the button-pressed event. This method is called when one of the buttons
	 * has been pressed by the user. The current implementation ignores the event.
	 * Subclasses will typically override this method.
	 * @param index the index of the button (as specified by the <tt>buttonIds</tt>
	 *        passed to the constructor.
	 */
	protected void handleButton( int index )
	{
		hideDialog();
	}

	/**
	 * Gets the button that was pressed by the user.
	 *
	 * @return the button that was pressed by the user (as an index in the
	 *         <tt>buttonIds</tt> passed to the constructor) or <tt>-1</tt>
	 *         if none of the buttons was selected (but e.g., the user pressed escape).
	 */
	final public int getSelectedButton()
	{
		return selectedButton;
	}

	/** Hides the dialog. */
	final protected void hideDialog() {
		dialog.dispose();
	}

	/**
	 * This method should be called after <tt>componentInitialized()</tt> has been called,
	 * to determine whether this dialog is resizable by the user.
	 *
	 * @param resizable <tt>true</tt> if the user can resize this dialog;
	 *        <tt>false</tt> otherwise.
	 */
	final protected void setResizable( boolean resizable )
	{
		dialog.setResizable(resizable);
	}

	/**
	 * Handles the button pressed event. This method is introduced to make sure
	 * that <tt>selectedButton</tt> is set when <tt>handleButtonPressed()</tt>
	 * is called. When <tt>handleButtonPressed()</tt> is overriden,
	 * <tt>selectedButton</tt> still gets the correct value.
	 *
	 * @param index the index of the button (as specified by the <tt>buttonIds</tt>
	 *        passed to the constructor.
	 */
	private void buttonPressed( int index )
	{
		selectedButton = index;
		handleButton(index);
	}

	/**
	 * This method is called when an action occurs. This event is generated by
	 * the OK or Cancel button.
	 * @param e the event.
	 */
	@Override
	final public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		for (int i=0; i<buttons.length; i++)
		{
			if (buttons[i] == source) // used == intentionally
			{
				buttonPressed(i);
				break;
			}
		}
	}

	/**
	 * This method is called when a key is pressed.
	 * @param e the key event.
	 */
	@Override
	public void keyPressed(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
        case KeyEvent.VK_ENTER:
            Component c = dialog.getFocusOwner();
        	if (c instanceof JButton) {
        	    actionPerformed(new ActionEvent(c, 0, null));
        	} else {
        	    buttonPressed(0);
        	}
        	break;

			case KeyEvent.VK_ESCAPE:
				handleCancel();
				break;

			default:
				// ignore other keys
				break;
		}
	}

	/**
	 * This method is called when a key is released.
	 * @param e the key event.
	 */
	@Override
	public void keyReleased(KeyEvent e)
	{
		// ignore this event
	}

	/**
	 * This method is called when a key is typed.
	 * @param e the key event.
	 */
	@Override
	public void keyTyped(KeyEvent e)
	{
		// ignore this event
	}

	/**
	 * This method is called when a field gains focus.
	 * @param event the event.
	 */
	@Override
	final public void focusGained(FocusEvent event)
	{
		Object source = event.getSource();
		if (source instanceof JTextComponent)
		{
			((JTextComponent)source).selectAll();
		}
	}

	/**
	 * This method is called when a field loses focus.
	 * @param event the event.
	 */
	@Override
	final public void focusLost(FocusEvent event)
	{
		Object source = event.getSource();
		if (source instanceof JTextComponent)
		{
			((JTextComponent)source).select(0, 0);
		}
	}

	/** Validates the dialog and all of its subcomponents. */
	final protected void validateDialog()
	{
		dialog.validate();
	}

	/**
	 * Requests focus for the dialog.
	 */
	public void requestFocus()
	{
	    dialog.requestFocus();
	}

	/**
	 * Causes this dialog to be sized to fit the preferred size and layouts of
	 * its subcomponents. If the dialog and/or its owner are not yet displayable,
	 * both are made displayable before calculating the preferred size.
	 * The dialog will be validated after the preferredSize is calculated.
	 */
	public void pack() {
	    dialog.pack();
	}
}
