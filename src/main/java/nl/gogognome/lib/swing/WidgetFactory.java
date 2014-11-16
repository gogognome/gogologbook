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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import nl.gogognome.lib.swing.table.AbstractTableModelWithColumnDefinitions;
import nl.gogognome.lib.swing.table.ColumnDefinition;
import nl.gogognome.lib.text.TextResource;
import nl.gogognome.lib.util.Factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is a factory for buttons, menus, menu items, text fields and
 * check boxes.
 *
 * @author kooijmas
 */
public class WidgetFactory {

	private final static Logger LOGGER = LoggerFactory.getLogger(WidgetFactory.class);

	/** The <code>TextResource</code> used to obtain string resources. */
	private final TextResource textResource;

	private final Map<String, Icon> iconCache = new HashMap<String, Icon>();
	private final Map<String, Image> imageCache = new HashMap<String, Image>();

	public WidgetFactory(TextResource textResource)
	{
		this.textResource = textResource;
	}

	/**
	 * Creates an <code>Action</code> for the specified identifier.
	 * @param id the identifier
	 * @return the <code>Action</code> or <code>null</code> if the specified
	 *          identifier does not occur in the resources.
	 */
	public ActionWrapper createAction(String id) {

	    String name = textResource.getString(id);

	    ActionWrapper action;
	    if (name != null) {
		    Icon icon = createIcon(id + ".icon16");
	        if (icon != null) {
	            action = new ActionWrapper(name, icon);
	        } else {
	            action = new ActionWrapper(name);
	        }
	    } else {
	        action = new ActionWrapper();
	    }

	    String s = textResource.getString(id + ".accelerator");
	    KeyStroke accelerator = s != null ? KeyStroke.getKeyStroke(s) : null;
	    if (accelerator != null) {
	        action.putValue(Action.ACCELERATOR_KEY, accelerator);
	    }

	    s = textResource.getString(id + ".mnemonic");
	    int mnemonic = getMnemonic(id);
	    if (mnemonic != -1) {
	        action.putValue(Action.MNEMONIC_KEY, new Integer(mnemonic));
	    }

	    s = textResource.getString(id + ".tooltip");
	    if (s != null) {
	        action.putValue(Action.SHORT_DESCRIPTION, s);
	    }

	    s = textResource.getString(id + ".contexthelp");
	    if (s != null) {
	        action.putValue(Action.LONG_DESCRIPTION, s);
	    }

	    return action;
	}

	/**
	 * Creates a button.
	 * @param id the id of the button's description in the resources.
	 * @param the action to be performed when the button is pressed
	 * @return the button.
	 */
	public JButton createButton(String id, Action action) {
	    ActionWrapper actionWrapper = createAction(id);
	    actionWrapper.setAction(action);

		return new JButton(actionWrapper);
	}

	/**
	 * Creates a button containing an icon only.
	 * @param id the id of the button's description in the resources.
	 * @param the action to be performed when the button is pressed
	 * @param size the width and height in pixels
	 * @return the button.
	 */
	public JButton createIconButton(String id, Action action, int size) {
	    JButton button = createButton(id, action);
	    button.setText(null);
	    button.setPreferredSize(new Dimension(size, size));
		return button;
	}

	/**
	 * Creates a label with the specified text.
	 *
	 * @param id the id of the text.
	 * @return the label.
	 */
	public JLabel createLabel(String id) {
		return createLabel(id, null);
	}

    /**
     * Creates a label with the specified text. If <code>component != null</code> then this
     * component receives the focus after the user entered the mnemonic for this label.
     *
     * @param id the id of the text.
     * @param component the component that receives focus after the mnemonic for the
     *        label has been entered
     * @return the label.
     */
    public JLabel createLabel(String id, JComponent component) {
        JLabel label = new JLabel(textResource.getString(id));

        String s = textResource.getString(id + ".mnemonic");
        if (s != null && s.length() == 1 && component != null) {
            label.setDisplayedMnemonic(s.charAt(0));
            label.setLabelFor(component);
        }

        return label;
    }

	/**
	 * Creates a text field. When the created text field gains focus, its
	 * contents is selected.
	 *
	 * @return the text field.
	 */
	public JTextField createTextField() {
		JTextField textField = new JTextField();
		return textField;
	}

	/**
	 * Creates a text field. When the created text field gains focus, its
	 * contents is selected.
	 *
	 * @param columns the number of columns of the text field.
	 * @return the text field.
	 */
	public JTextField createTextField(int columns)
	{
		JTextField textField = new JTextField(columns);
        textField.setFocusable(true);
		return textField;
	}

