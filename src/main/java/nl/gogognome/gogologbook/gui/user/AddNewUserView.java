package nl.gogognome.gogologbook.gui.user;


public class AddNewUserView extends AbstractEditUserView {

	private static final long serialVersionUID = 1L;

	public AddNewUserView() {
		controller = new AddNewUserController();
		model = controller.getModel();
	}

	@Override
	public String getTitle() {
		return textResource.getString("add_user_title");
	}
}
