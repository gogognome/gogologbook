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

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JComboBox;

import nl.gogognome.lib.gui.Closeable;
import nl.gogognome.lib.text.StringMatcher;

/**
 * This class extends the standard combo box implementation with keyboard input:
 * the user can select an item by typing a substring of the items name.
 *
 * @author Sander Kooijmans
 */
public class JComboBoxWithKeyboardInput extends JComboBox implements KeyListener, FocusListener, Closeable {

	private static final long serialVersionUID = 1L;

	private final ArrayList<String> itemStrings = new ArrayList<String>();

	private final StringBuilder textEnteredByUser = new StringBuilder();

	public JComboBoxWithKeyboardInput() {
		super();
		addKeyListener(this);
		addFocusListener(this);
	}

	@Override
	public void close() {
		removeKeyListener(this);
		removeFocusListener(this);
	}

	@Override
	public void addItem(Object item) {
		addItemWithStringRepresentation(item, item.toString());
	}

	protected void addItemWithStringRepresentation(Object item, String representation) {
		super.addItem(item);
		itemStrings.add(representation);
	}

	@Override
	public void removeAllItems() {
		super.removeAllItems();
		itemStrings.clear();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		char c = e.getKeyChar();
		if (Character.isLetterOrDigit(c)) {
			textEnteredByUser.append(Character.toLowerCase(c));
			selectItemWithSubstring(textEnteredByUser.toString());
		} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			if (textEnteredByUser.length() > 0) {
				textEnteredByUser.deleteCharAt(textEnteredByUser.length() - 1);
				selectItemWithSubstring(textEnteredByUser.toString());
			}
		}
	}

	/**
	 * Selects the first item in the list that has the specified string as substring. If the specified string is not a substring of any item, then
	 * the currently selected item stays selected.
	 *
	 * @param s the string that should be matched as substring.
	 */
	private void selectItemWithSubstring(String s) {
		StringMatcher matcher = new StringMatcher(s, true);
		for (int i = 0; i < itemStrings.size(); i++) {
			String text = itemStrings.get(i);
			if (text != null && matcher.match(text) != -1) {
				setSelectedIndex(i);
				return;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// ignore this event
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// ignore this event
	}

	@Override
	public void focusGained(FocusEvent event) {
		textEnteredByUser.delete(0, textEnteredByUser.length());
	}

	@Override
	public void focusLost(FocusEvent event) {
		// ignore
	}
}
