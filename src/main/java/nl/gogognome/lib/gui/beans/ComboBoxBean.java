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
package nl.gogognome.lib.gui.beans;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import nl.gogognome.lib.swing.JComboBoxWithKeyboardInput;
import nl.gogognome.lib.swing.models.AbstractModel;
import nl.gogognome.lib.swing.models.ListModel;
import nl.gogognome.lib.swing.models.ModelChangeListener;

/**
 * This class implements a bean for selecting an item in a list of items.
 *
 * @author Sander Kooijmans
 */
public class ComboBoxBean<T> extends JComboBoxWithKeyboardInput implements Bean {

	private static final long serialVersionUID = 1L;

	private List<T> items;

	private ListModel<T> listModel;

	private ModelChangeListener modelChangeListener;

	private ItemListener itemListener;

	private ObjectFormatter<T> itemFormatter;

	/**
	 * Constructor.
	 * @param listModel the list model containing the items and managing the selected item
	 */
	public ComboBoxBean(ListModel<T> listModel) {
		super();
		this.listModel = listModel;
	}

	@Override
	public void initBean() {
		updateItems();

		int index = listModel.getSelectedIndex();
		if (0 <= index && index < getItemCount()) {
			setSelectedIndex(index);
		}

		modelChangeListener = new ModelChangeListener() {
			@Override
			public void modelChanged(AbstractModel model) {
				onModelChanged();
			}
		};

		listModel.addModelChangeListener(modelChangeListener);

		itemListener = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				updateSelectedItemInModel();
			}
		};
		addItemListener(itemListener);
		updateSelectionInCombobox();
	}

	private void updateSelectionInCombobox() {
		setSelectedIndex(listModel.getSelectedIndex());
	}

	public void setItemFormatter(ObjectFormatter<T> itemFormatter) {
		this.itemFormatter = itemFormatter;
		if (itemFormatter != null) {
			setRenderer(new ItemFormatterRenderer<T>(itemFormatter));
		} else {
			setRenderer(new DefaultListCellRenderer());
		}
		updateItems();
	}

	private void updateItems() {
		int prevSelIndex = getSelectedIndex();
		removeAllItems();
		items = new ArrayList<T>(listModel.getItems());
		for (T item : listModel.getItems()) {
			if (itemFormatter != null) {
				addItemWithStringRepresentation(item, itemFormatter.format(item));
			} else {
				addItem(item);
			}
		}
		setSelectedIndex(prevSelIndex);
	}

	private void onModelChanged() {
		if (!Arrays.equals(listModel.getItems().toArray(), items.toArray())) {
			updateItems();
		}

		if (getSelectedIndex() != listModel.getSelectedIndex()) {
			updateSelectionInCombobox();
		}

		setEnabled(listModel.isEnabled());
	}

	private void updateSelectedItemInModel() {
		if (getSelectedIndex() != listModel.getSelectedIndex()) {
			listModel.setSelectedIndex(getSelectedIndex(), modelChangeListener);
		}
	}

	@Override
	public void close() {
		super.close();
		listModel.removeModelChangeListener(modelChangeListener);
		removeItemListener(itemListener);
	}
}

class ItemFormatterRenderer<T> extends BasicComboBoxRenderer {

	private static final long serialVersionUID = 1L;

	private ObjectFormatter<T> itemFormatter;

	public ItemFormatterRenderer(ObjectFormatter<T> itemFormatter) {
		super();
		this.itemFormatter = itemFormatter;
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		String formattedValue = itemFormatter.format((T) value);

		return super.getListCellRendererComponent(list, formattedValue, index, isSelected,
				cellHasFocus);
	}
}