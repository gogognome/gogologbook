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

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 * This interface specifies a table model that is to be used by {@link SortedTable}s.
 */
public interface SortedTableModel extends TableModel {

    /**
     * Gets the width of the specified column.
     * @param column the column index
     * @return the width of the column in pixels
     */
    public int getColumnWidth(int column);

    /**
     * Gets the {@link TableCellRenderer} for the specified column.
     * If no specific renderer is needed, then <code>null</code> should be returned.
     * @param column the column index
     * @return the renderer or <code>null</code>
     */
    public TableCellRenderer getRendererForColumn(int column);

    /**
     * Gets the {@link TableCellEditor} for the specified column.
     * If no specific editor is needed, then <code>null</code> should be returned.
     * @param column the column index
     * @return the editor or <code>null</code>
     */
    public TableCellEditor getEditorForColumn(int column);

    /**
     * Gets the comparator for the specified column.
     * @param column the column index
     * @return the comparator or <code>null</code> if the default comparator must be used.
     */
    public Comparator<Object> getComparator(int column);
}
