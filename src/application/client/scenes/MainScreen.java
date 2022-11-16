package src.application.client.scenes;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import src.application.client.style.StylizedCell;
import src.application.server.database.*;
import src.application.server.database.exceptions.*;
import src.application.server.network.ConnectionManager;

public class MainScreen extends AbstractScreen {
	
	public static final String SCREEN_LAYOUT = "src/resource/stylesheets/MainScreen.fxml";
	
	private String[] book_search_options = { "Any", "ISBN", "Title", "Author" };
	private String[] loan_search_options = {"Any", "Card No.", "Name", "ISBN"};
	
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
	@FXML
	private Text date;
	@FXML
	private Button action_button;

	// GUI components for the book search canvas opened by clicking the books
	// button on the home canvas
	@FXML
	private BorderPane book_canvas;
	@FXML
	private Label loan_error;
	@FXML
	private TableView<BookSearchResult> book_result_table;
	@FXML
	private TableColumn<BookSearchResult, String> bookIsbnCol;
	@FXML
	private TableColumn<BookSearchResult, String> titleCol;
	@FXML
	private TableColumn<BookSearchResult, String> authorCol;
	@FXML
	private TableColumn<BookSearchResult, Boolean> isAvailableCol;
	
	// GUI components for the borrower management canvas opened by clicking
	// the borrower button on the home canvas
	@FXML
	private BorderPane borrower_canvas;
	@FXML
	private TextField user_ssn_field;
	@FXML
	private TextField user_fname_field;
	@FXML
	private TextField user_lname_field;
	@FXML
	private TextField user_address_field;
	@FXML
	private TextField user_phone_field;
	
	// GUI components for the loan search canvas opened by clicking the loan
	// button on the home canvas
	@FXML
	private BorderPane loan_canvas;
	@FXML
	private TableView<LoanSearchResult> loan_result_table;
	@FXML
	private TableColumn<LoanSearchResult, Integer> loanIDCol;
	@FXML
	private TableColumn<LoanSearchResult, Integer> cardIDCol;
	@FXML
	private TableColumn<LoanSearchResult, String> borrowerCol;
	@FXML
	private TableColumn<LoanSearchResult, String> loanIsbnCol;
	@FXML
	private TableColumn<LoanSearchResult, LocalDate> checkoutCol;
	@FXML
	private TableColumn<LoanSearchResult, LocalDate> dueDateCol;
	@FXML
	private TableColumn<LoanSearchResult, LocalDate> checkinCol;
	
	
	// GUI components for the fines display canvas opened by clicking the fines
	// button on the home canvas
	@FXML
	private BorderPane fines_canvas;
	
	// table placeholders
	private Text book_search_progress;
	private Text loan_search_progress;
	
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
		initBookTableColumns();
		initLoanTableColumns();
		
