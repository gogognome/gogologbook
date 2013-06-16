package nl.gogognome.gogologbook.gui.logmessage;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import nl.gogognome.lib.gui.beans.InputFieldsRow;
import nl.gogognome.lib.swing.ButtonPanel;
import nl.gogognome.lib.swing.SwingUtils;
import nl.gogognome.lib.swing.views.View;

public class LogMessageOverviewView extends View {

	private static final long serialVersionUID = 1L;

	private final LogMessageOverviewController controller = new LogMessageOverviewController(this);
	private final LogMessageOverviewModel model = controller.getModel();

	@Override
	public String getTitle() {
		return textResource.getString("logMessageOverview_title");
	}

	@Override
	public void onInit() {
		addCloseable(controller);
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JTable table = widgetFactory.createSortedTable(model.logMessageTableModel);
		table.setSelectionModel(controller.getModel().selectionModel);
		add(widgetFactory.createScrollPane(table), BorderLayout.CENTER);

		ButtonPanel buttonPanel = new ButtonPanel(SwingConstants.LEFT);
		buttonPanel.addButton("logMessageOverview_refresh", controller.getRefreshAction());
		buttonPanel.addButton("logMessageOverview_edit", controller.getEditAction());
		add(buttonPanel, BorderLayout.SOUTH);

		InputFieldsRow searchCriteria = new InputFieldsRow();
		searchCriteria.setBorder(new EmptyBorder(0, 0, 10, 0));
		searchCriteria.addDateField("logMessageOverview_fromDate", model.fromDate);
		searchCriteria.addDateField("logMessageOverview_toDate", model.toDate);
		searchCriteria.addComboBoxField("logMessageOverview_user", model.usersModel, new UserFormatter());
		searchCriteria.addField("logMessageOverview_project", model.project);
		searchCriteria.addField("logMessageOverview_customer", model.customer);
		searchCriteria.addField("logMessageOverview_town", model.town);
		searchCriteria.addComboBoxField("logMessageOverview_category", model.categoriesModel, new CategoryFormatter());
		searchCriteria.addField("logMessageOverview_message", model.message);
		searchCriteria.add(widgetFactory.createButton("logMessageOverview_filter", controller.getFilterAction()),
				SwingUtils.createLabelGBConstraints(22, 0));
		add(searchCriteria, BorderLayout.NORTH);
	}

	@Override
	public void onClose() {}

}
