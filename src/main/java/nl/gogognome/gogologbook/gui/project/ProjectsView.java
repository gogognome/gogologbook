package nl.gogognome.gogologbook.gui.project;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import nl.gogognome.lib.swing.ButtonPanel;
import nl.gogognome.lib.swing.views.View;

public class ProjectsView extends View {

	private static final long serialVersionUID = 1L;

	private final ProjectController controller = new ProjectController(this);
	private final ProjectsModel model = controller.getModel();

	@Override
	public String getTitle() {
		return textResource.getString("projects_title");
	}

	@Override
	public void onInit() {
		addCloseable(controller);
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JTable table = widgetFactory.createSortedTable(model.projectsTableModel);
		table.setSelectionModel(model.selectionModel);
		add(widgetFactory.createScrollPane(table), BorderLayout.CENTER);

		ButtonPanel buttonPanel = new ButtonPanel(SwingConstants.LEFT);
		buttonPanel.addButton("projects_add", controller.getAddAction());
		buttonPanel.addButton("projects_edit", controller.getEditAction());
		buttonPanel.addButton("projects_delete", controller.getDeleteAction());
		add(buttonPanel, BorderLayout.SOUTH);
	}

	@Override
	public void onClose() {}

}
