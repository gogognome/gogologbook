package nl.gogognome.gogologbook.gui;

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

	private final LogMessageCreateController controller = new LogMessageCreateController();
	private final LogMessageCreateModel model = controller.getModel();

	@Override
	public String getTitle() {
		return textResource.getString("logMessageCreateView_title");
	}

	@Override
	public void onInit() {
		addComponents();
	}

	@Override
	public void onClose() {
	}

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
		panel.add(beanFactory.createLabelBean(model.resultModel));
		return panel;
	}

	protected JComponent createCenterComponent() {
        InputFieldsColumn ifc = new InputFieldsColumn();
        addCloseable(ifc);

        ifc.addField("logMessageCreateView_username", model.usernameModel);
        ifc.addField("logMessageCreateView_project", model.projectModel);
        ifc.addField("logMessageCreateView_town", model.townModel);
        ifc.addField("logMessageCreateView_category", model.categoryModel);
        ifc.addField("logMessageCreateView_message", model.messageModel);

		return ifc;
	}

}
