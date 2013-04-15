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

import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;

import nl.gogognome.lib.swing.models.AbstractModel;
import nl.gogognome.lib.swing.models.BooleanModel;
import nl.gogognome.lib.swing.models.ModelChangeListener;

/**
 * This class implements a bean for selecting a <code>Boolean</code>.
 *
 * @author Sander Kooijmans
 */
public class CheckBoxBean extends JCheckBox implements Bean {

	private static final long serialVersionUID = 1L;

	private BooleanModel booleanModel;

    private ModelChangeListener modelChangeListener;
    private ItemListener itemListener;

    /**
     * Constructor.
     * @param booleanModel the model that will reflect the content of the bean
     */
    public CheckBoxBean(BooleanModel booleanModel) {
        this.booleanModel = booleanModel;
    }

    @Override
	public void initBean() {
        setOpaque(false);
        setLayout(new GridBagLayout());

        updateRadioButton();
        modelChangeListener = new ModelChangeListener() {

            @Override
			public void modelChanged(AbstractModel model) {
                updateRadioButton();
            }

        };
        booleanModel.addModelChangeListener(modelChangeListener);

        itemListener = new ItemListenerImpl();
        addItemListener(itemListener);
    }

    @Override
	public void close() {
        booleanModel.removeModelChangeListener(modelChangeListener);
        removeItemListener(itemListener);
        modelChangeListener = null;
        itemListener = null;
        booleanModel = null;
    }

    /**
     * Updates the check box with the value of the boolean model.
     */
    private void updateRadioButton() {
        setSelected(booleanModel.getBoolean());
    }

    private class ItemListenerImpl implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			booleanModel.setBoolean(isSelected(), modelChangeListener);
		}
    }
}
