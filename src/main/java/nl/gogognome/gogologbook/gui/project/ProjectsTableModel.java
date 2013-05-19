package nl.gogognome.gogologbook.gui.project;

import java.util.Collections;
import java.util.List;

import nl.gogognome.gogologbook.interactors.boundary.ProjectFindResult;
import nl.gogognome.lib.swing.AbstractListTableModel;
import nl.gogognome.lib.swing.ColumnDefinition;

import com.google.common.collect.Lists;

public class ProjectsTableModel extends AbstractListTableModel<ProjectFindResult> {

	private static final long serialVersionUID = 1L;

	private final static ColumnDefinition NUMBER = new ColumnDefinition("projectsTableModel_number", String.class, 100);
	private final static ColumnDefinition CUSTOMER = new ColumnDefinition("projectsTableModel_customer", String.class, 200);
	private final static ColumnDefinition TOWN = new ColumnDefinition("projectsTableModel_town", String.class, 100);
	private final static ColumnDefinition STREET = new ColumnDefinition("projectsTableModel_street", String.class, 100);

	private final static List<ColumnDefinition> COLUMN_DEFINTIIONS = Lists.newArrayList(NUMBER, CUSTOMER, TOWN, STREET);

	public ProjectsTableModel() {
		super(COLUMN_DEFINTIIONS, Collections.<ProjectFindResult> emptyList());
	}

	public void setProjects(List<ProjectFindResult> projects) {
		replaceRows(projects);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ColumnDefinition colDef = COLUMN_DEFINTIIONS.get(columnIndex);
		ProjectFindResult project = getRow(rowIndex);

		if (NUMBER == colDef) {
			return project.projectNr;
		}
		if (CUSTOMER == colDef) {
			return project.customer;
		}
		if (TOWN == colDef) {
			return project.town;
		}
		if (STREET == colDef) {
			return project.street;
		}
		return null;
	}

}
