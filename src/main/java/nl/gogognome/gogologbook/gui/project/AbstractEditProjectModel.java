package nl.gogognome.gogologbook.gui.project;

import java.awt.Component;

import nl.gogognome.lib.swing.models.StringModel;

public class AbstractEditProjectModel {

	public Component parent;

	public StringModel projectNumberModel = new StringModel();
	public StringModel customerModel = new StringModel();
	public StringModel townModel = new StringModel();
	public StringModel streetModel = new StringModel();
}
