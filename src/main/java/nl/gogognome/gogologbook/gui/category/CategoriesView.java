package nl.gogognome.gogologbook.gui.category;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import nl.gogognome.lib.swing.ButtonPanel;
import nl.gogognome.lib.swing.views.View;

public class CategoriesView extends View {

	private static final long serialVersionUID = 1L;

	private final CategoriesController controller = new CategoriesController(this);
	private final CategoriesModel model = controller.getModel();

	@Override
	public String getTitle() {
		return textResource.getString("categories_title");
	}

	@Override
	public void onInit() {
		addCloseable(controller);
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JTable table = widgetFactory.createSortedTable(model.categoriesTableModel);
		table.setSelectionModel(model.selectionModel);
		add(widgetFactory.createScrollPane(table), BorderLayout.CENTER);

		ButtonPanel buttonPanel = new ButtonPanel(SwingConstants.LEFT);
		buttonPanel.addButton("categories_add", controller.getAddAction());
		buttonPanel.addButton("categories_edit", controller.getEditAction());
		buttonPanel.addButton("categories_delete", controller.getDeleteAction());
		add(buttonPanel, BorderLayout.SOUTH);
	}

	@Override
	public void onClose() {}

}
