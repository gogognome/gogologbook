package nl.gogognome.lib.text;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;

import nl.gogognome.lib.text.TextWrapper.TextWidthCalculator;

import org.junit.Test;

import com.google.common.collect.Lists;

public class TextWrapperTest {

	private final TextWidthCalculator lengthCalculactor = new TextWidthCalculator() {
		@Override
		public int getWidthOfLine(String line) {
			return line.length();
		}
	};

	@Test
	public void wrapEmptyList() {
		TextWrapper textWrapper = new TextWrapper(10, lengthCalculactor);
		List<String> wrappedText = textWrapper.getWrappedText(Collections.<String> emptyList());
		assertTrue(wrappedText.isEmpty());
	}

	@Test
	public void wrapSingleLineThatFits() {
		TextWrapper textWrapper = new TextWrapper(10, lengthCalculactor);
		List<String> wrappedText = textWrapper.getWrappedText(Lists.newArrayList("Ha ha!"));
		assertEquals(Lists.newArrayList("Ha ha!"), wrappedText);
	}

	@Test
	public void wrapSingleLineThatFitsIn2Lines() {
		TextWrapper textWrapper = new TextWrapper(3, lengthCalculactor);
		List<String> wrappedText = textWrapper.getWrappedText(Lists.newArrayList("Ha ha!"));
		assertEquals(Lists.newArrayList("Ha", "ha!"), wrappedText);
	}

	@Test
	public void wrapSingleLineThatFitsIn3Lines() {
		TextWrapper textWrapper = new TextWrapper(3, lengthCalculactor);
		List<String> wrappedText = textWrapper.getWrappedText(Lists.newArrayList("Ha hi ho!"));
		assertEquals(Lists.newArrayList("Ha", "hi", "ho!"), wrappedText);
	}

}
