package nl.gogognome.gogologbook.gui.logmessage;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import nl.gogognome.lib.gui.beans.InputFieldsColumn;
import nl.gogognome.lib.swing.ActionWrapper;
import nl.gogognome.lib.swing.ButtonPanel;
import nl.gogognome.lib.swing.views.View;

public class LogMessageCreateView extends View {

	private static final long serialVersionUID = 1L;

	private final LogMessageCreateController controller = new LogMessageCreateController(this);
	private final LogMessageCreateModel model = controller.getModel();

	@Override
	public String getTitle() {
		return textResource.getString("logMessageCreateView_title");
	}

	@Override
	public void onInit() {
		addCloseable(controller);
		addComponents();
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
		ActionWrapper actionWrapper = widgetFactory.createAction("logMessageCreateView_add");
		actionWrapper.setAction(controller.getCreateAction());
		panel.addButton("logMessageCreateView_add", actionWrapper);
		return panel;
	}

	protected JComponent createCenterComponent() {
		InputFieldsColumn ifc = new InputFieldsColumn();
		addCloseable(ifc);

		ifc.addField("logMessageCreateView_specifiyTimestampManually", model.manuallySpecifyTimestamp);
		ifc.addTimestampFieldWithMinuteAccuracy("logMessageCreateView_timestamp", model.manuallySpecifiedTimestamp);
		ifc.addComboBoxField("logMessageCreateView_username", model.usersModel, new UserFormatter());
		ifc.addComboBoxField("logMessageCreateView_project", model.projectsModel, new ProjectFormatter());
		ifc.addComboBoxField("logMessageCreateView_category", model.categoriesModel, new CategoryFormatter());
		ifc.addTetxtArea("logMessageCreateView_message", model.messageModel);

		return ifc;
	}
}
