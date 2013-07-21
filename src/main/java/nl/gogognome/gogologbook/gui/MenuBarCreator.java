package nl.gogognome.gogologbook.gui;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import nl.gogognome.lib.swing.WidgetFactory;
import nl.gogognome.lib.util.Factory;

public class MenuBarCreator {

	private final WidgetFactory widgetFactory = Factory.getInstance(WidgetFactory.class);

	public static class ActionListeners {
		public ActionListener openAboutView;
		public ActionListener openLogMessageCreateView;
		public ActionListener openProjectsView;
		public ActionListener openUsersView;
		public ActionListener openCategoriesView;
		public ActionListener exit;
	}

	public JMenuBar createMenuBar(ActionListeners listeners) {
		JMenu viewMenu = widgetFactory.createMenu("mi.view");
		viewMenu.add(widgetFactory.createMenuItem("mi.openLogMessageCreateView", listeners.openLogMessageCreateView));
		viewMenu.add(widgetFactory.createMenuItem("mi.openProjectsView", listeners.openProjectsView));
		viewMenu.add(widgetFactory.createMenuItem("mi.openUsersView", listeners.openUsersView));
		viewMenu.add(widgetFactory.createMenuItem("mi.openCategoriesView", listeners.openCategoriesView));
		viewMenu.addSeparator();
		viewMenu.add(widgetFactory.createMenuItem("mi.exit", listeners.exit));

		JMenu helpMenu = widgetFactory.createMenu("mi.help");
		helpMenu.add(widgetFactory.createMenuItem("mi.about", listeners.openAboutView));

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(viewMenu);
		menuBar.add(helpMenu);

		return menuBar;
	}
}
