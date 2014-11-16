package nl.gogognome.lib.swing;

import java.awt.Component;
import java.awt.Insets;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;

public class TableWithLayoutableColumns extends JTable {

	private static final long serialVersionUID = 1L;

	public TableWithLayoutableColumns(TableModel tableModel) {
		super(tableModel);
	}

	@Override
	public void doLayout() {
		for (int columnIndex=0; columnIndex<getColumnCount(); columnIndex++) {
			TableColumn column = getColumnModel().getColumn(columnIndex);
			for (int row = 0; row < getRowCount(); row++) {
				if (column.getCellRenderer() != null) {
					Component c = prepareRenderer(column.getCellRenderer(), row, columnIndex);
					if (c instanceof JTextArea) {
						JTextArea a = (JTextArea) c;
						int h = getPreferredHeight(a) + getIntercellSpacing().height;
						if (getRowHeight(row) != h) {
							setRowHeight(row, h);
						}
					}
				}
			}
		}
		super.doLayout();
	}

	private int getPreferredHeight(JTextComponent c) {
		Insets insets = c.getInsets();
		View view = c.getUI().getRootView(c).getView(0);
		int preferredHeight = (int) view.getPreferredSpan(View.Y_AXIS);
		return preferredHeight + insets.top + insets.bottom;
	}
}
