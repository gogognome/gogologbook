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

import java.awt.GridBagConstraints;

import nl.gogognome.lib.swing.SwingUtils;

/**
 * This class implements a panel containing a column of input fields.
 * Each input field consists of a label and a component (typically a text field).
 * The values of the fields are managed by models (for example, StringModel
 * and DateModel).
 */
public class InputFieldsColumn extends AbstractInputFieldsPanel {

    private static final long serialVersionUID = 1L;

    public InputFieldsColumn() {
        super();
    }

	@Override
	protected GridBagConstraints getLabelConstraints() {
		return SwingUtils.createLabelGBConstraints(0, components.size());
	}

	@Override
	protected GridBagConstraints getFixedSizeFieldConstraints() {
		return SwingUtils.createLabelGBConstraints(1, components.size());
	}

	@Override
	protected GridBagConstraints getVariableSizeFieldConstraints() {
		return SwingUtils.createTextFieldGBConstraints(1, components.size());
	}
}
