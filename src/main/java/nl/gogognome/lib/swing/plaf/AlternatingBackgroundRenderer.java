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
package nl.gogognome.lib.swing.plaf;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * This class wraps a {@link TableCellRenderer}. It uses the wrapped renderer to obtain
 * a table cell renderer component and sets the background color of that component alternatingly.
 */
public class AlternatingBackgroundRenderer implements TableCellRenderer {

    /** Background color for the odd rows. */
    private final static Color COLOR_ODD_ROWS = new Color(240, 240, 255);

    /** The wrapped renderer. */
    private TableCellRenderer wrappedRenderer;

    /**
     * Constructor.
     * @param wrappedRenderer the wrapped renderer
     */
    public AlternatingBackgroundRenderer(TableCellRenderer wrappedRenderer) {
        this.wrappedRenderer = wrappedRenderer;
    }

    /**
     * @see TableCellRenderer#getTableCellRendererComponent(JTable, Object, boolean, boolean, int, int)
     */
    @Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = wrappedRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (!isSelected) {
            component.setBackground(row % 2 == 0 ? Color.WHITE : COLOR_ODD_ROWS);
        }
        return component;
    }

}