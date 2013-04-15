/*
    This file is part of gogo account.

    gogo account is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    gogo account is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with gogo account.  If not, see <http://www.gnu.org/licenses/>.
*/
package nl.gogognome.gogologbook.gui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import nl.gogognome.lib.swing.MessageDialog;
import nl.gogognome.lib.swing.views.View;
import nl.gogognome.lib.swing.views.ViewListener;
import nl.gogognome.lib.swing.views.ViewTabbedPane;
import nl.gogognome.lib.text.TextResource;
import nl.gogognome.lib.util.Factory;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;

	private final ViewTabbedPane viewTabbedPane;

	private final Map<Class<?>, View> openViews = new HashMap<Class<?>, View>();
	private final TextResource textResource = Factory.getInstance(TextResource.class);

	public MainFrame() {
		super();
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		viewTabbedPane = new ViewTabbedPane(this);
		getContentPane().add(viewTabbedPane);

		addWindowListener(new WindowAdapter() {
			@Override
            public void windowClosing(WindowEvent e) { handleExit(); } }
		);

        setTitle(createTitle());

        // Set icon
        URL url = ClassLoader.getSystemResource("icon-32x32.png");
        Image image = Toolkit.getDefaultToolkit().createImage(url);
        setIconImage(image);

        setMinimumSize(new Dimension(800, 600));
	}

	private String createTitle() {
	    return textResource.getString("mf.title");
	}


	private void handleExit() {
		dispose();
	}

	private void openView(Class<? extends View> viewClass) {
		View view = openViews.get(viewClass);
	    if (view == null) {
	    	try {
				view = createView(viewClass);
			} catch (Exception e) {
				MessageDialog.showErrorMessage(this, e, "mf.problemCreatingView");
				return;
			}
	    	view.addViewListener(new ViewCloseListener());
            viewTabbedPane.openView(view);
            viewTabbedPane.selectView(view);

            openViews.put(viewClass, view);
        } else {
            viewTabbedPane.selectView(view);
	    }
	}

    private View createView(Class<? extends View> viewClass) throws Exception {
    	Constructor<? extends View> c = viewClass.getConstructor();
    	return c.newInstance();
	}

    private class ViewCloseListener implements ViewListener {
        @Override
		public void onViewClosed(View view) {
            view.removeViewListener(this);
            openViews.remove(view.getClass());
        }
    }
}
