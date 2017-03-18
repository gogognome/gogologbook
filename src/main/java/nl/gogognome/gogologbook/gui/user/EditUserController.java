package nl.gogognome.gogologbook.gui.user;

import nl.gogognome.gogologbook.entities.User;
import nl.gogognome.gogologbook.gui.session.SessionManager;
import nl.gogognome.gogologbook.interactors.UserInteractor;
import nl.gogognome.gogologbook.interactors.boundary.InteractorFactory;
import nl.gogognome.gogologbook.interactors.boundary.UserUpdateParams;
import nl.gogognome.lib.swing.MessageDialog;

import org.slf4j.LoggerFactory;

public class EditUserController extends AbstractEditUserController {

	private final UserInteractor userInteractor = InteractorFactory.getInteractor(UserInteractor.class);

	public EditUserController(User user) {
		model = new EditUserModel();

		((EditUserModel) model).userUnderEdit = user;
		model.nameModel.setString(user.name);
		model.activeModel.setBoolean(user.active);
	}

	@Override
	public void save() {
		UserUpdateParams params = new UserUpdateParams();
		params.userId = ((EditUserModel) model).userUnderEdit.id;
		params.name = model.nameModel.getString();
		params.active = model.activeModel.getBoolean();

		try {
			userInteractor.updateUser(params);
			closeAction.actionPerformed(null);
			SessionManager.getInstance().notifyListeners(new UserChangedEvent());
		} catch (Exception e) {
			LoggerFactory.getLogger(getClass()).warn("Failed to update user " + params.userId, e);
			MessageDialog.showErrorMessage(model.parent, "editUser_failedToUpdateUser", e.getMessage());
		}
	}

}
