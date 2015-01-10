package nl.gogognome.lib.text;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;


public class StringMatcherTest {

	@Test
	public void searchInEmptyString() {
		assertEquals(-1, new StringMatcher("test").match(""));
		assertEquals(-1, new StringMatcher("test", true).match(""));
		assertEquals(-1, new StringMatcher("test", false).match(""));
	}
	
	@Test
	public void searchInNull() {
		assertEquals(-1, new StringMatcher("test").match(null));
		assertEquals(-1, new StringMatcher("test", true).match(null));
		assertEquals(-1, new StringMatcher("test", false).match(null));
	}

}
