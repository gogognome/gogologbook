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

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import nl.gogognome.lib.text.TextResource;
import nl.gogognome.lib.util.Factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class implements dialog used to show a message to the user.
 *
 * @author Sander Kooijmans
 */
public class MessageDialog extends DialogWithButtons {

    /** Return value from class method if YES is chosen. */
    public static final int YES_OPTION = JOptionPane.YES_OPTION;
    /** Return value from class method if NO is chosen. */
    public static final int NO_OPTION = JOptionPane.NO_OPTION;
    /** Return value from class method if CANCEL is chosen. */
    public static final int CANCEL_OPTION = JOptionPane.CANCEL_OPTION;
    /** Return value form class method if OK is chosen. */
    public static final int OK_OPTION = JOptionPane.OK_OPTION;
    /** Return value from class method if user closes window without selecting
     * anything, more than likely this should be treated as either a
     * <code>CANCEL_OPTION</code> or <code>NO_OPTION</code>. */
    public static final int CLOSED_OPTION = JOptionPane.CLOSED_OPTION;

	private final static Logger LOGGER = LoggerFactory.getLogger(MessageDialog.class);

	/**
	 * Constructor.
	 *
	 * @param owner the owner of this dialog.
	 * @param titleId the id of the title string.
	 * @param message the message to be shown to the user.
	 */
	private MessageDialog(Frame owner, String titleId, String message) {
		this(owner, titleId, message, BT_OK);
	}

	/**
	 * Constructor.
	 *
	 * @param owner the owner of this dialog.
	 * @param titleId the id of the title string.
	 * @param message the message to be shown to the user.
	 * @param buttonIds the ids of the buttons.
	 */
	private MessageDialog(Frame owner, String titleId, String message, String[] buttonIds) {
		super(owner, titleId, buttonIds);
		showDialog(message);
	}

	/**
	 * Constructor.
	 *
	 * @param owner the owner of this dialog.
	 * @param titleId the id of the title string.
	 * @param message the message to be shown to the user.
	 */
	private MessageDialog(Dialog owner, String titleId, String message) {
		this(owner, titleId, message, BT_OK);
	}

	/**
	 * Constructor.
	 *
	 * @param owner the owner of this dialog.
	 * @param titleId the id of the title string.
	 * @param message the message to be shown to the user.
	 * @param buttonIds the ids of the buttons.
	 */
	private MessageDialog(Dialog owner, String titleId, String message, String[] buttonIds) {
		super(owner, titleId, buttonIds);
		showDialog(message);
	}

    /**
     * Constructor.
     *
     * @param owner the owner of this dialog.
     * @param titleId the id of the title string.
     * @param t throwable whose message will be shown to the user.
     */
    private MessageDialog(Frame owner, String titleId, Throwable t) {
        this(owner, titleId, t.getMessage(), BT_OK);
        t.printStackTrace();
    }

	/**
	 * Shows the dialog.
	 * @param message the message to be shown. The sequence of the characters
	 *        '\\' and 'n' indicate a line break.
	 */
	private void showDialog( String message ) {
	    String[] lines = message.split("\\n");
	    Component messageComponent;
	    if (lines.length > 1) {
		    JPanel panel = new JPanel(new GridLayout(lines.length, 1));
		    for (int i = 0; i < lines.length; i++) {
                panel.add(new JLabel(lines[i]));
            }
	        messageComponent = panel;
	    } else {
	        messageComponent = new JLabel(message);
	    }
		componentInitialized(messageComponent);
		super.showDialog();
	}