	/**
	 * Creates a text field. When the created text field gains focus, its
	 * contents is selected.
	 *
	 * @param text the initial text of the text field.
	 * @return the text field.
	 */
	public JTextField createTextField(String text)
	{
		JTextField textField = new JTextField(text);
		return textField;
	}

    /**
     * Creates a check box.
     *
     * @param textId the id of the check box' text.
     * @param checked the initial state of the check box.
     * @return the check box.
     */
    public JCheckBox createCheckBox(String textId, boolean checked) {
        JCheckBox checkBox = new JCheckBox(
            textResource.getString(textId), checked);
        return checkBox;
    }

	/**
	 * Creates a <tt>JMenu</tt> with a specified string as label.
	 * @param id the id of the string.
	 * @param keyEvent the key event assoicated with the mnemonic.
	 * @return the menu.
	 */
	public JMenu createMenu(String id)
	{
		JMenu result = new JMenu(textResource.getString(id));
		int mnemonic = getMnemonic(id);
		if (mnemonic != -1)
		{
			result.setMnemonic(mnemonic);
		}
		return result;
	}

	/**
	 * Creates a <tt>JMenuItem</tt> with a specified string as label.
	 * @param id the id of the string.
	 * @param l the action listener that has to be added to the menu item or
	 *          <tt>null</tt> if no listener has to be added.
	 * @return the menu item.
	 */
	public JMenuItem createMenuItem(String id, ActionListener l)
	{
		JMenuItem result = new JMenuItem(textResource.getString(id));
		result.setActionCommand(id);
		int mnemonic = getMnemonic(id);
		if (mnemonic != -1)
		{
		    result.setMnemonic(mnemonic);
		}
		String keyStrokeDescription = textResource.getString(id + ".keystroke");
		if (keyStrokeDescription != null)
		{
			KeyStroke keyStroke = KeyStroke.getKeyStroke(keyStrokeDescription);
			if (keyStroke != null)
			{
				result.setAccelerator(keyStroke);
			}
		}
		if (l != null)
		{
			result.addActionListener(l);
		}
		return result;
	}

    /**
     * Creates a <tt>JComboBox</tt> with the specified values.
     * @param ids the ids of the values.
     * @return the <tt>JComboBox</tt>.
     */
    public JComboBox<String> createComboBox(String[] ids)
    {
        JComboBox<String> result = new JComboBox<String>();
        for (int i = 0; i < ids.length; i++)
        {
            result.addItem(textResource.getString(ids[i]));
        }
        return result;
    }

    /**
     * Creates an icon.
     * The id specifies a string resource. The string represents the path to the image.
     *
     * <p>Icons are cached.
     * @param id the id of a string resource. The string resource refers to an image resource
     * @return the icon or <code>null</code> if no icon exists with the specified id
     */
    public Icon createIcon(String id) {
    	Icon icon = iconCache.get(id);
    	if (icon == null) {
            URL iconUrl = getUrlForResource(id);
            if (iconUrl == null) {
                return null;
            }

            String description = Factory.getInstance(TextResource.class).getString(id + ".description");
            if (description != null) {
                icon = new ImageIcon(iconUrl, description);
            } else {
                icon = new ImageIcon(iconUrl);
            }
            iconCache.put(id, icon);
    	}
        return icon;
    }

    /**
     * Creates an image.
     * The id specifies a string resource. The string represents the path to the image.
     *
     * <p>Images are cached.
     * @param id the id of a string resource. The string resource refers to an image resource
     * @return the icon or <code>null</code> if no icon exists with the specified id
     */
    public Image createImage(String id) {
    	Image image = imageCache.get(id);
    	if (image == null) {
            URL imageUrl = getUrlForResource(id);
            if (imageUrl == null) {
                return null;
            }

            try {
				image = ImageIO.read(imageUrl);
			} catch (IOException e) {
				LOGGER.warn("Failed to load image " + imageUrl + ": " + e.getMessage(), e);
			}
            imageCache.put(id, image);
    	}
        return image;
    }

    /**
     * Gets the URL that represents a resource (e.g., an image).
     * The id specifies a string resource. The string represents the path to the actual resource
     * (e.g., an image).
     * @param id the id of the resource
     * @return the URL
     */
    private URL getUrlForResource(String id) {
        TextResource tr = Factory.getInstance(TextResource.class);
        String resourceName = tr.getString(id);
        if (resourceName == null) {
            return null;
        } else {
        	return getClass().getResource(resourceName);
        }
    }

