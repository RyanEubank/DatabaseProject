package src.application.client.scenes.controllers;

import javafx.fxml.*;
import javafx.scene.control.*;
import src.application.client.scenes.SceneManager;
import src.application.client.scenes.Scenes;
import src.application.server.database.query.LoginHandler;

public class LoginScreen extends AbstractWindow {
	
	private static final String EMPTY_LOGIN = "Please enter a username and password.";
	private static final String INVALID_LOGIN = "Invalid username or password.";
	private static final String SERVER_UNAVAILABLE = "Database servers are currently down. Please try again later.";
	private static final String ERROR = "An unexpected error has occured. Please see log for details";

	@FXML
	private Button login_button;
	
	@FXML
	private Label login_warning_label;
	
	@FXML
	private TextField username_field;
	
	@FXML
	private PasswordField password_field;
	
	/**
	 * Sets the prompt text for the username and password fields and registers 
	 * the event handler for the loginbutton and hitting the enter key in the 
	 * text fields.
	 */
	@Override
	public void initialize() {
		super.initialize();
		this.username_field.setPromptText("username");
		this.password_field.setPromptText("password");
		this.login_warning_label.setText("");
		this.login_button.setOnAction(event -> onLogin());
		this.username_field.setOnAction(event -> onLogin());
		this.password_field.setOnAction(event -> onLogin());
	}
	
	/**
	 * Retrieves the username and password entered by the uiser and attempts to
	 * make a valid connection with the database and load the main application 
	 * screen. Issues such as invalid logins or server availability are 
	 * displayed above the login form.
	 */
	private void onLogin() {
		if (username_field.getText().isBlank() || password_field.getText().isBlank())
			login_warning_label.textProperty().set(EMPTY_LOGIN);
		else 
			tryLogin();
	}

	/**
	 * Attempts to login to the mysql library database. If successful the 
	 * scene manager loads the main screen, otherwise displays an error 
	 * to the user.
	 */
	private void tryLogin() {
		LoginHandler.LoginStatus status = new LoginHandler().login(
			username_field.getText(), password_field.getText());
			
		if (status == LoginHandler.LoginStatus.VALID) {
			SceneManager.getSingleton().setMainStage(
				Scenes.MAIN_SCREEN, Scenes.DEFAULT_STYLESHEET, 1200, 800
			);
		} else if (status == LoginHandler.LoginStatus.INVALID)
			login_warning_label.textProperty().set(INVALID_LOGIN);
		else if (status == LoginHandler.LoginStatus.UNAVAILABLE)
			login_warning_label.textProperty().set(SERVER_UNAVAILABLE);
		else
			login_warning_label.textProperty().set(ERROR);
	}
}
