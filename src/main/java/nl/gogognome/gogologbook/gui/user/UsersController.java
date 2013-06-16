package nl.gogognome.gogologbook.gui.user;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ListSelectionModel;

import nl.gogognome.gogologbook.entities.User;
import nl.gogognome.gogologbook.gui.session.SessionChangeEvent;
import nl.gogognome.gogologbook.gui.session.SessionListener;
import nl.gogognome.gogologbook.gui.session.SessionManager;
import nl.gogognome.gogologbook.interactors.UserInteractor;
import nl.gogognome.gogologbook.interactors.boundary.InteractorFactory;
import nl.gogognome.lib.gui.Closeable;
import nl.gogognome.lib.swing.MessageDialog;
import nl.gogognome.lib.swing.views.ViewDialog;

public class UsersController implements Closeable, SessionListener {

	private final UsersModel model = new UsersModel();
	private final Component parent;
	private final UserInteractor userInteractor = InteractorFactory.getInteractor(UserInteractor.class);

	public UsersController(Component parent) {
		this.parent = parent;
		refreshUsers();
		model.selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		SessionManager.getInstance().addSessionListener(this);
	}

	public UsersModel getModel() {
		return model;
	}

	@Override
	public void close() {
		SessionManager.getInstance().removeSessionListener(this);
	}

	@Override
	public void sessionChanged(SessionChangeEvent event) {
		if (event instanceof UserChangedEvent || event instanceof UserDeletedEvent) {
			refreshUsers();
		}
	}

	private void refreshUsers() {
		List<User> users = userInteractor.findAllUsers();
		model.usersTableModel.setUsers(users);
	}

	public Action getAddAction() {
		return new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onAddNewUser();
			}
		};
	}

	public Action getEditAction() {
		return new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onEditSelectedUser();
			}
		};
	}

	public Action getDeleteAction() {
		return new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onDeleteSelectedUser();
			}
		};
	}

	private void onAddNewUser() {
		AddNewUserView view = new AddNewUserView();
		new ViewDialog(parent, view).showDialog();
	}

	private void onEditSelectedUser() {
		int index = model.selectionModel.getMaxSelectionIndex();
		if (index == -1) {
			MessageDialog.showInfoMessage(parent, "editUser_selectRowFirst");
			return;
		}

		User user = model.usersTableModel.getRow(index);
		EditUserView view = new EditUserView(user);
		new ViewDialog(parent, view).showDialog();
	}

	private void onDeleteSelectedUser() {
		int index = model.selectionModel.getMaxSelectionIndex();
		if (index == -1) {
			MessageDialog.showInfoMessage(parent, "editUser_selectRowFirst");
			return;
		}

		User user = model.usersTableModel.getRow(index);
		int choice = MessageDialog.showYesNoQuestion(parent, "gen.confirmation", "editUser_confirm_delete_user", user.name);
		if (choice == MessageDialog.YES_OPTION) {
			//			try {
			//				userInteractor.deleteUser(user.id);
			//				SessionManager.getInstance().notifyListeners(new UserDeletedEvent(user.id));
			//			} catch (CannotDeleteUserThatIsInUseException e) {
			//				MessageDialog.showWarningMessage(parent, "editUser_cannotDeleteUserBecauseItIsUsed", user.name);
			//			}
		}

	}

}
