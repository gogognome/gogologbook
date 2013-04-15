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
import java.awt.event.KeyEvent;
import java.text.ParseException;

import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SpinnerModel;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;


/**
 * TableCellEditor containing a spinner.
 *
 * @author Sander Kooijmans
 */
public class SpinnerTableCellEditor extends AbstractCellEditor implements TableCellEditor {

	private JSpinner spinner;

	public SpinnerTableCellEditor(SpinnerModel spinnerModel) {
		spinner = new AdaptedSpinner(spinnerModel);
	}

	@Override
	public Object getCellEditorValue() {
		return spinner.getValue();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		spinner.setValue(value);
		selectAllText();
		return spinner;
	}

	private void selectAllText() {
		getSpinnerTextField().selectAll();
	}

	@Override
	public boolean stopCellEditing() {
		try {
			spinner.commitEdit();
			fireEditingStopped();
			return true;
		} catch (ParseException e) {
			// input is invalid
			return false;
		}
	}

	private JFormattedTextField getSpinnerTextField() {
		return ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
	}
}

/**
 * This class implements a trick found at http://jroller.com/santhosh/entry/keyboard_handling_in_tablecelleditor
 *
 * <p>When the user starts editing by typing, then the first key event does not end at the editor
 * of the spinner, but it is swallowed by the spinner component itself.
 *
 * <p>This spinner extension forwards the first key event to its editor.
 * After the first key has been handled, the editor gets focus. Subsequent key events are therefore
 * forwarded to the editor.
 *
 * @author Sander Kooijmans
 */
class AdaptedSpinner extends JSpinner {

	public AdaptedSpinner(SpinnerModel spinnerModel) {
		 super(spinnerModel);
	}

	@Override
	public void addNotify(){
        super.addNotify();
        getTextField().requestFocus();
    }

	private JFormattedTextField getTextField() {
		return ((JSpinner.DefaultEditor) getEditor()).getTextField();
	}

    @Override
	protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
    	if (isFocusOwner()) {
    		return super.processKeyBinding(ks, e, condition, pressed);
    	} else {
    		return editorProcessKeyBinding(ks, e, condition, pressed);
    	}
     }

    private boolean editorProcessKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
        InputMap map = getTextField().getInputMap(condition);
        ActionMap am = getTextField().getActionMap();

        if (map != null && am != null && isEnabled()) {
            Object binding = map.get(ks);
            Action action = (binding==null) ? null : am.get(binding);
            if (action != null) {
            	return SwingUtilities.notifyAction(action, ks, e, getTextField(), e.getModifiers());
            }
        }
        return false;
    }
}