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
package nl.gogognome.lib.swing.plaf;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextFieldUI;

/**
 * Default UI for a text field.
 */
public class DefaultTextFieldUI extends BasicTextFieldUI {

    private FocusListener focusListener;

    public static ComponentUI createUI(JComponent c) {
        return new DefaultTextFieldUI();
    }

    @Override
	public void installUI(JComponent c) {
        super.installUI(c);
        c.setFocusable(true);

        final JTextField textField = (JTextField) c;
        focusListener = new FocusListener() {

            @Override
			public void focusGained(FocusEvent e) {
                textField.selectAll();
            }

            @Override
			public void focusLost(FocusEvent e) {
                textField.select(0, 0);
            }

        };
        c.addFocusListener(focusListener);
    }

    @Override
	public void uninstallUI(JComponent c) {
        c.removeFocusListener(focusListener);
        super.uninstallUI(c);
    }
}
