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

import java.util.Comparator;
import java.util.List;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * Abstract class for table model implementations. This class
 * uses ColumnDefinitions to define the table columns.
 *
 * @author Sander Kooijmans
 */
public abstract class AbstractTableModel extends javax.swing.table.AbstractTableModel {

    /** The column definitions. */
    private List<ColumnDefinition> columnDefinitions;

    /**
     * Constructor.
     * @param columnDefinitions the column definitions
     */
    public AbstractTableModel(List<ColumnDefinition> columnDefinitions) {
        super();
        this.columnDefinitions = columnDefinitions;
    }

    @Override
    public String getColumnName(int column) {
        return columnDefinitions.get(column).getName();
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return columnDefinitions.get(column).getClassOfValues();
    }

	public int getColumnWidth(int column) {
        return columnDefinitions.get(column).getWidthInPixels();
    }

	public Comparator<Object> getComparator(int column) {
        return columnDefinitions.get(column).getComparator();
    }

	public TableCellRenderer getRendererForColumn(int column) {
        return columnDefinitions.get(column).getTableCellRenderer();
    }

	public TableCellEditor getEditorForColumn(int column) {
        return columnDefinitions.get(column).getTableCellEditor();
    }

    @Override
	public int getColumnCount() {
        return columnDefinitions.size();
    }

    /**
     * Gets the column definition for the specified column.
     * @param column the index of the column
     * @return the column definition
     */
    public ColumnDefinition getColumnDefinition(int column) {
        return columnDefinitions.get(column);
    }

}
