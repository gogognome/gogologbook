package nl.gogognome.gogologbook.gui;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import nl.gogognome.lib.swing.WidgetFactory;
import nl.gogognome.lib.util.Factory;

public class MenuBarCreator {

	private final WidgetFactory widgetFactory = Factory.getInstance(WidgetFactory.class);

	public static class ActionListeners {
		public ActionListener openLogMessageCreateView;
		public ActionListener exit;
	}

	public JMenuBar createMenuBar(ActionListeners listeners) {
		JMenu viewMenu = widgetFactory.createMenu("mi.view");
		viewMenu.add(widgetFactory.createMenuItem("mi.openLogMessageCreateView", listeners.openLogMessageCreateView));

		JMenu helpMenu = widgetFactory.createMenu("mi.help");
		helpMenu.add(widgetFactory.createMenuItem("mi.exit", listeners.exit));

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(viewMenu);
		menuBar.add(helpMenu);

		return menuBar;
	}
}
