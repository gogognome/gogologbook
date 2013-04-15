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
package nl.gogognome.lib.swing.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class implements a model for a list of items.
 *
 * @author Sander Kooijmans
 */
public class ListModel<T> extends AbstractModel {

	private List<T> items;

	private int[] selectedIndices = new int[0];

	public ListModel() {
	}

	public ListModel(List<T> items) {
		setItems(items);
	}

	/**
	 * Sets the items for the list model.
	 * @param items the items
	 */
	public void setItems(List<T> items) {
		setItems(items, null);
	}

	/**
	 * Sets the items for the list model.
	 * @param items the items
	 * @param source the model change listener that sets the items.  It will not
     *        get notified. It may be null.
	 */
	public void setItems(List<T> items, ModelChangeListener source) {
		this.items = new ArrayList<T>(items);
		notifyListeners(source);
	}

	/**
	 * Adds an item to the list model.
	 * @param item the item
	 * @param source the model change listener that sets the items.  It will not
     *        get notified. It may be null.
	 */
	public void addItem(T item, ModelChangeListener source) {
		items.add(item);
		notifyListeners(source);
	}

	public List<T> getItems() {
		return items;
	}

	/**
	 * Sets the selected index.
	 * @param selectedIndex the selected index. -1 indicates that no item is selected
	 * @param source the model change listener that sets the items.  It will not
     *        get notified. It may be <code>null</code>.
	 */
	public void setSelectedIndex(int selectedIndex, ModelChangeListener source) {
		int[] indices;
		if (selectedIndex == -1) {
			indices = new int[0];
		} else {
			indices = new int[] { selectedIndex };
		}
		setSelectedIndices(indices, source);
	}

	/**
	 * Sets the selected indices.
	 * @param selectedIndices the selected indices
	 * @param source the model change listener that sets the items.  It will not
     *        get notified. It may be <code>null</code>.
	 */
	public void setSelectedIndices(int[] selectedIndices, ModelChangeListener source) {
		if (!Arrays.equals(this.selectedIndices, selectedIndices)) {
			this.selectedIndices = selectedIndices;
			notifyListeners(source);
		}
	}

	/**
	 * If exactly one item has been selected, then its index is returned.
	 * Otherwise -1 is returned.
	 * @return the index of the selected item or -1
	 */
	public int getSelectedIndex() {
		if (selectedIndices.length == 1) {
			return selectedIndices[0];
		} else {
			return -1;
		}
	}

	public int[] getSelectedIndices() {
		return selectedIndices;
	}

	/**
	 * If exactly one item has been selected, then that item is returned.
	 * Otherwise null is returned.
	 * @return the selected item or null
	 */
	public T getSelectedItem() {
		if (selectedIndices.length == 1) {
			return items.get(selectedIndices[0]);
		} else {
			return null;
		}
	}

	/**
	 * Selects a single item.
	 * @param item the item
	 * @param source the model change listener that sets the items.  It will not
     *        get notified. It may be <code>null</code>.
	 */
	public void setSelectedItem(T item, ModelChangeListener source) {
		int index = items.indexOf(item);
		int[] indices = index == -1 ? new int[0] : new int[] { index };
		setSelectedIndices(indices, source);
	}

	/**
	 * Gets a list of selected items.
	 * @return the list (never null)
	 */
	public List<T> getSelectedItems() {
		List<T> list = new ArrayList<T>();
		for (int index : selectedIndices) {
			list.add(items.get(index));
		}
		return list;
	}

}
