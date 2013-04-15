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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Action;
import javax.swing.JTable;

import nl.gogognome.lib.gui.Closeable;

/**
 * This class will execute a specified Action when the user
 * selects a table row. A row is selected by pressing enter
 * when the table has focus and a row has been highlighted,
 * or by double clicking a row.
 *
 * Usage: after creation of a TableRowSelectAction instance,
 * call registerListeners(). When finished, call
 * unregisterListeners(). This class implements the
 * Closeable interface. Instead of calling unregisterListeners()
 * the close() method can be called.
 *
 * @author Sander Kooijmans
 */
public class TableRowSelectAction implements Closeable {

	private final JTable table;
	private final KeyListener keyListener;
	private final MouseListener mouseListener;
	private final Action action;
	private boolean registered;

	public TableRowSelectAction(JTable table, Action action) {
		super();
		this.table = table;
		this.action = action;
		this.keyListener = new KeyListenerImpl();
		this.mouseListener = new MouseListenerImpl();
	}

	/**
	 * Adds listeners to the table. The listeners will execute the action
	 * when the user selects a row in the table.
	 */
	public void registerListeners() {
		if (!registered) {
			table.addKeyListener(keyListener);
			table.addMouseListener(mouseListener);
			registered = true;
		}
	}

	public void deregisterListeners() {
		if (registered) {
			registered = false;
			table.removeKeyListener(keyListener);
			table.removeMouseListener(mouseListener);
		}
	}

	@Override
	public void close() {
		deregisterListeners();
	}

	private class KeyListenerImpl extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                action.actionPerformed(null);
            }
        }
	}

	private class MouseListenerImpl extends MouseAdapter {
        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getClickCount() == 2) {
                action.actionPerformed(null);
            }
        }
	}
}
