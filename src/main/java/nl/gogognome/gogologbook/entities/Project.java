package nl.gogognome.gogologbook.entities;

import nl.gogognome.gogologbook.util.AbstractEntity;

public class Project extends AbstractEntity {

	public Project() {
		super();
	}

	public Project(int id) {
		super(id);
	}

	public String projectNr;
	public String customer;
	public String street;
	public String town;
}
