package nl.gogognome.lib.swing.table;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

public class MultilineCellRenderer extends JTextArea implements TableCellRenderer {

	private static final long serialVersionUID = 1L;

	public MultilineCellRenderer() {
		setLineWrap(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (isSelected) {
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}
		setFont(table.getFont());
		setText((value == null) ? "" : value.toString());
		return this;
	}
}
