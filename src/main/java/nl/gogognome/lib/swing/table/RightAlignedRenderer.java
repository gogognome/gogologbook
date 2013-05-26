/*
    This file is part of gogo account.

    gogo account is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    gogo account is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with gogo account.  If not, see <http://www.gnu.org/licenses/>.
*/
package nl.gogognome.lib.swing.table;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Table cell renderer that right aligns its contents.
 *
 * @author Sander Kooijmans
 */
public class RightAlignedRenderer extends DefaultTableCellRenderer {
    @Override
	public void setValue(Object value) {
        super.setValue(value);
        setHorizontalAlignment(SwingConstants.RIGHT);
    }
}
