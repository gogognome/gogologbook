package nl.gogognome.gogologbook.util;

public class AbstractEntity {

	public final int id;

	public AbstractEntity() {
		this(-1);
	}

	public AbstractEntity(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "-" + id;
	}
}
