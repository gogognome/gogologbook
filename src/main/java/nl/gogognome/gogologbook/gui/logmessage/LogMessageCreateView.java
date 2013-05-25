package nl.gogognome.gogologbook.gui.logmessage;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import nl.gogognome.gogologbook.entities.Category;
import nl.gogognome.gogologbook.entities.User;
import nl.gogognome.gogologbook.interactors.boundary.ProjectFindResult;
import nl.gogognome.lib.gui.beans.InputFieldsColumn;
import nl.gogognome.lib.gui.beans.ObjectFormatter;
import nl.gogognome.lib.swing.ActionWrapper;
import nl.gogognome.lib.swing.ButtonPanel;
import nl.gogognome.lib.swing.views.View;
import nl.gogognome.lib.util.StringUtil;

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
		panel.add(beanFactory.createLabelBean(model.resultModel));
		return panel;
	}

	protected JComponent createCenterComponent() {
		InputFieldsColumn ifc = new InputFieldsColumn();
		addCloseable(ifc);

		ifc.addComboBoxField("logMessageCreateView_username", model.usersModel, new UserFormatter());
		ifc.addComboBoxField("logMessageCreateView_project", model.projectsModel, new ProjectFormatter());
		ifc.addComboBoxField("logMessageCreateView_category", model.categoriesModel, new CategoryFormatter());
		ifc.addField("logMessageCreateView_message", model.messageModel);

		return ifc;
	}
}

class UserFormatter implements ObjectFormatter<User> {
	@Override
	public String format(User user) {
		return user != null ? user.name : "";
	}
}

class CategoryFormatter implements ObjectFormatter<Category> {
	@Override
	public String format(Category category) {
		return category != null ? category.name : "";
	}
}

class ProjectFormatter implements ObjectFormatter<ProjectFindResult> {
	@Override
	public String format(ProjectFindResult project) {
		return project != null ? StringUtil.nullToEmptyString(project.projectNr) + " " + StringUtil.nullToEmptyString(project.customer) + " "
				+ StringUtil.nullToEmptyString(project.town) + " " + StringUtil.nullToEmptyString(project.street) : "";
	}
}
