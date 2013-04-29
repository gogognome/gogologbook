package nl.gogognome.gogologbook.gui;

import java.awt.BorderLayout;

import javax.swing.JTable;
import javax.swing.SwingConstants;

import nl.gogognome.lib.swing.ButtonPanel;
import nl.gogognome.lib.swing.views.View;

public class LogMessageOverviewView extends View {

	private static final long serialVersionUID = 1L;

	private final LogMessageOverviewController controller = new LogMessageOverviewController();
	private final LogMessageOverviewModel model = controller.getModel();

	@Override
	public String getTitle() {
		return textResource.getString("logMessageOverview_title");
	}

	@Override
	public void onInit() {
		setLayout(new BorderLayout());

		ButtonPanel buttonPanel = new ButtonPanel(SwingConstants.LEFT);
		buttonPanel.addButton("logMessageOverview_refresh", controller.getRefreshAction());
		add(buttonPanel, BorderLayout.NORTH);

		JTable table = widgetFactory.createSortedTable(model.logMessageTableModel);
        add(widgetFactory.createScrollPane(table), BorderLayout.CENTER);
	}

	@Override
	public void onClose() {
	}

}
