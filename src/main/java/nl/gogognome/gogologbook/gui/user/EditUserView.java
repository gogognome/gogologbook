package nl.gogognome.gogologbook.gui.user;

import nl.gogognome.gogologbook.entities.User;

public class EditUserView extends AbstractEditUserView {

	private static final long serialVersionUID = 1L;

	public EditUserView(User user) {
		controller = new EditUserController(user);
		model = controller.getModel();
	}

	@Override
	public String getTitle() {
		return textResource.getString("edit_user_title");
	}

}
