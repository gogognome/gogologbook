package nl.gogognome.gogologbook.entities;

import nl.gogognome.gogologbook.util.AbstractEntity;

public class Category extends AbstractEntity {

	public Category() {
		super();
	}

	public Category(int id) {
		super(id);
	}

	public String name;
}
