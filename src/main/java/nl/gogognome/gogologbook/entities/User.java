package nl.gogognome.gogologbook.entities;

import nl.gogognome.gogologbook.util.AbstractEntity;

public class User extends AbstractEntity {

	public User() {
		super();
	}

	public User(int id) {
		super(id);
	}

	public String name;
}
