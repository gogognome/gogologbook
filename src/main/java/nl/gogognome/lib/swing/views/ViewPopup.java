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
package nl.gogognome.lib.swing.views;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPopupMenu;
import javax.swing.MenuSelectionManager;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import nl.gogognome.lib.swing.SwingUtils;

/**
 * This class shows a view in a popup.
 *
 * @author Sander Kooijmans
 */
public class ViewPopup {

	private View view;

	private JPopupMenu popup;

	private PopupMenuListener popupMenuListener;

	public ViewPopup(View view) {
		this.view = view;
	}

	public void show(Component owner, Point p) {
		if (popup == null) {
			initView();
			JPopupMenu popup = new JPopupMenu();
			popup.add(view);
			popup.show(SwingUtils.getTopLevelContainer(owner), p.x, p.y);
			popupMenuListener = new PopupMenuListenerImpl();
			popup.addPopupMenuListener(popupMenuListener);
		}
	}

	public void hidePopup() {
		if (popup != null) {
			requestClose();
		}
	}

	private void initView() {
        Action closeAction = new AbstractAction() {
            @Override
			public void actionPerformed(ActionEvent event) {
                requestClose();
            }
        };

        view.setCloseAction(closeAction);
        view.doInit();
	}

	private void requestClose() {
		view.doClose();
		MenuSelectionManager.defaultManager().clearSelectedPath();
		popup = null;
	}

	private void onClosedByUser() {
		view.doClose();
		popup = null;
	}

	private class PopupMenuListenerImpl implements PopupMenuListener {

		@Override
		public void popupMenuWillBecomeVisible(PopupMenuEvent popupmenuevent) {
		}

		@Override
		public void popupMenuWillBecomeInvisible(PopupMenuEvent popupmenuevent) {
			onClosedByUser();
		}

		@Override
		public void popupMenuCanceled(PopupMenuEvent popupmenuevent) {
		}

	}
}