		// initialize the cell factory to style rows based on the book availabilty
		// value set in the isAvailable column
		this.isAvailableCol.setCellFactory(
			tableCell -> new StylizedCell<BookSearchResult, Boolean>
				((availability) -> availability, "AVAILABLE", "UNAVAILABLE")
		);
		
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
		this.loan_canvas.managedProperty().bind(this.loan_canvas.visibleProperty());
		this.borrower_canvas.managedProperty()
			.bind(this.borrower_canvas.visibleProperty());
	}
	
	/**
	 * Sets a default node to display when search tables are empty.
	 */
	private void setPlaceholderCanvases() {
		this.book_search_progress = new Text("Search for a book.");
		this.loan_search_progress = new Text("Search for a loan.");
		this.book_result_table.setPlaceholder(book_search_progress);
		this.loan_result_table.setPlaceholder(loan_search_progress);
	}
	
	/**
	 * Sets the binding between columns and the respective properties
	 * defined in the BookSearchResult objects used to populate the book table.
	 */
	private void initBookTableColumns() {
		this.bookIsbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
		this.titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
		this.authorCol.setCellValueFactory(new PropertyValueFactory<>("authors"));
		this.isAvailableCol.setCellValueFactory(new PropertyValueFactory<>("isAvailable"));
	}
	
	/**
	 * Sets the binding between columns and the respective properties
	 * defined in the LoanSearchResult objects used to populate the loan
	 * table.
	 */
	private void initLoanTableColumns() {
		this.loanIDCol.setCellValueFactory(new PropertyValueFactory<>("loanID"));
		this.cardIDCol.setCellValueFactory(new PropertyValueFactory<>("borrowerID"));
		this.borrowerCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		this.loanIsbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
		this.checkoutCol.setCellValueFactory(new PropertyValueFactory<>("checkoutDate"));
		this.dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
		this.checkinCol.setCellValueFactory(new PropertyValueFactory<>("checkinDate"));
	}
	
	/**
	 * Sets the home canvas as the active screen and hides the 
	 * search bar/other menus.
	 */
	public void onHomeClicked() {
		this.book_canvas.setVisible(false);
		this.borrower_canvas.setVisible(false);
		this.loan_canvas.setVisible(false);
		this.fines_canvas.setVisible(false);
		this.search_bar_container.setVisible(false);
		this.search_bar.clear();
		this.action_button.setVisible(false);
		this.loan_error.setText("");
		this.home_canvas.setVisible(true);
	}
	
	/**
	 * Sets the Book menu as the active container and hides the other canvases.
	 */
	public void onBookClicked() {
		this.home_canvas.setVisible(false);
		this.book_canvas.setVisible(true);
		this.search_type.getItems().clear();
		this.search_type.getItems().addAll(book_search_options);
		this.search_type.setValue(book_search_options[0]);
		this.action_button.setText("Checkout");
		this.action_button.setVisible(true);
		this.search_bar_container.setVisible(true);
	}
	
	/**
	 * Sets the loan menu as the active container and hides the other canvases.
	 */
	public void onLoanClicked() {
		this.home_canvas.setVisible(false);
		this.loan_canvas.setVisible(true);
		this.search_type.getItems().clear();
		this.search_type.getItems().addAll(loan_search_options);
		this.search_type.setValue(loan_search_options[0]);
		this.action_button.setText("Check In");
		this.action_button.setVisible(true);
		this.search_bar_container.setVisible(true);
	}
	
	/**
	 * Sets the fines menu as the active container and hides the other
	 * canvases.
	 */
	public void onFinesClicked() {
		this.home_canvas.setVisible(false);
		this.fines_canvas.setVisible(true);
		this.action_button.setText("Pay Fine");
		this.action_button.setVisible(true);
	}
	
	/**
	 * Opens the borrower management canvas from the home screen
	 * when the borrower button is clicked.
	 */
	public void onBorrowerClicked() {
		this.home_canvas.setVisible(false);
		this.borrower_canvas.setVisible(true);
	}
	
	/**
	 * Opens the calendar dialog box to send updates to the database to simulate
	 * the date changing to track various information such as book due dates
	 * and overdue fees.
	 */
	public void onCalendarClicked() {
		Dialog<LocalDate> dialog = new CalendarDialog(
			"/src/resource/stylesheets/Calendar.fxml");
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
	 * Called when the user selects a search result in one of the menus
	 * and clicks the action button to either checkout/checkin a book or
	 * mark a fine as paid.
	 */
	public void onActionButtonClicked() {
		if (this.book_canvas.isVisible()) 
			onCheckout();
		else if (this.loan_canvas.isVisible()) 
			onCheckIn();
		else if (this.fines_canvas.isVisible())
			onPayFine();
		else
			throw new RuntimeException(
				"Null field. No table selected.");
	}
	
	/**
	 * Gets the slected fine and attempts to update and mark it as paid
	 * in the database.
	 */
	private void onPayFine() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Reads the currently selected row in the loans tables and
	 * calls the check in handler. Sets an error messsage if nothing
	 * is selected.
	 */
	private void onCheckIn() {
		SelectionModel<LoanSearchResult> selectionHandler 
			= this.loan_result_table.getSelectionModel();
		LoanSearchResult selection = selectionHandler.getSelectedItem();
	
		if (selection == null)
			this.loan_error.setText("No loan is selected.");
		else 
			checkinBook(selection);
	}

	/**
	 * Gets the current date and attempts to check the selected book in
	 * with the current date as its check in date.
	 * 
	 * @param loan - the selected book loan to check in.
	 */
	private void checkinBook(LoanSearchResult loan) {
		LocalDate dayIn = LocalDate.now();
		if (CheckinHandler.checkinBook(loan.getLoanID(), dayIn)) {
			loan.setCheckinDate(dayIn);
			
			this.book_result_table.getItems().stream().filter(
				(book) -> book.getIsbn().equals(loan.getIsbn())
			).findAny().ifPresent((book) -> {
				book.setIsAvailable(true);
				System.out.println(book.getIsbn());
			});
			
			this.loan_result_table.refresh();
			this.book_result_table.refresh();
		} else
			this.loan_error.setText(
				"Loan ID: (" + loan.getBorrowerID() + ") not found!");
	}
	
	/**
	 * Reads the currently selected row in the books table and calls
	 * the checkout handler if the book is available. Sets an error message
	 * if nothing is selected or book is not available.
	 */
	private void onCheckout() {
		SelectionModel<BookSearchResult> selectionHandler 
			= this.book_result_table.getSelectionModel();
		BookSearchResult selection = selectionHandler.getSelectedItem();
		this.loan_error.setText("");
		
		if (selection == null)
			this.loan_error.setText("No book is selected.");
		else if (!selection.getIsAvailable())
			this.loan_error.setText("Book is unavailable!");
		else 
			checkoutBook(selection);
	}
	
	/**
	 * Opens a dialog for the user to enter a borrower card number
	 * and attempts to insert a new loan in the library database for
	 * the selected book and borrower.
	 * 
	 * @param book - the table row selected for checkout.
	 */
	private void checkoutBook(BookSearchResult book) {
		Dialog<Integer> dialog = new CheckoutDialog(
			"/src/resource/stylesheets/Checkout.fxml");
		Optional<Integer> result = dialog.showAndWait();
		
		if (result.isPresent()) {
			try {
				CheckoutHandler.checkoutBook(book.getIsbn(), result.get());
				book.setIsAvailable(false);
				this.book_result_table.refresh();
			} catch (UnknownBorrowerException e) {
				this.loan_error.setText(
					"Borrower ID: (" + result.get() + ") not found!");
			} catch (MaximumLoanException e) {
				this.loan_error.setText(
					"Borrower ID: (" + result.get() + ") has exceeded maximum allowed checkouts");
			} catch (SQLException e) {
				this.loan_error.setText("An unknown error has occured.");
			}
		}
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
		else if (this.loan_canvas.isVisible()) 
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
		this.loan_search_progress.setText("Searching loans... please wait");
		this.loan_result_table.getItems().clear();
		
		Runnable loanSearchTask = () -> {
			List<LoanSearchResult> results = LoanSearchHandler.lookup(key, filter);
			if (results.isEmpty())
				this.loan_search_progress.setText("No loans found");
			this.loan_result_table.getItems().addAll(results);
		};
		new Thread(loanSearchTask).start();
	}

	/**
	 * Performs a query on the database to return book loans matching
	 * the specified filter on the given search key.
	 * 
	 * @param key - the search term entered by the user.
	 * @param filter - the criteria category filter to match search results on.
	 */
	private void onSearchBooks(String key, String filter) {
		this.book_search_progress.setText("Searching books... please wait");
		this.book_result_table.getItems().clear();
		this.loan_error.setText("");
		
		Runnable bookSearchTask = () -> {
			List<BookSearchResult> results = BookSearchHandler.lookup(key, filter);
			if (results.isEmpty())
				this.book_search_progress.setText("No books found");
			this.book_result_table.getItems().addAll(results);
		};
		new Thread(bookSearchTask).start();
	}
	
	public void onCreateUser() {
		String ssn = this.user_ssn_field.getText();
		String firstname = this.user_fname_field.getText();
		String lastname = this.user_lname_field.getText();
		String address = this.user_address_field.getText();
		String phone = this.user_phone_field.getText();
		
		LibraryCardManager.createUser(ssn, firstname, lastname, address, phone);
	}
}
