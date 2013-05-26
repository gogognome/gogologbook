package nl.gogognome.gogologbook.gui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import nl.gogognome.gogologbook.gui.MenuBarCreator.ActionListeners;
import nl.gogognome.gogologbook.gui.logmessage.LogMessageCreateAndOverviewView;
import nl.gogognome.gogologbook.gui.project.ProjectsView;
import nl.gogognome.lib.swing.MessageDialog;
import nl.gogognome.lib.swing.views.View;
import nl.gogognome.lib.swing.views.ViewDialog;
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
			public void windowClosing(WindowEvent e) {
				handleExit();
			}
		});

		setTitle(createTitle());

		setIcon("icon-32x32.png");

		setMinimumSize(new Dimension(800, 600));

		setJMenuBar(new MenuBarCreator().createMenuBar(getMenuListeners()));
		openView(LogMessageCreateAndOverviewView.class);
	}

	private ActionListeners getMenuListeners() {
		ActionListeners listeners = new ActionListeners();
		listeners.openAboutView = new OpenViewInDialogAction(AboutView.class);
		listeners.openLogMessageCreateView = new OpenViewAction(LogMessageCreateAndOverviewView.class);
		listeners.openProjectsView = new OpenViewAction(ProjectsView.class);

		listeners.exit = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		};

		return listeners;
	}

	private void setIcon(String iconName) {
		URL url = ClassLoader.getSystemResource(iconName);
		Image image = Toolkit.getDefaultToolkit().createImage(url);
		setIconImage(image);
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

	private class OpenViewAction implements ActionListener {
		private final Class<? extends View> viewClass;

		public OpenViewAction(Class<? extends View> viewClass) {
			this.viewClass = viewClass;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			openView(viewClass);
		}
	}

	private class OpenViewInDialogAction implements ActionListener {
		private final Class<? extends View> viewClass;

		public OpenViewInDialogAction(Class<? extends View> viewClass) {
			this.viewClass = viewClass;
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				new ViewDialog(MainFrame.this, viewClass.newInstance()).showDialog();
			} catch (Exception e) {
				MessageDialog.showErrorMessage(MainFrame.this, "gen.problem_occurred", e);
			}
		}
	}

}