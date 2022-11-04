package src.application.client;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class MainScreen extends AbstractScreen {
	public static final String SCREEN_LAYOUT = "src/resource/stylesheets/MainScreen.fxml";
	
	@FXML 
	private StackPane toolbar;
	
	@FXML
	private BorderPane home_canvas;
	
	@FXML
	private BorderPane book_canvas;
	
	@FXML
	private BorderPane borrower_canvas;
	
	@FXML
	private MenuButton settings_button;
	
	@FXML
	private MenuItem increment_dropdown;
	
	@FXML
	private MenuItem logout_dropdown;
	
	@FXML
	public void initialize() {
		super.initialize();
		this.home_canvas.managedProperty().bind(this.home_canvas.visibleProperty());
		this.book_canvas.managedProperty().bind(this.book_canvas.visibleProperty());
		this.borrower_canvas.managedProperty().bind(this.borrower_canvas.visibleProperty());
		OnHomeClicked();
	}
	
	public void OnHomeClicked() {
		this.book_canvas.setVisible(false);
		this.borrower_canvas.setVisible(false);
		this.home_canvas.setVisible(true);
	}
	
	public void OnBookClicked() {
		this.home_canvas.setVisible(false);
		this.book_canvas.setVisible(true);
	}
	
	public void OnBorrowerClicked() {
		this.home_canvas.setVisible(false);
		this.borrower_canvas.setVisible(true);
	}
	
	public void OnIncrementDate() {
		
	}
	
	public void OnLogout() {
		SceneManager.getSingleton().loadScene(Scenes.LOGIN_SCREEN, SceneManager.STYLE, 1200, 800);
	}
}
