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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import nl.gogognome.lib.gui.Closeable;
import nl.gogognome.lib.swing.WidgetFactory;
import nl.gogognome.lib.swing.models.*;
import nl.gogognome.lib.util.Factory;

/**
 * Base class for the InputFieldsColumn and InputFieldsRow.
 * 
 * @author Sander Kooijmans
 */
public abstract class AbstractInputFieldsPanel extends JPanel implements Closeable {

	private static final long serialVersionUID = 1L;

	protected List<Component> components = new ArrayList<Component>();
	private final BeanFactory beanFactory = Factory.getInstance(BeanFactory.class);

	public AbstractInputFieldsPanel() {
		super(new GridBagLayout());
		setOpaque(false);
	}

	/**
	 * This method should be called after the view containing this panel is closed. It releases its resources.
	 */
	@Override
	public void close() {
		for (Component c : components) {
			if (c instanceof Closeable) {
				((Closeable) c).close();
			}
		}
	}

	/**
	 * Adds a field to edit a string.
	 * 
	 * @param labelId
	 *            the id of the label that is put in front of the text field
	 * @param model
	 *            the model controlling the text field
	 */
	public void addField(String labelId, StringModel model) {
		addVariableSizeField(labelId, beanFactory.createTextFieldBean(model));
	}

	/**
	 * Adds a field to edit a string.
	 * 
	 * @param labelId
	 *            the id of the label that is put in front of the text field
	 * @param model
	 *            the model controlling the text field
	 * @param nrColumns
	 *            the width of the text field as the number of columns. The value 0 indicates that the width can be determined by the layout manager.
	 */
	public void addField(String labelId, StringModel model, int nrColumns) {
		addVariableSizeField(labelId, beanFactory.createTextFieldBean(model, nrColumns));
	}

	/**
	 * Adds a field to edit a double.
	 * 
	 * @param labelId
	 *            the id of the label that is put in front of the text field
	 * @param model
	 *            the model controlling the text field
	 */
	public void addField(String labelId, DoubleModel model) {
		addVariableSizeField(labelId, beanFactory.createDoubleFieldBean(model));
	}

	/**
	 * Adds a field to edit a double.
	 * 
	 * @param labelId
	 *            the id of the label that is put in front of the text field
	 * @param model
	 *            the model controlling the text field
	 * @param nrColumns
	 *            the width of the text field as the number of columns. The value 0 indicates that the width can be determined by the layout manager.
	 */
	public void addField(String labelId, DoubleModel model, int nrColumns) {
		addVariableSizeField(labelId, beanFactory.createDoubleFieldBean(model, nrColumns));
	}

	/**
	 * Adds a field to edit a password.
	 * 
	 * @param labelId
	 *            the id of the label that is put in front of the text field
	 * @param model
	 *            the model controlling the text field
	 * @param nrColumns
	 *            the width of the text field as the number of columns. The value 0 indicates that the width can be determined by the layout manager.
	 */
	public void addPasswordField(String labelId, StringModel model, int nrColumns) {
		addVariableSizeField(labelId, beanFactory.createPasswordBean(model, nrColumns));
	}

	/**
	 * Adds a check box.
	 * 
	 * @param labelId
	 *            the id of the label that is put in front of the check box
	 * @param model
	 *            the model controlling the check box
	 */
	public void addField(String labelId, BooleanModel model) {
		addVariableSizeField(labelId, beanFactory.createCheckBoxBean(model));
	}

	/**
	 * Adds a field to select a file.
	 * 
	 * @param labelId
	 *            the id of the label that is put in front of the file selection bean.
	 * @param model
	 *            the model controlling the file seleciton bean
	 */
	public void addField(String labelId, FileModel model) {
		addVariableSizeField(labelId, beanFactory.createFileSelectionBean(model));
	}

	/**
	 * Adds a component. Use this method to add components for which the general models (StringModel, DateModel etc.) cannot be used.
	 * 
	 * @param labelId
	 *            the id of the label that is put in front of the component
	 * @param component
	 *            the component
	 */
	public void addVariableSizeField(String labelId, JComponent component) {
		addLabelAndFieldWithConstraints(labelId, component, getVariableSizeFieldConstraints());
	}

	/**
	 * Adds a field to edit a string.
	 * 
	 * @param labelId
	 *            the id of the label that is put in front of the text field
	 * @param model
	 *            the model controlling the text field
	 */
	public void addField(String labelId, DateModel model) {
		addLabelAndFieldWithConstraints(labelId, beanFactory.createDateSelectionBean(model), getFixedSizeFieldConstraints());
	}

	/**
	 * Adds a combo box to select a single item from a list of items.
	 * 
	 * @param labelId
	 *            the id of the label that is put in front of the combo box
	 * @param model
	 *            the model controlling the combo box
	 */
	public <T> void addComboBoxField(String labelId, ListModel<T> model, ObjectFormatter<T> itemFormatter) {
		ComboBoxBean<T> bean = beanFactory.createComboBoxBean(model);
		bean.setItemFormatter(itemFormatter);
		addLabelAndFieldWithConstraints(labelId, bean, getFixedSizeFieldConstraints());
	}

	protected void addLabelAndFieldWithConstraints(String labelId, JComponent component, GridBagConstraints constraints) {
		JLabel label = Factory.getInstance(WidgetFactory.class).createLabel(labelId, component);
		add(label, getLabelConstraints());
		add(component, constraints);
		components.add(component);
	}

	protected abstract GridBagConstraints getLabelConstraints();

	protected abstract GridBagConstraints getFixedSizeFieldConstraints();

	protected abstract GridBagConstraints getVariableSizeFieldConstraints();

	@Override
	public void requestFocus() {
		if (!components.isEmpty()) {
			components.get(0).requestFocus();
		}
	}

	@Override
	public boolean requestFocusInWindow() {
		if (!components.isEmpty()) {
			return components.get(0).requestFocusInWindow();
		} else {
			return false;
		}
	}
}
