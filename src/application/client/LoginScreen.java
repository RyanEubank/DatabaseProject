package src.application.client;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.text.Text;

public class LoginScreen implements Initializable {

	private static String IDLE_BUTTON_STYLE = "-fx-background-color: #6085b7; -fx-background-radius: 15px";
	private static String HOVER_BUTTON_STYLE = "-fx-background-color: #91ceff; -fx-background-radius: 15px; -fx-border-color: #000000; -fx-border-radius: 15px";
	private static String CLICKED_BUTTON_STYLE = "-fx-background-color: #767676; -fx-background-radius: 15px;";
	
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
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		login_button.setStyle(IDLE_BUTTON_STYLE);
		login_button.setOnMouseEntered(event -> login_button.setStyle(HOVER_BUTTON_STYLE));
		login_button.setOnMouseExited(event -> login_button.setStyle(IDLE_BUTTON_STYLE));
	}
	
	public void onLoginClicked() {
		
		// set button color to grey and disable color updates while login is processed
		login_button.setStyle(CLICKED_BUTTON_STYLE);
		login_button.setOnMouseEntered(event -> login_button.setStyle(CLICKED_BUTTON_STYLE));
		login_button.setOnMouseExited(event -> login_button.setStyle(CLICKED_BUTTON_STYLE));
		
		// process login information -> change scenes if login
		System.out.println("hello");
	}
}
