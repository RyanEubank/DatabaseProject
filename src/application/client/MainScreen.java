package src.application.client;

import java.time.LocalDate;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import src.application.server.database.DatabaseUpdater;
import src.application.server.network.ConnectionManager;

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
	private HBox search_bar_box;
	
	@FXML
	private TextField search_bar;
	
	@FXML
	private ChoiceBox<String> search_type;
	
	@FXML
	private Text date;
	
	private String[] book_search_options = { "Any", "ISBN", "Title", "Author" };
	private String[] borrower_search_options = {"Any", "Card No.", "Name", "ISBN"};
	
	/**
	 * Sets up the initial layout and binds the visibility of the book search and
	 * borrow search containers to automaticly resize when toggled.
	 */
	@FXML
	public void initialize() {
		super.initialize();
		// bind visibility and auto resizing
		this.home_canvas.managedProperty().bind(this.home_canvas.visibleProperty());
		this.book_canvas.managedProperty().bind(this.book_canvas.visibleProperty());
		this.borrower_canvas.managedProperty().bind(this.borrower_canvas.visibleProperty());
		
		// set current display date
		this.date.setText(LocalDate.now().toString());
		
		// go to default canvas
		OnHomeClicked();
	}
	
	/**
	 * Sets the home canvas as the active screen and hides the search bar/ book or
	 * borrower menus.
	 */
	public void OnHomeClicked() {
		this.book_canvas.setVisible(false);
		this.borrower_canvas.setVisible(false);
		this.search_bar_box.setVisible(false);
		this.search_bar.clear();
		this.home_canvas.setVisible(true);
	}
	
	/**
	 * Sets the Book menu as the active container and hides the home and borrower 
	 * menus.
	 */
	public void OnBookClicked() {
		this.home_canvas.setVisible(false);
		this.book_canvas.setVisible(true);
		this.search_type.getItems().clear();
		this.search_type.getItems().addAll(book_search_options);
		this.search_bar_box.setVisible(true);
	}
	
	/**
	 * Sets the borrower menu as the active container and hides the home and 
	 * book menus.
	 */
	public void OnBorrowerClicked() {
		this.home_canvas.setVisible(false);
		this.borrower_canvas.setVisible(true);
		this.search_type.getItems().clear();
		this.search_type.getItems().addAll(borrower_search_options);
		this.search_bar_box.setVisible(true);
	}
	
	/**
	 * Opens the calendar dialog box to send updates to the database to simulate
	 * the date changing to track various information such as book due dates
	 * and overdue fees.
	 */
	public void OnCalendarClicked() {
		Dialog<LocalDate> dialog = new CalendarDialog();
		Optional<LocalDate> result = dialog.showAndWait();
		if (result.isPresent()) {
			DatabaseUpdater.setDate(result.get());
			this.date.setText(result.get().toString());
		}
	}
	
	/**
	 * Clears the connection properties and returns to the application
	 * login screen.
	 */
	public void OnLogoutClicked() {
		ConnectionManager.getSingleton().clearProperties();
		SceneManager.getSingleton().loadScene(
			Scenes.LOGIN_SCREEN, SceneManager.STYLE, 1200, 800);
	}
}
