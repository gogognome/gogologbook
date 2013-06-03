package nl.gogognome.gogologbook.gui.logmessage;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import nl.gogognome.gogologbook.interactors.boundary.LogMessageFindResult;
import nl.gogognome.lib.gui.beans.InputFieldsColumn;
import nl.gogognome.lib.swing.ButtonPanel;
import nl.gogognome.lib.swing.views.View;

public class LogMessageEditView extends View {

	private static final long serialVersionUID = 1L;

	private final LogMessageEditController controller = new LogMessageEditController(this);
	private final LogMessageEditModel model = controller.getModel();

	public LogMessageEditView(LogMessageFindResult logMessageUnderEdit) {
		controller.setLogMessageUnderEdit(logMessageUnderEdit);
	}

	@Override
	public String getTitle() {
		return textResource.getString("logMessageEditView_title");
	}

	@Override
	public void onInit() {
		addComponents();
		controller.setCloseAction(closeAction);
	}

	@Override
	public void onClose() {}

	protected void addComponents() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel buttonPanel = createButtonPanel();
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

		add(createCenterComponent(), BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	private JPanel createButtonPanel() {
		ButtonPanel panel = new ButtonPanel(SwingConstants.LEFT);
		panel.addButton("gen.ok", controller.getOkAction());
		panel.addButton("gen.cancel", closeAction);
		return panel;
	}

	protected JComponent createCenterComponent() {
		InputFieldsColumn ifc = new InputFieldsColumn();
		addCloseable(ifc);

		ifc.addComboBoxField("logMessageCreateView_username", model.usersModel, new UserFormatter());
		ifc.addComboBoxField("logMessageCreateView_project", model.projectsModel, new ProjectFormatter());
		ifc.addComboBoxField("logMessageCreateView_category", model.categoriesModel, new CategoryFormatter());
		ifc.addTetxtArea("logMessageCreateView_message", model.messageModel);

		return ifc;
	}
}
