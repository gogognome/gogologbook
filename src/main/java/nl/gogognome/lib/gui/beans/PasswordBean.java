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

import javax.swing.JPasswordField;
import javax.swing.JTextField;

import nl.gogognome.lib.swing.models.StringModel;

/**
 * This class implements a bean for password entry.
 *
 * @author Sander Kooijmans
 */
public class PasswordBean extends TextFieldBean {

	private static final long serialVersionUID = 1L;

	public PasswordBean(StringModel stringModel) {
		super(stringModel);
	}

	public PasswordBean(StringModel stringModel, int nrColumns) {
		super(stringModel, nrColumns);
	}

	@Override
	protected JTextField createTextField(int nrColumns) {
		return new JPasswordField(nrColumns);
	}
}
