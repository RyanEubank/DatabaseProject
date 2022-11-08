package src.application.client.scenes;

import java.time.LocalDate;
import java.util.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import src.application.client.style.StylizedCell;
import src.application.server.database.*;
import src.application.server.network.ConnectionManager;

public class MainScreen extends AbstractScreen {
	
	public static final String SCREEN_LAYOUT = "src/resource/stylesheets/MainScreen.fxml";
	
	private String[] book_search_options = { "Any", "ISBN", "Title", "Author" };
	private String[] borrower_search_options = {"Any", "Card No.", "Name", "ISBN"};
	
	// GUI components for the home button canvas (default canvas when scene is loaded)
	@FXML 
	private BorderPane home_canvas;	
	@FXML
	private HBox search_bar_container;
	@FXML
	private TextField search_bar;
	@FXML
	private ChoiceBox<String> search_type;
	@FXML
	private MenuButton settings_button;
	@FXML
	private MenuItem calendar_option;
	@FXML
	private MenuItem logout_option;
	
	// GUI components for the book search canvas opened by clicking the books
	// button on the home canvas
	@FXML
	private BorderPane book_canvas;
	@FXML
	private TableView<BookSearchResult> book_result_table;
	@FXML
	private TableColumn<BookSearchResult, String> isbnCol;
	@FXML
	private TableColumn<BookSearchResult, String> titleCol;
	@FXML
	private TableColumn<BookSearchResult, String> authorCol;
	@FXML
	private TableColumn<BookSearchResult, Boolean> isAvailableCol;
	
	// GUI components for the loan search canvas opened by clicking the borrower
	// button on the home canvas
	@FXML
	private BorderPane borrower_canvas;
	@FXML
	private TableView<LoanSearchResult> loan_result_table;

	// current selected date displayed on the bottom bar of the window
	@FXML
	private Text date;
	
	/**
	 * Sets up the initial layout and displayed fields and binds the 
	 * visibility of the book search and loan search containers to 
	 * automaticly resize when toggled.
	 */
	@FXML
	public void initialize() {
		super.initialize();
		bindVisibilty();
		setPlaceholderCanvases();
		initTableColumns();
		this.isAvailableCol.setCellFactory(column -> new StylizedCell());
		this.date.setText(LocalDate.now().toString());
		onHomeClicked();
	}

	/**
	 * Binds the visibilty of the book/borrower/home canvases on the main screen
	 * to the managaed property which will automatically resize them to screen
	 * when the active canvas is swapped.
	 */
	private void bindVisibilty() {
		this.home_canvas.managedProperty().bind(this.home_canvas.visibleProperty());
		this.book_canvas.managedProperty().bind(this.book_canvas.visibleProperty());
		this.borrower_canvas.managedProperty().bind(this.borrower_canvas.visibleProperty());
	}
	
	/**
	 * Sets a default node to display when search tables are empty.
	 */
	private void setPlaceholderCanvases() {
		this.book_result_table.setPlaceholder(
			new Text("No books found in search results."));
		this.loan_result_table.setPlaceholder(
			new Text("No loans found in search results."));
	}
	
	/**
	 * Sets the binding between columns and the respective properties
	 * defined in the BookSearchResuly populating the table.
	 */
	private void initTableColumns() {
		this.isbnCol.setCellValueFactory(
			new PropertyValueFactory<BookSearchResult, String>("isbn"));
		this.titleCol.setCellValueFactory(
			new PropertyValueFactory<BookSearchResult, String>("title"));
		this.authorCol.setCellValueFactory(
			new PropertyValueFactory<BookSearchResult, String>("authors"));
		this.isAvailableCol.setCellValueFactory(
			new PropertyValueFactory<BookSearchResult, Boolean>("isAvailable"));
	}	
	
	/**
	 * Sets the home canvas as the active screen and hides the search bar/ book or
	 * borrower menus.
	 */
	public void onHomeClicked() {
		this.book_canvas.setVisible(false);
		this.borrower_canvas.setVisible(false);
		this.search_bar_container.setVisible(false);
		this.search_bar.clear();
		this.home_canvas.setVisible(true);
	}
	
	/**
	 * Sets the Book menu as the active container and hides the home and borrower 
	 * menus.
	 */
	public void onBookClicked() {
		this.home_canvas.setVisible(false);
		this.book_canvas.setVisible(true);
		this.search_type.getItems().clear();
		this.search_type.getItems().addAll(book_search_options);
		this.search_type.setValue(book_search_options[0]);
		this.search_bar_container.setVisible(true);
	}
	
	/**
	 * Sets the borrower menu as the active container and hides the home and 
	 * book menus.
	 */
	public void onBorrowerClicked() {
		this.home_canvas.setVisible(false);
		this.borrower_canvas.setVisible(true);
		this.search_type.getItems().clear();
		this.search_type.getItems().addAll(borrower_search_options);
		this.search_type.setValue(borrower_search_options[0]);
		this.search_bar_container.setVisible(true);
	}
	
	/**
	 * Opens the calendar dialog box to send updates to the database to simulate
	 * the date changing to track various information such as book due dates
	 * and overdue fees.
	 */
	public void onCalendarClicked() {
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
	public void onLogoutClicked() {
		ConnectionManager.getSingleton().clearProperties();
		SceneManager.getSingleton().loadScene(
			Scenes.LOGIN_SCREEN, SceneManager.STYLE, 1200, 800);
	}
	
	/**
	 * Called when the user hits the enter key after typing a search query in
	 * the search bar to populate the search table with results from a database
	 * query.
	 */
	public void onSearch() {
		String key = this.search_bar.getText();
		String filter = this.search_type.getValue();
		
		if (this.book_canvas.isVisible()) 
			onSearchBooks(key, filter);
		else if (this.borrower_canvas.isVisible()) 
			onSearchLoans(key, filter);
		else
			throw new RuntimeException(
				"User searched null field. No table selected.");
	}

	/**
	 * Performs a query on the database to return books and their availabilities
	 * matching the specified filter on the given search key.
	 * 
	 * @param key - the search term entered by the user.
	 * @param filter - the criteria category filter to match search results on.
	 */
	private void onSearchLoans(String key, String filter) {
		List<LoanSearchResult> results = LoanSearchHandler.lookup(key, filter);
		this.loan_result_table.getItems().clear();
		this.loan_result_table.getItems().addAll(results);
	}

	/**
	 * Performs a query on the database to return book loans matching
	 * the specified filter on the given search key.
	 * 
	 * @param key - the search term entered by the user.
	 * @param filter - the criteria category filter to match search results on.
	 */
	private void onSearchBooks(String key, String filter) {
		List<BookSearchResult> results = BookSearchHandler.lookup(key, filter);
		this.book_result_table.getItems().clear();
		this.book_result_table.getItems().addAll(results);
	}
}
