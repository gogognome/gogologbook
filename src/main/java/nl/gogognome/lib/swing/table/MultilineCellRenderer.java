package nl.gogognome.lib.swing.table;

import java.awt.Component;
import java.awt.FontMetrics;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

import nl.gogognome.lib.text.TextWrapper;
import nl.gogognome.lib.text.TextWrapper.TextWidthCalculator;

import com.google.common.base.Joiner;

public class MultilineCellRenderer extends JTextArea implements TableCellRenderer {

	private static final long serialVersionUID = 1L;
	private TextWidthCalculator textWidthCalculator;
	private final Joiner joiner = Joiner.on('\n');
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		initLayout(table, isSelected);
		  
		int width = table.getColumnModel().getColumn(column).getWidth() - 1;
		setText(wrapText(width, (String) value));
		setWrapStyleWord(true);
		setLineWrap(true);

		int height = calcRowHeight();
		table.setRowHeight(row, height);
		return this;
	}

	private void initLayout(JTable table, boolean isSelected) {
		if (isSelected) {
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}
	}

	private String wrapText(int width, String text) {
		List<String> lines = Arrays.asList((text).split("\n"));
		List<String> wrappedLines = new TextWrapper(width, getTextWidthCalculator()).getWrappedText(lines);
		return joiner.join(wrappedLines);
	}

	private TextWidthCalculator getTextWidthCalculator() {
		if (textWidthCalculator == null) {
			FontMetrics fontMetrics = getFontMetrics(getFont());
			textWidthCalculator = new TextWidthCalculatorImpl(fontMetrics); 
		}
		return textWidthCalculator;
	}

	private int calcRowHeight() {
		FontMetrics fontMetrics =getFontMetrics(getFont());
		int fontHeight = fontMetrics.getHeight();
		int lines = getLineCount();
		int height = fontHeight * lines;
		return height;
	}

}

class TextWidthCalculatorImpl implements TextWidthCalculator {
	
	private final FontMetrics fontMetrics;
	
	
	public TextWidthCalculatorImpl(FontMetrics fontMetrics) {
		this.fontMetrics = fontMetrics;
	}

	@Override
	public int getWidthOfLine(String line) {
		return fontMetrics.stringWidth(line);
	}
	
}
