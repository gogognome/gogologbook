package nl.gogognome.gogologbook.gui;

import java.util.Collections;
import java.util.List;

import nl.gogognome.gogologbook.interactors.boundary.LogMessageFindResult;
import nl.gogognome.lib.swing.AbstractListTableModel;
import nl.gogognome.lib.swing.ColumnDefinition;

import com.google.common.collect.Lists;

public class LogMessageTableModel extends AbstractListTableModel<LogMessageFindResult> {

	private static final long serialVersionUID = 1L;

	private final static ColumnDefinition USERNAME = new ColumnDefinition("LogEntryTableModel_username", String.class, 100);

	private final static ColumnDefinition PROJECT = new ColumnDefinition("LogEntryTableModel_project", String.class, 100);

	private final static ColumnDefinition TOWN = new ColumnDefinition("LogEntryTableModel_town", String.class, 100);

	private final static ColumnDefinition STREET = new ColumnDefinition("LogEntryTableModel_street", String.class, 100);

	private final static ColumnDefinition CATEGORY = new ColumnDefinition("LogEntryTableModel_category", String.class, 100);

	private final static ColumnDefinition MESSAGE = new ColumnDefinition("LogEntryTableModel_message", String.class, 300);

	private final static List<ColumnDefinition> COLUMN_DEFINTIIONS = Lists.newArrayList(USERNAME, PROJECT, TOWN, STREET, CATEGORY, MESSAGE);

	public LogMessageTableModel() {
		super(COLUMN_DEFINTIIONS, Collections.<LogMessageFindResult> emptyList());
	}

	public void setLogMessages(List<LogMessageFindResult> logMessages) {
		replaceRows(logMessages);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ColumnDefinition colDef = COLUMN_DEFINTIIONS.get(columnIndex);
		LogMessageFindResult logMessage = getRow(rowIndex);

		if (USERNAME == colDef) {
			return logMessage.username;
		}
		if (PROJECT == colDef) {
			return logMessage.projectNr;
		}
		if (TOWN == colDef) {
			return logMessage.town;
		}
		if (STREET == colDef) {
			return logMessage.street;
		}
		if (CATEGORY == colDef) {
			return logMessage.category;
		}
		if (MESSAGE == colDef) {
			return logMessage.message;
		}
		return null;
	}
}
