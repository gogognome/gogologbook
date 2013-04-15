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
package nl.gogognome.lib.swing;

import static javax.swing.SwingConstants.BOTTOM;
import static javax.swing.SwingConstants.CENTER;
import static javax.swing.SwingConstants.HORIZONTAL;
import static javax.swing.SwingConstants.LEFT;
import static javax.swing.SwingConstants.RIGHT;
import static javax.swing.SwingConstants.TOP;
import static javax.swing.SwingConstants.VERTICAL;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import nl.gogognome.lib.util.Factory;

/**
 * This class implements a panel that can hold a number of buttons.
 * The buttons are shown in a single row or in a single column.
 * The location of the buttons within the panel is configurable.
 */
public class ButtonPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/** The location of the buttons within the panel. */
    private int location;

    /** Indicates whether the buttons are laid out horizontally or vertically. */
    private int orientation;

    /** A label that gets added to this panel to push the row of buttons to the left or the right. */
    private JLabel label = new JLabel();

    /**
     * Constructs a button panel.
     * @param location specifies the location of the buttons within the panel.
     *        Allowed values are <code>SwingConstants.LEFT</code>,
     *        <code>SwingConstants.CENTER</code> and <code>SwingConstants.RIGHT</code>
     */
    public ButtonPanel(int location) {
        this(location, SwingConstants.HORIZONTAL);
    }

    /**
     * Constructs a button panel.
     * @param location specifies the location of the buttons within the panel.
     *        Allowed values for horizontally alligned panels are <code>SwingConstants.LEFT</code>,
     *        <code>SwingConstants.CENTER</code> and <code>SwingConstants.RIGHT</code>.
     *        Allowed values for vertically alligned panels are <code>SwingConstants.TOP</code>,
     *        <code>SwingConstants.CENTER</code> and <code>SwingConstants.BOTTOM</code>.
     * @param orientation the orientation. Must be <code>SwingConstants.HORIZONTAL</code>
     *        or <code>SwingConstants.VERTICAL</code>
     */
    public ButtonPanel(int location, int orientation) {
        super();
        this.location = location;
        this.orientation = orientation;
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException("Invalid orientation: " + orientation);
        }
        setLayout(new GridBagLayout());
        switch(location) {
        case CENTER:
            if (orientation == SwingConstants.HORIZONTAL) {
                add(new JLabel(), SwingUtils.createGBConstraints(0, 0, 1, 1, 1.0, 1.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 0, 0, 0, 0));
                add(label, SwingUtils.createGBConstraints(1, 0, 1, 1, 1.0, 1.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 0, 0, 0, 0));
            } else {
                add(new JLabel(), SwingUtils.createGBConstraints(0, 0, 1, 1, 1.0, 1.0,
                    GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, 0, 0, 0, 0));
                add(label, SwingUtils.createGBConstraints(0, 1, 1, 1, 1.0, 1.0,
                    GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, 0, 0, 0, 0));
            }
            break;

        case LEFT:
            if (orientation != SwingConstants.HORIZONTAL) {
                throw new IllegalArgumentException("The location LEFT is only allowed for HORIZONTAL orientation.");
            }
            add(label, SwingUtils.createGBConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 0, 0, 0, 0));
            break;

        case RIGHT:
            if (orientation != SwingConstants.HORIZONTAL) {
                throw new IllegalArgumentException("The location RIGHT is only allowed for HORIZONTAL orientation.");
            }
            add(label, SwingUtils.createGBConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 0, 0, 0, 0));
            break;

        case TOP:
            if (orientation != SwingConstants.VERTICAL) {
                throw new IllegalArgumentException("The location TOP is only allowed for VERTICAL orientation.");
            }
            add(label, SwingUtils.createGBConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, 0, 0, 0, 0));
            break;

        case BOTTOM:
            if (orientation != SwingConstants.VERTICAL) {
                throw new IllegalArgumentException("The location BOTTOM is only allowed for VERTICAL orientation.");
            }
            add(label, SwingUtils.createGBConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, 0, 0, 0, 0));
            break;

        default:
            throw new IllegalArgumentException("Invalid location specified: " + location);
        }
    }

    /**
     * Adds a button to this panel.
	 * @param id the id of the button's description in the resources.
	 * @param the action to be performed when the button is pressed
	 * @return the button.
     */
    public JButton addButton(String id, Action action) {
    	WidgetFactory wf = Factory.getInstance(WidgetFactory.class);
    	JButton button = wf.createButton(id, action);
    	add(button);
    	return button;
    }

    /**
     * Adds a component to this panel.
     * @param component the component to be added
     * @return the component
     */
    @Override
    public Component add(Component component) {
        boolean firstButton = getComponentCount() == 1;
    	int leftTopInset = (location == LEFT || location == TOP) && firstButton ? 0 : 5;
    	int rightBottomInset = (location == RIGHT || location == BOTTOM) && firstButton ? 0 : 5;

        switch(location) {
        case CENTER:
        case LEFT:
        case TOP:
            // Remove the label, add the button and add the label as right-most element in the row
            remove(label);
            if (orientation == SwingConstants.HORIZONTAL) {
                add(component, SwingUtils.createGBConstraints(getComponentCount(), 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, leftTopInset, 5, rightBottomInset));
                add(label, SwingUtils.createGBConstraints(getComponentCount(), 0, 1, 1, 1.0, 1.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 0, 0, 0, 0));
            } else {
                add(component, SwingUtils.createGBConstraints(0, getComponentCount(), 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, leftTopInset, 5, rightBottomInset, 5));
                add(label, SwingUtils.createGBConstraints(0, getComponentCount(), 1, 1, 1.0, 1.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 0, 0, 0, 0));
            }
            break;

        case RIGHT:
            add(component, SwingUtils.createGBConstraints(getComponentCount(), 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 5, rightBottomInset));
            break;
        case BOTTOM:
            add(component, SwingUtils.createGBConstraints(0, getComponentCount(), 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 5, rightBottomInset));
            break;
        }
        return component;
    }

}
