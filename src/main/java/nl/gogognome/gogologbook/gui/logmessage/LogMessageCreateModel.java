package nl.gogognome.gogologbook.gui.logmessage;

import nl.gogognome.gogologbook.entities.Category;
import nl.gogognome.gogologbook.entities.User;
import nl.gogognome.gogologbook.interactors.boundary.ProjectFindResult;
import nl.gogognome.lib.swing.models.BooleanModel;
import nl.gogognome.lib.swing.models.DateModel;
import nl.gogognome.lib.swing.models.ListModel;
import nl.gogognome.lib.swing.models.StringModel;

public class LogMessageCreateModel {

	public ListModel<User> usersModel = new ListModel<User>();
	public ListModel<ProjectFindResult> projectsModel = new ListModel<ProjectFindResult>();
	public ListModel<Category> categoriesModel = new ListModel<Category>();
	public StringModel messageModel = new StringModel();
	public BooleanModel manuallySpecifyTimestamp = new BooleanModel();
	public DateModel manuallySpecifiedTimestamp = new DateModel();
}
