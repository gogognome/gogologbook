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

import java.util.Comparator;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import nl.gogognome.lib.text.TextResource;
import nl.gogognome.lib.util.Factory;

/**
 * This class defines a column in a table.
 *
 * @author Sander Kooijmans
 */
public class ColumnDefinition {

    /** The identifier for the name of the column. */
    private String id;

    /** The class to which all values of the column belong. */
    private Class<?> classOfValues;

    /** The column width in pixels. */
    private int widthInPixels;

    /**
     * The table cell renderer for the column. <code>null</code> indicates that
     * the default table cell renderer must be used.
     */
    private TableCellRenderer tableCellRenderer;

    /**
     * The table cell editor for the column. <code>null</code> indicates that the
     * default table cell editor must be used.
     */
    private TableCellEditor tableCellEditor;

    /**
     * The comparator used to sort the values of this column.
     * <code>null</code> indicates that the default comparator must be used.
     */
    private Comparator<Object> comparator;

    /**
     * Constructor.
     * @param id the id of the column's name
     * @param classOfValues the class to which all values of the column belong
     * @param widthInPixels the column width in pixels
     */
    public ColumnDefinition(String id, Class<?> classOfValues,
            int widthInPixels) {
        super();
        this.id = id;
        this.classOfValues = classOfValues;
        this.widthInPixels = widthInPixels;
    }

    public String getName() {
        return Factory.getInstance(TextResource.class).getString(id);
    }

    public Class<?> getClassOfValues() {
        return classOfValues;
    }

    public int getWidthInPixels() {
        return widthInPixels;
    }

    public TableCellRenderer getTableCellRenderer() {
        return tableCellRenderer;
    }

    public TableCellEditor getTableCellEditor() {
		return tableCellEditor;
	}

    public Comparator<Object> getComparator() {
        return comparator;
    }

    /**
     * A builder for creating {@link ColumnDefinition}s.
     *
     * @author Sander Kooijmans
     */
    public static class Builder {

    	private ColumnDefinition columnDefinition;

        public Builder(String id, Class<?> classOfValues,
                int widthInPixels) {
        	columnDefinition = new ColumnDefinition(id, classOfValues, widthInPixels);
        }

        public Builder add(TableCellRenderer tableCellRenderer) {
        	columnDefinition.tableCellRenderer = tableCellRenderer;
        	return this;
        }

        public Builder add(TableCellEditor tableCellEditor) {
        	columnDefinition.tableCellEditor = tableCellEditor;
        	return this;
        }

        public Builder add(Comparator<Object> comparator) {
        	columnDefinition.comparator = comparator;
        	return this;
        }

        public ColumnDefinition build() {
        	return columnDefinition;
        }
    }
}
