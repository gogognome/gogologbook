package nl.gogognome.gogologbook.gui;

import nl.gogognome.gogologbook.entities.Category;
import nl.gogognome.gogologbook.entities.Project;
import nl.gogognome.gogologbook.entities.User;
import nl.gogognome.lib.swing.models.ListModel;
import nl.gogognome.lib.swing.models.StringModel;

public class LogMessageCreateModel {

	public ListModel<User> usersModel = new ListModel<User>();
	public ListModel<Project> projectsModel = new ListModel<Project>();
	public StringModel townModel = new StringModel();
	public ListModel<Category> categoriesModel = new ListModel<Category>();
	public StringModel messageModel = new StringModel();

	public StringModel resultModel = new StringModel();
}
