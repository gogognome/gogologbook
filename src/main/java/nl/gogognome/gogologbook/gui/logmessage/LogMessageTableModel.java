package nl.gogognome.gogologbook.gui.logmessage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import nl.gogognome.gogologbook.interactors.boundary.LogMessageFindResult;
import nl.gogognome.lib.swing.table.AbstractListTableModel;
import nl.gogognome.lib.swing.table.ColumnDefinition;
import nl.gogognome.lib.swing.table.MultilineCellRenderer;

import com.google.common.collect.Lists;

public class LogMessageTableModel extends AbstractListTableModel<LogMessageFindResult> {

	private static final long serialVersionUID = 1L;

	private final static ColumnDefinition TIMESTAMP = new ColumnDefinition.Builder("LogEntryTableModel_timestamp", String.class, 100)
			.add(new DateComparator()).build();

	private final static ColumnDefinition USERNAME = new ColumnDefinition("LogEntryTableModel_username", String.class, 100);

	private final static ColumnDefinition PROJECT = new ColumnDefinition("LogEntryTableModel_project", String.class, 100);

	private final static ColumnDefinition CUSTOMER = new ColumnDefinition("LogEntryTableModel_customer", String.class, 100);

	private final static ColumnDefinition TOWN = new ColumnDefinition("LogEntryTableModel_town", String.class, 100);

	private final static ColumnDefinition STREET = new ColumnDefinition("LogEntryTableModel_street", String.class, 100);

	private final static ColumnDefinition CATEGORY = new ColumnDefinition("LogEntryTableModel_category", String.class, 100);

	private final static ColumnDefinition MESSAGE = new ColumnDefinition.Builder("LogEntryTableModel_message", String.class, 500)
			.add(new MultilineCellRenderer()).build();

	private final static List<ColumnDefinition> COLUMN_DEFINTIIONS = Lists
			.newArrayList(TIMESTAMP, USERNAME, PROJECT, CUSTOMER, TOWN, STREET, CATEGORY, MESSAGE);

	private final static DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

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

		if (TIMESTAMP == colDef) {
			return logMessage.timestamp != null ? dateFormat.format(logMessage.timestamp) : null;
		}
		if (USERNAME == colDef) {
			return logMessage.username;
		}
		if (PROJECT == colDef) {
			return logMessage.projectNr;
		}
		if (CUSTOMER == colDef) {
			return logMessage.customer;
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

	private static class DateComparator implements Comparator<Object> {

		@Override
		public int compare(Object o1, Object o2) {
			Date d1;
			try {
				d1 = dateFormat.parse((String) o1);
			} catch (ParseException e) {
				d1 = null;
			}
			Date d2;
			try {
				d2 = dateFormat.parse((String) o2);
			} catch (ParseException e) {
				d2 = null;
			}

			if (d1 == null && d2 == null) {
				return 0;
			}
			if (d1 == null) {
				return 1;
			}
			if (d2 == null) {
				return -1;
			}

			return d1.compareTo(d2);
		}

	}
}
