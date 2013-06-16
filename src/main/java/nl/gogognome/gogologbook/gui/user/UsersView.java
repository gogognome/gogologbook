package nl.gogognome.gogologbook.gui.user;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import nl.gogognome.lib.swing.ButtonPanel;
import nl.gogognome.lib.swing.views.View;

public class UsersView extends View {

	private static final long serialVersionUID = 1L;

	private final UsersController controller = new UsersController(this);
	private final UsersModel model = controller.getModel();

	@Override
	public String getTitle() {
		return textResource.getString("users_title");
	}

	@Override
	public void onInit() {
		addCloseable(controller);
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JTable table = widgetFactory.createSortedTable(model.usersTableModel);
		table.setSelectionModel(model.selectionModel);
		add(widgetFactory.createScrollPane(table), BorderLayout.CENTER);

		ButtonPanel buttonPanel = new ButtonPanel(SwingConstants.LEFT);
		buttonPanel.addButton("users_add", controller.getAddAction());
		buttonPanel.addButton("users_edit", controller.getEditAction());
		buttonPanel.addButton("users_delete", controller.getDeleteAction());
		add(buttonPanel, BorderLayout.SOUTH);
	}

	@Override
	public void onClose() {}

}
