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

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusListener;
import java.text.ParseException;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import nl.gogognome.lib.swing.SwingUtils;
import nl.gogognome.lib.swing.models.AbstractModel;
import nl.gogognome.lib.swing.models.ModelChangeListener;

/**
 * Base class for a text field bean. Make sure that after instantiation
 * first the method {@link #initBean()} is called.
 *
 * @author Sander Kooijmans
 */
public abstract class AbstractTextFieldBean<M extends AbstractModel> extends JPanel
		implements Bean {

	private static final long serialVersionUID = 1L;

	protected M model;

    /** The text field in which the user can enter the string. */
    private JTextField textfield;

    /** The listener for changes made programmatically. */
    private ModelChangeListener modelChangeListener;

    /** Listener for changes made by the user. */
    private DocumentListener documentListener;

    private int nrColumns;

    /**
     * Constructor.
     * @param model the model that will reflect the content of the bean
     */
    protected AbstractTextFieldBean(M model) {
    	this(model, 0);
    }

    /**
     * Constructor.
     * @param model the model that will reflect the content of the bean
     * @param nrColumns the width of the text field as the number of columns.
     *        The value 0 indicates that the width can be determined by the layout manager.
     */
    protected AbstractTextFieldBean(M model, int nrColumns) {
    	this.model = model;
    	this.nrColumns = nrColumns;
    }

    @Override
	public void initBean() {
        setOpaque(false);
        setLayout(new GridBagLayout());

        textfield = createTextField(nrColumns);

        updateTextField();
        modelChangeListener = new UpdateTextFieldOnModelChangeListener();
        model.addModelChangeListener(modelChangeListener);

        documentListener = new ParseUserInputOnDocumentChangeListener();
        textfield.getDocument().addDocumentListener(documentListener);

        add(textfield, SwingUtils.createGBConstraints(0,0, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, nrColumns == 0 ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE,
            0, 0, 0, 0));
    }

    protected JTextField createTextField(int nrColumns) {
		return new JTextField(nrColumns);
	}

	/**
     * Deinitializes this bean. It will free its resources.
     */
    @Override
	public void close() {
        model.removeModelChangeListener(modelChangeListener);
        textfield.getDocument().removeDocumentListener(documentListener);
        modelChangeListener = null;
        documentListener = null;
        model = null;
        textfield = null;
    }

    /**
     * @see JComponent#addFocusListener(FocusListener)
     */
    @Override
	public void addFocusListener(FocusListener listener) {
        textfield.addFocusListener(listener);
    }

    /**
     * @see JComponent#removeFocusListener(FocusListener)
     */
    @Override
	public void removeFocusListener(FocusListener listener) {
        textfield.removeFocusListener(listener);
    }

    /**
     * Updates the text field with the value of the model.
     */
    private void updateTextField() {
    	textfield.setEnabled(model.isEnabled());
        String string = getStringFromModel();
        if (string != null) {
            textfield.setText(string);
        } else {
            textfield.setText("");
        }
    }

    /**
     * This method gets a string representation of the model's value.
     * @return the string representation
     */
    protected abstract String getStringFromModel();

	/**
     * Parses the text that has been entered by the user. If the entered text is a valid
     * value, then the model is updated.
     */
    private void parseUserInput() {
        try {
            parseUserInput(textfield.getText(), modelChangeListener);
            textfield.setBorder(new LineBorder(Color.GRAY));
        } catch (ParseException e) {
            if (textfield.getText().length() > 0) {
                textfield.setBorder(new LineBorder(Color.RED));
            } else {
                textfield.setBorder(new LineBorder(Color.GRAY));
            }
        }
    }

    /**
     * Parses the entered text and updates the model with the parsed value.
     * @param text the entered text
     * @throws ParseException if the text is invalid for the model
     */
    protected abstract void parseUserInput(String text, ModelChangeListener modelChangeListener)
    	throws ParseException;

    @Override
    public void requestFocus() {
    	textfield.requestFocus();
    }

    @Override
    public boolean requestFocusInWindow() {
    	return textfield.requestFocusInWindow();
    }

    private final class UpdateTextFieldOnModelChangeListener implements ModelChangeListener {
    	@Override
    	public void modelChanged(AbstractModel model) {
    		updateTextField();
    	}
    }

    private final class ParseUserInputOnDocumentChangeListener implements DocumentListener {
    	@Override
    	public void changedUpdate(DocumentEvent evt) {
    		parseUserInput();
    	}

    	@Override
    	public void insertUpdate(DocumentEvent evt) {
    		parseUserInput();
    	}

    	@Override
    	public void removeUpdate(DocumentEvent evt) {
    		parseUserInput();
    	}
    }
}