    /**
     * Creates a title border for the specified title.
     * @param titleId the id of the title
	 * @param arguments the arguments
     * @return the title border
     */
    public Border createTitleBorder(String titleId, Object... args) {
    	return new TitledBorder(' '  + textResource.getString(titleId, args) + ' ');
    }

    /**
     * Creates a title border for the specified title.
     * @param titleId the id of the title
	 * @param arguments the arguments
     * @return the title border
     */
    public Border createTitleBorderWithPadding(String titleId, Object... args) {
    	return new CompoundBorder(createTitleBorder(titleId, args),
                new EmptyBorder(10, 10, 10, 10));
    }

    /**
     * Creates a title border for the specified title with extra margin.
     * @param titleId the id of the title
     * @return the title border
     */
    public Border createTitleBorderWithMarginAndPadding(String titleId) {
    	return new CompoundBorder(new EmptyBorder(10, 10, 10, 10),
    		createTitleBorderWithPadding(titleId));
    }

    /**
     * Creates a non-sortable table based on the specified table model.
     * The columns of the table are defined by the ColumnDefinitions
     * of the table model.
     * @param tableModel the table model
     * @return the table
     */
    public JTable createTable(AbstractTableModelWithColumnDefinitions tableModel) {
    	JTable table = new TableWithLayoutableColumns(tableModel);
    	initTableColumns(table, tableModel);
    	return table;
    }

    /**
     * Creates a sortable table based on the specified table model.
     * The columns of the table are defined by the ColumnDefinitions
     * of the table model.
     * @param tableModel the table model
     * @return the table
     */
    public JTable createSortedTable(AbstractTableModelWithColumnDefinitions tableModel) {
    	JTable table = new TableWithLayoutableColumns(tableModel);
    	TableRowSorter<AbstractTableModelWithColumnDefinitions> sorter = new TableRowSorter<AbstractTableModelWithColumnDefinitions>(tableModel);
    	initTableColumns(table, tableModel);
    	initSorterForTableModel(sorter, tableModel);
    	table.setRowSorter(sorter);
    	return table;
    }

	private void initTableColumns(JTable table, AbstractTableModelWithColumnDefinitions tableModel) {
        TableColumnModel columnModel = table.getColumnModel();
        int nrCols = tableModel.getColumnCount();

        for (int i=0; i<nrCols; i++) {
            // Set column width
            int colWidth = tableModel.getColumnWidth(i);
            TableColumn column = columnModel.getColumn(i);
            column.setPreferredWidth(colWidth);

            // If present, set the table cell renderer
            TableCellRenderer renderer = tableModel.getRendererForColumn(i);
            if (renderer != null) {
            	column.setCellRenderer(renderer);
            }

            // If present, set the table cell editor
            TableCellEditor editor = tableModel.getEditorForColumn(i);
            if (editor != null) {
                column.setCellEditor(editor);
            }
        }
	}

	private void initSorterForTableModel(TableRowSorter<AbstractTableModelWithColumnDefinitions> sorter,
			AbstractTableModelWithColumnDefinitions tableModel) {
		for (int c=0; c<tableModel.getColumnCount(); c++) {
    		ColumnDefinition colDef = tableModel.getColumnDefinition(c);
    		Comparator<?> comparator =  colDef.getComparator();
    		if (comparator != null) {
    			sorter.setComparator(c, comparator);
    		}
    	}
	}

    /**
     * Creates a scroll pane for the component.
     * @param component the component
     * @return the scroll pane
     */
    public JScrollPane createScrollPane(Component component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setOpaque(false);
        return scrollPane;
    }

    /**
     * Creates a scroll pane for the component with a titled border.
     * @param component the component
     * @param titleId the id of the title
	 * @param arguments the arguments
     * @return the scroll pane
     */
    public JScrollPane createScrollPane(Component component, String titleId, Object... args) {
        JScrollPane scrollPane = createScrollPane(component);
        scrollPane.setBorder(createTitleBorder(titleId, args));
        return scrollPane;
    }

    /**
     * Gets the mnemonic for the specified resource.
     * @param id the id of the resource
     * @return the mnemonic or -1 if no mnemonic was found.
     */
    private int getMnemonic(String id)
    {
        int result = -1;
		try
		{
			String mnemonicId = id + ".mnemonic";
			if (textResource.containsString(mnemonicId)) {
				Class<?> clazz = KeyEvent.class;
			    Field field = clazz.getField(textResource.getString(mnemonicId));
				result = field.getInt(null);
			}
		}
		catch (Exception e)
		{
		    // no mnemonic available in the resources.
		}
		return result;
    }
}
