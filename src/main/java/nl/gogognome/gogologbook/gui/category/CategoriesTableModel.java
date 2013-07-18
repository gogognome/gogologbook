package nl.gogognome.gogologbook.gui.category;

import java.util.Collections;
import java.util.List;

import nl.gogognome.gogologbook.entities.Category;
import nl.gogognome.lib.swing.table.AbstractListTableModel;
import nl.gogognome.lib.swing.table.ColumnDefinition;

import com.google.common.collect.Lists;

public class CategoriesTableModel extends AbstractListTableModel<Category> {

	private static final long serialVersionUID = 1L;

	private final static ColumnDefinition NAME = new ColumnDefinition("categoriesTableModel_name", String.class, 200);

	private final static List<ColumnDefinition> COLUMN_DEFINTIIONS = Lists.newArrayList(NAME);

	public CategoriesTableModel() {
		super(COLUMN_DEFINTIIONS, Collections.<Category> emptyList());
	}

	public void setCategories(List<Category> categories) {
		replaceRows(categories);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ColumnDefinition colDef = COLUMN_DEFINTIIONS.get(columnIndex);
		Category category = getRow(rowIndex);

		if (NAME == colDef) {
			return category.name;
		}
		return null;
	}

}
