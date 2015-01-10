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

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import nl.gogognome.lib.swing.models.AbstractModel;
import nl.gogognome.lib.swing.models.ModelChangeListener;
import nl.gogognome.lib.swing.models.StringModel;

public class TextAreaBean extends JPanel implements Bean {

	private static final long serialVersionUID = 1L;

	private StringModel model;
	private JTextArea textArea;
	private int nrColumns;
	private int nrRows;
	private ModelChangeListener modelChangeListener;
	private DocumentListener documentListener;

	public TextAreaBean(StringModel model) {
		this.model = model;
	}

	public TextAreaBean(StringModel model, int nrColumns, int nrRows) {
		this.model = model;
		this.nrColumns = nrColumns;
		this.nrRows = nrRows;
	}

	@Override
	public void initBean() {
		setOpaque(false);
		setLayout(new BorderLayout());

		textArea = new JTextArea();
		if (nrColumns > 0) {
			textArea.setColumns(nrColumns);
		}
		if (nrRows > 0) {
			textArea.setRows(nrRows);
		}

		updateTextArea();
		modelChangeListener = new UpdatetextAreaOnModelChangeListener();
		model.addModelChangeListener(modelChangeListener);

		documentListener = new ParseUserInputOnDocumentChangeListener();
		textArea.getDocument().addDocumentListener(documentListener);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setOpaque(false);
		add(scrollPane, BorderLayout.CENTER);
	}

	@Override
	public void close() {
		model.removeModelChangeListener(modelChangeListener);
		textArea.getDocument().removeDocumentListener(documentListener);
		modelChangeListener = null;
		documentListener = null;
		model = null;
		textArea = null;
	}

	private void updateTextArea() {
		textArea.setEnabled(model.isEnabled());
		String string = model.getString();
		if (string != null) {
			textArea.setText(string);
		} else {
			textArea.setText("");
		}
	}

	private void parseUserInput() {
		model.setString(textArea.getText(), modelChangeListener);
		textArea.setBorder(new LineBorder(Color.GRAY));
	}

	private final class UpdatetextAreaOnModelChangeListener implements ModelChangeListener {
		@Override
		public void modelChanged(AbstractModel model) {
			updateTextArea();
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
