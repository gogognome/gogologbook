package nl.gogognome.gogologbook.gui.logmessage;

import java.awt.BorderLayout;

import nl.gogognome.lib.swing.views.View;

public class LogMessageCreateAndOverviewView extends View {

	private static final long serialVersionUID = 1L;

	private LogMessageCreateView logMessageCreateView;
	private LogMessageOverviewView logMessageOverviewView;

	@Override
	public String getTitle() {
		return textResource.getString("logMessageCreateAndOverviewView.title");
	}

	@Override
	public void onInit() {
		logMessageCreateView = new LogMessageCreateView();
		logMessageCreateView.onInit();
		logMessageOverviewView = new LogMessageOverviewView();
		logMessageOverviewView.onInit();

		setLayout(new BorderLayout());
		add(logMessageCreateView, BorderLayout.NORTH);
		add(logMessageOverviewView, BorderLayout.CENTER);
	}

	@Override
	public void onClose() {
		logMessageCreateView.onClose();
		logMessageOverviewView.onClose();
	}

}
