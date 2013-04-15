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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.HashSet;

import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import nl.gogognome.lib.swing.DateRenderer;
import nl.gogognome.lib.swing.InitialValueSelectingCellEditor;

/**
 * The default look and feel for a table.
 */
public class DefaultTableUI extends BasicTableUI {

    public static ComponentUI createUI(JComponent c) {
        return new DefaultTableUI();
    }

    @Override
	public void installUI(JComponent c) {
        super.installUI(c);
        final JTable table = (JTable) c;

        table.setDefaultRenderer(Date.class, new DateRenderer());

        installInitialValueSelectingCellEditors(table);
        installAlternatingBackgroundRenderers(table);

        // Add listener that installs the alternating background renderers each time the model
        // of the table changes (not when the contents of the model change, but when a new model
        // is set in the table).
        table.addPropertyChangeListener("model", new PropertyChangeListener() {
            @Override
			public void propertyChange(PropertyChangeEvent evt) {
                installAlternatingBackgroundRenderers(table);
            }
        });
    }

    private static void installInitialValueSelectingCellEditors(JTable table) {
    	installInitialValueSelectingCellEditorsForClass(String.class, table);
    	installInitialValueSelectingCellEditorsForClass(Integer.class, table);
    	installInitialValueSelectingCellEditorsForClass(Long.class, table);
    	installInitialValueSelectingCellEditorsForClass(Float.class, table);
    	installInitialValueSelectingCellEditorsForClass(Double.class, table);
	}

	private static void installInitialValueSelectingCellEditorsForClass(Class<?> clazz, JTable table) {
		TableCellEditor editor = table.getDefaultEditor(clazz);
		editor = new InitialValueSelectingCellEditor(editor);
		table.setDefaultEditor(clazz, editor);
	}

	@Override
	protected void installKeyboardActions() {
        super.installKeyboardActions();
        InputMap inputMap = table.getInputMap();
        inputMap.remove(KeyStroke.getKeyStroke("ENTER"));
    }

    /**
     * Install alternating background renderer for the columns.
     * @param table the table for which the renderers must be installed
     */
    private static void installAlternatingBackgroundRenderers(JTable table) {
        HashSet<Class<?>> classes = new HashSet<Class<?>>();
        for (int col=0; col<table.getColumnCount(); col++) {
            Class<?> clazz = table.getColumnClass(col);
            if (!classes.contains(clazz)) {
                classes.add(clazz);
                TableCellRenderer renderer = table.getDefaultRenderer(clazz);
                table.setDefaultRenderer(clazz, new AlternatingBackgroundRenderer(renderer));
            }

            table.getColumnModel().getColumn(col).addPropertyChangeListener(new PropertyChangeListener() {
                @Override
				public void propertyChange(PropertyChangeEvent evt) {
                    if ("cellRenderer".equals(evt.getPropertyName())) {
                        if (!(evt.getNewValue() instanceof AlternatingBackgroundRenderer)) {
                            ((TableColumn) evt.getSource()).setCellRenderer(
                                new AlternatingBackgroundRenderer((TableCellRenderer)evt.getNewValue()));
                        }
                    }
                }
            });
        }
    }
}
