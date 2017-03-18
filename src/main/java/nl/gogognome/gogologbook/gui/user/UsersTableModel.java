package nl.gogognome.gogologbook.gui.user;

import java.util.Collections;
import java.util.List;

import nl.gogognome.gogologbook.entities.User;
import nl.gogognome.lib.swing.table.AbstractListTableModel;
import nl.gogognome.lib.swing.table.ColumnDefinition;

import com.google.common.collect.Lists;

public class UsersTableModel extends AbstractListTableModel<User> {

    private static final long serialVersionUID = 1L;

    private final static ColumnDefinition NAME = new ColumnDefinition("usersTableModel_name", String.class, 200);
    private final static ColumnDefinition ACTIVE = new ColumnDefinition("usersTableModel_active", Boolean.class, 20);

    private final static List<ColumnDefinition> COLUMN_DEFINTIIONS = Lists.newArrayList(NAME, ACTIVE);

    public UsersTableModel() {
        super(COLUMN_DEFINTIIONS, Collections.<User> emptyList());
    }

    public void setUsers(List<User> users) {
        replaceRows(users);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ColumnDefinition colDef = COLUMN_DEFINTIIONS.get(columnIndex);
        User user = getRow(rowIndex);

        if (NAME == colDef) {
            return user.name;
        }
        if (ACTIVE == colDef) {
            return user.active;
        }

        return null;
    }

}
