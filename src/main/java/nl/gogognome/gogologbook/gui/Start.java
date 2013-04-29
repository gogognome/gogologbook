package nl.gogognome.gogologbook.gui;

import java.io.File;
import java.util.Locale;

import javax.swing.JFrame;

import nl.gogognome.gogologbook.dao.LogMessageDAO;
import nl.gogognome.gogologbook.dbinsinglefile.SingleFileLogMessageDAO;
import nl.gogognome.gogologbook.util.DaoFactory;
import nl.gogognome.lib.gui.beans.BeanFactory;
import nl.gogognome.lib.swing.SwingUtils;
import nl.gogognome.lib.swing.WidgetFactory;
import nl.gogognome.lib.swing.plaf.DefaultLookAndFeel;
import nl.gogognome.lib.text.TextResource;
import nl.gogognome.lib.util.Factory;

public class Start {

	private MainFrame mainFrame;
	private File dbFile;

	public static void main(String[] args) {
		Start start = new Start();
		start.startApplication(args);
	}

	private void startApplication(String[] args) {
		initFactory(Locale.getDefault());
		parseArguments(args);
		registerDAOs();
		DefaultLookAndFeel.useDefaultLookAndFeel();
		initFrame();
	}

	public void initFactory(Locale locale) {
		TextResource tr = new TextResource(locale);
		tr.loadResourceBundle("stringresources");

		Factory.bindSingleton(TextResource.class, tr);
		Factory.bindSingleton(WidgetFactory.class, new WidgetFactory(tr));
		Factory.bindSingleton(BeanFactory.class, new BeanFactory());
	}

	private void parseArguments(String[] args) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("-lang=")) {
				Locale locale = new Locale(args[i].substring(6));
				initFactory(locale);
			} else if (dbFile == null) {
				dbFile = new File(args[i]);
			} else {
				throw new IllegalArgumentException("Illegal argument: " + args[i]);
			}
		}

		if (dbFile == null) {
			throw new IllegalArgumentException("Path to database file is missing.");
		}
	}

	private void registerDAOs() {
		DaoFactory.register(LogMessageDAO.class, new SingleFileLogMessageDAO(dbFile));
	}

	private void initFrame() {
		mainFrame = new MainFrame();
		mainFrame.setVisible(true);
		SwingUtils.center(mainFrame);
		mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

}
