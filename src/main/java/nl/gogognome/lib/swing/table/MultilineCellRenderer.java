package nl.gogognome.lib.swing.table;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

public class MultilineCellRenderer extends JTextArea implements TableCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		setText((String) value);
		setWrapStyleWord(true);
		setLineWrap(true);

		int height = calcRowHeight();
		table.setRowHeight(row, height);

		return this;
	}

	private int calcRowHeight() {
		int fontHeight = getFontMetrics(getFont()).getHeight();
		int lines = getLineCount();
		int height = fontHeight * lines;
		return height;
	}

}
