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
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JTable;

/**
 * This class offers a variety of methods that are useful when writing
 * Swing applications.
 *
 * @author Sander Kooijmans
 */
public class SwingUtils {

    public static GridBagConstraints createGBConstraints(
            int gridx, int gridy,
            int gridwidth, int gridheight, double weightx, double weighty,
            int anchor, int fill, Insets insets) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        gbc.anchor = anchor;
        gbc.fill = fill;
        gbc.insets = insets;
        return gbc;
    }

    public static GridBagConstraints createGBConstraints(
            int gridx, int gridy,
            int gridwidth, int gridheight, double weightx, double weighty,
            int anchor, int fill, int top, int left, int bottom, int right) {

        return createGBConstraints(gridx, gridy, gridwidth, gridheight, weightx,
                weighty, anchor, fill, new Insets(top, left, bottom, right));
    }

    public static GridBagConstraints createGBConstraints(
            int gridx, int gridy) {

        return createGBConstraints(gridx, gridy, 1, 1, 1.0,
                1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                0, 0, 0, 0);
    }

    public static GridBagConstraints createGBConstraints(
            int gridx, int gridy, int gridwidth, int gridheight) {

        return createGBConstraints(gridx, gridy, gridwidth, gridheight, 1.0,
                1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                0, 0, 0, 0);
    }


    public static GridBagConstraints createTextFieldGBConstraints(
            int gridx, int gridy) {
        return createGBConstraints(gridx, gridy, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                gridy == 0 ? 0 : 3, gridx == 0 ? 0 : 3, 0, 0);
    }

    public static GridBagConstraints createLabelGBConstraints(
            int gridx, int gridy) {
        return createGBConstraints(gridx, gridy, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                gridy == 0 ? 0 : 3, gridx == 0 ? 0 : 3, 0, 0);
    }

    public static GridBagConstraints createPanelGBConstraints(
            int gridx, int gridy) {
        return createGBConstraints(gridx, gridy, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                gridy == 0 ? 0 : 10, gridx == 0 ? 0 : 10, 10, 10);
    }

    /**
     * gets the top level container of a component.
     * @param component the component
     * @return its top level container or null if the component has no parent
     */
	public static Container getTopLevelContainer(Component component) {
		Container parent = component.getParent();
		if (parent == null || (component instanceof Dialog)) {
			return (Container) component;
		} else {
			return getTopLevelContainer(parent);
		}
	}

	/**
	 * Gets coordinates of a component as screen locations.
	 * @param c the component
	 * @return the coordinates
	 */
	public static Point getCoordinatesRelativeToTopLevelContainer(Component c) {
		Point p = c.getLocation();
		while (c.getParent() != null && !(c.getParent() instanceof Window)) {
			c = c.getParent();
			Point parentLocation = c.getLocation();
			p.translate(parentLocation.x, parentLocation.y);
		}
		return p;
	}

	/**
	 * Gets the selected rows of a (sorted) table. The returned row indices
	 * are converted to model indices.
	 * @param table the table
	 * @return the row indices
	 */
	public static int[] getSelectedRowsConvertedToModel(JTable table) {
		int[] rows = table.getSelectedRows();
		for (int i = 0; i < rows.length; i++) {
			rows[i] = table.convertRowIndexToModel(rows[i]);
		}
		return rows;
	}

	/**
	 * Gets the index of the first selected row of a (sorted) table.
	 * The returned row indix is converted to a model index.
	 * @param table the table
	 * @return the row index or -1 if no row is selected
	 */
	public static int getSelectedRowConvertedToModel(JTable table) {
		int row = table.getSelectedRow();
		if (row != -1) {
			row = table.convertRowIndexToModel(row);
		}
		return row;
	}

	/**
	 * Selects the first row of the table. If the table is empty,
	 * then this method has no effect.
	 * @param table the table
	 */
	public static void selectFirstRow(JTable table) {
		if (table.getRowCount() > 0) {
			table.getSelectionModel().setSelectionInterval(0, 0);
		}
	}

	/**
	 * Selects the specified row of the (sorted) table. The row index is
	 * specified as an index of the table model. The corresponding
	 * row in the table is selected.
	 * @param table the table
	 * @param row the row index of the model
	 */
	public static void selectRowWithModelIndex(JTable table, int row) {
        row = table.convertRowIndexToView(row);
        table.getSelectionModel().setSelectionInterval(row, row);
	}

	/**
	 * Puts the window in the center of the screen.
	 * @param window the window
	 */
	public static void center(Window window) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screenSize.width - window.getWidth()) / 2;
		int y = (screenSize.height - window.getHeight()) / 2;
		window.setLocation(x, y);
	}
}
