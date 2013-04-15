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

import nl.gogognome.lib.swing.models.DoubleModel;
import nl.gogognome.lib.swing.models.ModelChangeListener;
import nl.gogognome.lib.text.TextResource;
import nl.gogognome.lib.util.Factory;
import nl.gogognome.lib.util.StringUtil;

/**
 * This class implements a bean for entering a double.
 *
 * @author Sander Kooijmans
 */
public class DoubleFieldBean extends AbstractTextFieldBean<DoubleModel> {

    /**
     * Constructor.
     * @param doubleModel the double model that will reflect the content of the bean
     */
    protected DoubleFieldBean(DoubleModel doubleModel) {
    	super(doubleModel);
    }

    /**
     * Constructor.
     * @param doubleModel the double model that will reflect the content of the bean
     * @param nrColumns the width of the text field as the number of columns.
     *        The value 0 indicates that the width can be determined by the layout manager.
     */
    protected DoubleFieldBean(DoubleModel doubleModel, int nrColumns) {
    	super(doubleModel, nrColumns);
    }

    @Override
    protected String getStringFromModel() {
    	if (model.isNull()) {
    		return "";
    	} else {
    		return Factory.getInstance(TextResource.class).formatDouble(model.getDouble());
    	}
    }

    @Override
    protected void parseUserInput(String text, ModelChangeListener modelChangeListener)
    		throws ParseException {
    	if (StringUtil.isNullOrEmpty(text)) {
    		model.setDouble(null);
    	} else {
	    	double d = Factory.getInstance(TextResource.class).parseDouble(text);
	    	model.setDouble(d, modelChangeListener);
    	}
    }

}
