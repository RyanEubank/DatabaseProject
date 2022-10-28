package src.application.client;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import src.application.server.*;
import src.application.server.network.LoginHandler;

public class LoginScreen {

	private static final String EMPTY_LOGIN = "Please enter a username and password.";
	private static final String INVALID_LOGIN = "Invalid username or password.";
	private static final String SERVER_UNAVAILABLE = "Database servers are currently down. Please try again later.";
	private static final String ERROR = "An unexpected error has occured. Please see log for details";
	
	@FXML
	private Button login_button;
	
	@FXML
	private Label incorrect_login_label;
	
	@FXML
	private TextField username;
	
	@FXML
	private PasswordField password;
	
	@FXML
	private Text screen_title_top;

	@FXML
	private Text screen_title_bottom;
	
	/**
	 * Retrieves the username and password entered by the uiser and attempts to
	 * make a valid connection with the database and load the main application screen.
	 * Issues such as invalid logins or server availability are displayed above
	 * the login form.
	 */
	public void onLoginClicked() {
		if (username.getText().isBlank() || password.getText().isBlank())
			incorrect_login_label.textProperty().set(EMPTY_LOGIN);
		else 
			tryLogin();
	}

	/**
	 * Attempts to login to the mysql library database. If successful the scene manager
	 * loads the main screen, otherwise displays an error to the user.
	 */
	public void tryLogin() {
			LoginHandler.LoginStatus status = LoginHandler.login(username.getText(), password.getText());
			
			if (status == LoginHandler.LoginStatus.VALID) 
				SceneManager.getSingleton().loadScene(SceneManager.MAIN_SCREEN, SceneManager.STYLE, 1200, 800);
			else if (status == LoginHandler.LoginStatus.INVALID)
				incorrect_login_label.textProperty().set(INVALID_LOGIN);
			else if (status == LoginHandler.LoginStatus.UNAVAILABLE)
				incorrect_login_label.textProperty().set(SERVER_UNAVAILABLE);
			else
				incorrect_login_label.textProperty().set(ERROR);
				
	}
}
