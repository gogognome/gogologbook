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
package nl.gogognome.lib.swing.table;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

/**
 * This cell editor wraps a TableCellEditor such that
 * when the user starts editing a cell, its contents are selected.
 * This behavior makes it easier to simply replace the contents of
 * a cell by typing its new value.
 *
 * <p>The auto selection only works if the wrapped TableCellEditor
 * uses a JTextField as its component.
 *
 * @author Sander Kooijmans
 */
public class InitialValueSelectingCellEditor implements TableCellEditor {

	private TableCellEditor editor;

	public InitialValueSelectingCellEditor(TableCellEditor editor) {
		this.editor = editor;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		Component c = editor.getTableCellEditorComponent(table, value, isSelected,
				row, column);
		if (c instanceof JTextField) {
			((JTextField)c).selectAll();
		}
		return c;
	}

	@Override
	public Object getCellEditorValue() {
		return editor.getCellEditorValue();
	}

	@Override
	public boolean isCellEditable(EventObject anEvent) {
		return editor.isCellEditable(anEvent);
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		return editor.shouldSelectCell(anEvent);
	}

	@Override
	public boolean stopCellEditing() {
		return editor.stopCellEditing();
	}

	@Override
	public void cancelCellEditing() {
		editor.cancelCellEditing();
	}

	@Override
	public void addCellEditorListener(CellEditorListener l) {
		editor.addCellEditorListener(l);
	}

	@Override
	public void removeCellEditorListener(CellEditorListener l) {
		editor.removeCellEditorListener(l);
	}
}
