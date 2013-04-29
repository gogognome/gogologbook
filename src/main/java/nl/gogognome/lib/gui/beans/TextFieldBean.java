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

import java.text.ParseException;

import nl.gogognome.lib.swing.models.ModelChangeListener;
import nl.gogognome.lib.swing.models.StringModel;

/**
 * This class implements a bean for entering a <code>String</code>.
 *
 * @author Sander Kooijmans
 */
public class TextFieldBean extends AbstractTextFieldBean<StringModel> {

	private static final long serialVersionUID = 1L;

	/**
     * Constructor.
     * @param stringModel the string model that will reflect the content of the bean
     */
    public TextFieldBean(StringModel stringModel) {
    	super(stringModel);
    }

    /**
     * Constructor.
     * @param stringModel the string model that will reflect the content of the bean
     * @param nrColumns the width of the text field as the number of columns.
     *        The value 0 indicates that the width can be determined by the layout manager.
     */
    protected TextFieldBean(StringModel stringModel, int nrColumns) {
    	super(stringModel, nrColumns);
    }

    @Override
    protected String getStringFromModel() {
    	return model.getString();
    }

    @Override
    protected void parseUserInput(String text, ModelChangeListener modelChangeListener)
    		throws ParseException {
    	model.setString(text, modelChangeListener);
    }

}
