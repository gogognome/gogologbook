package nl.gogognome.gogologbook.gui.project;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import nl.gogognome.lib.gui.beans.InputFieldsColumn;
import nl.gogognome.lib.swing.ButtonPanel;
import nl.gogognome.lib.swing.views.View;

public abstract class AbstractEditProjectView extends View {

	private static final long serialVersionUID = 1L;

	protected AbstractEditProjectController controller;
	protected AbstractEditProjectModel model;

	@Override
	public void onInit() {
		controller.setCloseAction(closeAction);

		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		InputFieldsColumn ifc = createInputFields();
		add(ifc, BorderLayout.CENTER);

		ButtonPanel buttonPanel = createButtonPanel();
		add(buttonPanel, BorderLayout.SOUTH);
	}

	private InputFieldsColumn createInputFields() {
		InputFieldsColumn ifc = new InputFieldsColumn();
		addCloseable(ifc);

		ifc.addField("editProject_number", model.projectNumberModel);
		ifc.addField("editProject_customer", model.customerModel);
		ifc.addField("editProject_town", model.townModel);
		ifc.addField("editProject_street", model.streetModel);
		return ifc;
	}

	private ButtonPanel createButtonPanel() {
		ButtonPanel buttonPanel = new ButtonPanel(SwingConstants.LEFT);
		buttonPanel.addButton("gen.ok", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onOk();
			}
		});
		buttonPanel.addButton("gen.cancel", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		});
		return buttonPanel;
	}

	private void onOk() {
		controller.save();
	}

	private void onCancel() {
		controller.cancel();
	}

	@Override
	public void onClose() {}

}
