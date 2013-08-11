package nl.gogognome.lib.text;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class TextWrapper {

	private final int maxLineWidth;

	private final TextWidthCalculator textWidthCalculator;

	private List<String> wrappedLines;

	private final Joiner joiner = Joiner.on(' ');

	public interface TextWidthCalculator {
		public int getWidthOfLine(String line);
	}

	/**
	 * @param maxLineWidth the maximum line width
	 * @param textWidthCalculator calculates the width for a string
	 */
	public TextWrapper(int maxLineWidth, TextWidthCalculator textWidthCalculator) {
		this.maxLineWidth = maxLineWidth;
		this.textWidthCalculator = textWidthCalculator;
	}

	/**
	 * Wraps lines of text. Each line will have a size equal to or less than maxLineWidth. The width of the lines is calculated
	 * using the textWidthCalculator. 
	 * @param linesToBeWrapped the lines to be wrapped
	 * @return the wrapped lines
	 */
	public List<String> getWrappedText(Iterable<String> linesToBeWrapped) {
		wrappedLines = Lists.newArrayList();
		for (String lineToWrap : linesToBeWrapped) {
			int totalWidth = textWidthCalculator.getWidthOfLine(lineToWrap);
			if (totalWidth <= maxLineWidth) {
				wrappedLines.add(lineToWrap);
			} else {
				wrapSingleLine(Lists.newArrayList(lineToWrap.split(" ")), totalWidth);
			}
		}
		return wrappedLines;
	}

	private void wrapSingleLine(List<String> words, int totalWidth) {
		int nrWords = maxLineWidth * words.size() / totalWidth;
		for (int startIndex = 0; startIndex < words.size(); startIndex += nrWords) {
			if (wordsFit(words, startIndex, nrWords)) {
				while (startIndex + nrWords < words.size() && wordsFit(words, startIndex, nrWords + 1)) {
					nrWords++;
				}
			} else {
				while (nrWords > 0 && !wordsFit(words, startIndex, nrWords - 1)) {
					nrWords--;
				}
			}
			wrappedLines.add(joiner.join(words.subList(startIndex, startIndex + nrWords)));
		}
	}

	private boolean wordsFit(List<String> words, int startIndex, int nrWords) {
		String partOfLine = joiner.join(words.subList(startIndex, startIndex + nrWords));
		return textWidthCalculator.getWidthOfLine(partOfLine) <= maxLineWidth;
	}
}
