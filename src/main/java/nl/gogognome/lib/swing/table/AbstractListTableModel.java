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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Abstract class for table model implementations. This class
 * uses ColumnDefinitions to define the table columns. In addition it stores
 * the rows of the table model in a list. This class offers methods to modify the list.
 * Modifications of the list are signaled the registered TableModelListeners.
 *
 * @param <T> the type of rows
 * @author Sander Kooijmans
 */
public abstract class AbstractListTableModel<T> extends AbstractTableModelWithColumnDefinitions {
	private List<T> rows;

    /**
     * Constructor.
     * @param columnDefinitions the column definitions
     * @param initialRows the initial rows
     */
    public AbstractListTableModel(List<ColumnDefinition> columnDefinitions, List<T> initialRows) {
        super(columnDefinitions);
        rows = new ArrayList<T>(initialRows);
    }

	@Override
	public int getRowCount() {
		return rows.size();
	}

	/**
	 * Adds a row to the table model.
	 * Notifies all listeners about the change in the table.
	 * This method must be called from the AWT event thread.
	 * @param row the row
	 */
	public void addRow(T row) {
		rows.add(row);
		fireTableRowsInserted(rows.size() - 1, rows.size() - 1);
	}

	/**
	 * Removes a row from the table model.
	 * Notifies all listeners about the change in the table.
	 * This method must be called from the AWT event thread.
	 * @param index the index of the row
	 */
	public void removeRow(int index) {
		rows.remove(index);
		fireTableRowsDeleted(index, index);
	}

	/**
	 * Removes a number of rows from the table model.
	 * Notifies all listeners about the change in the table.
	 * This method must be called from the AWT event thread.
	 * @param index the index of the row
	 */
	public void removeRows(int[] indices) {
        Arrays.sort(indices);
        for (int i=indices.length-1; i >= 0; i--) {
        	rows.remove(i);
        }
        fireTableDataChanged();
	}

	/**
	 * Removes all rows from the table model.
	 * Notifies all listeners about the change in the table.
	 * This method must be called from the AWT event thread.
	 */
	public void clear() {
		int oldSize = rows.size();
		rows.clear();
		fireTableDataChanged();
	}

	/**
	 * Updates a row.
	 * Notifies all listeners about the change in the table.
	 * This method must be called from the AWT event thread.
	 * @param index the index of the row
	 * @param row the new value of the row
	 */
	public void updateRow(int index, T row) {
		rows.set(index, row);
		fireTableRowsUpdated(index, index);
	}

	/**
	 * Updates all rows of the table.
	 * @param newRows the new rows
	 */
	public void replaceRows(List<T> newRows) {
		rows.clear();
		rows.addAll(newRows);
		fireTableDataChanged();
	}

	/**
	 * Gets a row.
	 * @param index the index of the row
	 * @return the row
	 */
	public T getRow(int index) {
		return rows.get(index);
	}

	/**
	 * Gets the rows of the table.
	 *
	 * @return an unmodifiable list of rows
	 */
	public List<T> getRows() {
		return Collections.unmodifiableList(rows);
	}
}
