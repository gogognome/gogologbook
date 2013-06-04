package nl.gogognome.gogologbook.gui.logmessage;

import java.util.Date;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;

import nl.gogognome.gogologbook.entities.Category;
import nl.gogognome.gogologbook.entities.User;
import nl.gogognome.lib.swing.models.DateModel;
import nl.gogognome.lib.swing.models.ListModel;
import nl.gogognome.lib.swing.models.StringModel;
import nl.gogognome.lib.util.DateUtil;

public class LogMessageOverviewModel {

	public LogMessageTableModel logMessageTableModel = new LogMessageTableModel();
	public ListSelectionModel selectionModel = new DefaultListSelectionModel();

	public DateModel fromDate = new DateModel(DateUtil.addDays(new Date(), -14));
	public DateModel toDate = new DateModel(new Date());
	public ListModel<User> usersModel = new ListModel<User>();
	public StringModel project = new StringModel();
	public StringModel customer = new StringModel();
	public StringModel town = new StringModel();
	public ListModel<Category> categoriesModel = new ListModel<Category>();
	public StringModel message = new StringModel();
}