	/**
	 * Shows an info message dialog.
     * @param parentComponent determines the <code>Frame</code>
     *		in which the dialog is displayed; if <code>null</code>,
     *		or if the <code>parentComponent</code> has no
     *		<code>Frame</code>, a default <code>Frame</code> is used
	 * @param message the id of the message to be shown to the user.
     * @param args optional arguments to be filled in the placeholders of the message
	 */
	public static void showInfoMessage(Component parentComponent, String messageId, Object... args) {
		TextResource tr = Factory.getInstance(TextResource.class);
		JOptionPane.showMessageDialog(parentComponent, tr.getString(messageId, args),
				tr.getString("gen.titleMessage"), JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Shows a message dialog.
     * @param parentComponent determines the <code>Frame</code>
     *		in which the dialog is displayed; if <code>null</code>,
     *		or if the <code>parentComponent</code> has no
     *		<code>Frame</code>, a default <code>Frame</code> is used
	 * @param titleId the id of the title string.
	 * @param message the id of the message to be shown to the user.
     * @param args optional arguments to be filled in the placeholders of the message
	 */
	public static void showMessage(Component parentComponent, String titleId, String messageId, Object... args) {
		TextResource tr = Factory.getInstance(TextResource.class);
		JOptionPane.showMessageDialog(parentComponent, tr.getString(messageId, args), tr.getString(titleId),
				JOptionPane.INFORMATION_MESSAGE);
	}

    /**
     * Shows a warning message dialog.
     * @param parentComponent determines the <code>Frame</code>
     *		in which the dialog is displayed; if <code>null</code>,
     *		or if the <code>parentComponent</code> has no
     *		<code>Frame</code>, a default <code>Frame</code> is used
     * @param messageId the id of the message
     * @param args optional arguments to be filled in the placeholders of the message
     */
    public static void showWarningMessage(Component parentComponent, String messageId, Object... args) {
    	TextResource tr = Factory.getInstance(TextResource.class);
    	String message = tr.getString(messageId, args);
    	showFormattedMessage(parentComponent, message, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Shows an error message dialog.
     * @param parentComponent determines the <code>Frame</code>
     *		in which the dialog is displayed; if <code>null</code>,
     *		or if the <code>parentComponent</code> has no
     *		<code>Frame</code>, a default <code>Frame</code> is used
     * @param messageId the id of the message
     * @param args optional arguments to be filled in the placeholders of the message
     */
    public static void showErrorMessage(Component parentComponent, String messageId, Object... args) {
    	TextResource tr = Factory.getInstance(TextResource.class);
    	String message = tr.getString(messageId, args);
    	LOGGER.warn(message);
    	showFormattedMessage(parentComponent, message, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows an error message dialog.
     * @param parentComponent determines the <code>Frame</code>
     *		in which the dialog is displayed; if <code>null</code>,
     *		or if the <code>parentComponent</code> has no
     *		<code>Frame</code>, a default <code>Frame</code> is used
     * @param t throwable whose message is
     * @param messageId the id of the message
     * @param args optional arguments to be filled in the placeholders of the message
     */
    public static void showErrorMessage(Component parentComponent, Throwable t, String messageId, Object... args) {
    	TextResource tr = Factory.getInstance(TextResource.class);
    	List<String> lines = new ArrayList<String>();
    	String message = tr.getString(messageId, args);
    	lines.add(message);

    	addStackTraceToLines(lines, t);

    	showFormattedMessage(parentComponent, lines.toArray(), JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows a dialog with a yes and no button. The dialog further contains a title and a message.
     * @param parentComponent determines the <code>Frame</code>
     *		in which the dialog is displayed; if <code>null</code>,
     *		or if the <code>parentComponent</code> has no
     *		<code>Frame</code>, a default <code>Frame</code> is used
     * @param titleId the id of the title
     * @param messageId the id of the message
     * @param args optional arguments to be filled in the placeholders of the message
     * @return {@link #YES_OPTION}, {@link #NO_OPTION} or {@link #CLOSED_OPTION}.
     */
    public static int showYesNoQuestion(Component parentComponent, String titleId, String messageId, Object...args) {
    	return showConfirmationDialog(JOptionPane.YES_NO_OPTION, parentComponent, titleId, messageId, args);
    }

    /**
     * Shows a dialog with a yes, no and cancel button. The dialog further contains a title and a message.
     * @param parentComponent determines the <code>Frame</code>
     *		in which the dialog is displayed; if <code>null</code>,
     *		or if the <code>parentComponent</code> has no
     *		<code>Frame</code>, a default <code>Frame</code> is used
     * @param titleId the id of the title
     * @param messageId the id of the message
     * @param args optional arguments to be filled in the placeholders of the message
     * @return {@link #YES_OPTION}, {@link #NO_OPTION}, {@link #CANCEL_OPTION} or {@link #CLOSED_OPTION}.
     */
    public static int showYesNoCancelQuestion(Component parentComponent, String titleId, String messageId, Object...args) {
    	return showConfirmationDialog(JOptionPane.YES_NO_CANCEL_OPTION, parentComponent, titleId, messageId, args);
    }

    private static int showConfirmationDialog(int type, Component parentComponent, String titleId, String messageId, Object...args) {
    	TextResource tr = Factory.getInstance(TextResource.class);
    	String title = tr.getString(titleId);
    	String message = tr.getString(messageId, args);
    	return JOptionPane.showConfirmDialog(parentComponent, message, title, type);
    }

	private static void addStackTraceToLines(List<String> lines, Throwable t) {
    	while (t != null) {
    		lines.add(t.getClass().getName() + ": " + t.getMessage());
        	for (StackTraceElement ste : t.getStackTrace()) {
        		lines.add(ste.toString());
        	}

        	t = getCause(t);
    	}
	}

	private static Throwable getCause(Throwable t) {
		Throwable cause = t.getCause();
		if (cause == t) {
			cause = null;
		}
		return cause;
	}

	private static void showFormattedMessage(Component parentComponent, Object message, int messageType) {
		if (message instanceof Object[]) {
			message = truncateMessage((Object[]) message, 20);
		}

    	JOptionPane.showMessageDialog(parentComponent, message,
    			Factory.getInstance(TextResource.class).getString("gen.titleError"), messageType);
	}

	private static Object[] truncateMessage(Object[] message, int maxNrLines) {
		if (message.length > maxNrLines) {
			Object[] newMessage = new Object[maxNrLines];
			System.arraycopy(message, 0, newMessage, 0, maxNrLines-1);
			newMessage[maxNrLines-1] = "...";
			return newMessage;
		} else {
			return message;
		}
	}

}
