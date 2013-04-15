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

import java.awt.Dialog;
import java.awt.Frame;

import nl.gogognome.lib.swing.views.View;


/**
 * This class implements a dialog with two buttons: OK and Cancel.
 *
 * <p>Subclasses must implement the <tt>handleOk()</tt> method and should
 * implement <tt>handleCancel()</tt> as well. Subclasses must not (and can not)
 * implement or call the method <tt>handleButton(int)</tt>.
 *
 * @author Sander Kooijmans
 */
public abstract class OkCancelDialog extends DialogWithButtons {

    /** Indicates whether the dialog was exited with the Ok button. */
    private boolean exitedWithOk;

	/**
	 * Creates a dialog with one or more buttons.
	 * @param frame the frame that owns this dialog.
	 * @param titleId the id of the title string.
	 * @param buttonIds the ids of the buttons.
	 */
	protected OkCancelDialog(Frame frame, String titleId) {
		super(frame, titleId, BT_OK_CANCEL);
	}

	/**
	 * Creates a dialog with one or more buttons.
	 * @param dialog the dialog that owns this dialog.
	 * @param titleId the id of the title string.
	 * @param buttonIds the ids of the buttons.
	 */
	protected OkCancelDialog(Dialog dialog, String titleId) {
		super(dialog, titleId, BT_OK_CANCEL);
	}

    /**
     * Creates a dialog with one or more buttons.
     * @param view the view that owns this dialog.
     * @param titleId the id of the title string.
     * @param buttonIds the ids of the buttons.
     */
    protected OkCancelDialog(View view, String titleId) {
        super(view, titleId, BT_OK_CANCEL);
    }

	/**
	 * Handles the button-pressed event. This method is called when one of the buttons
	 * has been pressed by the user.
	 *
	 * @param index the index of the button: 0 for Ok, 1 for Cancel.
	 */
	@Override
	final protected void handleButton(int index) {
		if (index == 0) {
            exitedWithOk = true;
			handleOk();
		} else {
			handleCancel();
		}
	}

	/**
	 * Handles the OK event.
	 */
	protected abstract void handleOk();

    /**
     * Checks whether the dialog was exited with the Ok button.
     * @return <code>true</code> if the dialog was exited with the Ok button;
     *         <code>false</code> oterhwise
     */
    public boolean isExitedWithOk() {
        return exitedWithOk;
    }
}
