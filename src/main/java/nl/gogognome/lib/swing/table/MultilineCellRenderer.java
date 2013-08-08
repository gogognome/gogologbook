package nl.gogognome.lib.swing.table;

import java.awt.Component;
import java.awt.FontMetrics;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

import com.google.common.collect.Lists;

public class MultilineCellRenderer extends JTextArea implements TableCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		int width = table.getColumnModel().getColumn(column).getWidth();
		FontMetrics fontMetrics = getFontMetrics(getFont());
		setText(wrapText((String) value, width, fontMetrics));
		setWrapStyleWord(true);
		setLineWrap(true);

		int height = calcRowHeight(fontMetrics);
		table.setRowHeight(row, height);
		return this;
	}

	private String wrapText(String text, int width, FontMetrics fontMetrics) {
		StringBuilder sb = new StringBuilder(text.length() * 4 / 3);
		LinkedList<String> remainingLines = Lists.newLinkedList(Arrays.asList(text.split("\n")));
		while (!remainingLines.isEmpty()) {
			String line = remainingLines.removeFirst();
			int index = line.length();
			while (fontMetrics.stringWidth(line.substring(0, index)) > width && index > 1) {
				index--;
			}
			sb.append(line, 0, index);

			if (index < line.length()) {
				remainingLines.addFirst(line.substring(index));
			}

			if (!remainingLines.isEmpty()) {
				sb.append('\n');
			}
		}
		return sb.toString();
	}

	private int calcRowHeight(FontMetrics fontMetrics) {
		int fontHeight = fontMetrics.getHeight();
		int lines = getLineCount();
		int height = fontHeight * lines;
		return height;
	}

}
