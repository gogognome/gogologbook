package nl.gogognome.gogologbook.gui.user;

import nl.gogognome.gogologbook.gui.session.SessionManager;
import nl.gogognome.gogologbook.interactors.UserInteractor;
import nl.gogognome.gogologbook.interactors.boundary.InteractorFactory;
import nl.gogognome.gogologbook.interactors.boundary.UserCreateParams;
import nl.gogognome.lib.swing.MessageDialog;

import org.slf4j.LoggerFactory;

public class AddNewUserController extends AbstractEditUserController {

    private final UserInteractor userInteractor = InteractorFactory.getInteractor(UserInteractor.class);

    public AddNewUserController() {
        model = new AddNewUserModel();
        model.activeModel.setBoolean(true);
    }

    @Override
    public void save() {
        UserCreateParams params = new UserCreateParams();
        params.name = model.nameModel.getString();
        params.active = model.activeModel.getBoolean();

        try {
            userInteractor.createUser(params);
            closeAction.actionPerformed(null);
            SessionManager.getInstance().notifyListeners(new UserChangedEvent());
        } catch (Exception e) {
            LoggerFactory.getLogger(getClass()).warn("Failed to create user", e);
            MessageDialog.showErrorMessage(model.parent, "editUser_failedToCreateUser", e.getMessage());
        }
    }

}
