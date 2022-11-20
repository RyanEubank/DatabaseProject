package src.application.client.scenes.controllers;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import src.application.client.scenes.SceneManager;
import src.application.client.scenes.Scenes;
import src.application.client.scenes.dialogs.CalendarDialog;
import src.application.client.scenes.dialogs.CheckoutDialog;
import src.application.client.style.StylizedCell;
import src.application.server.database.*;
import src.application.server.database.exceptions.*;
import src.application.server.database.query.BookSearchHandler;
import src.application.server.database.query.CheckinHandler;
import src.application.server.database.query.CheckoutHandler;
import src.application.server.database.query.DateUpdater;
import src.application.server.database.query.FineSearchHandler;
import src.application.server.database.query.LibraryCardManager;
import src.application.server.database.query.LoanSearchHandler;
import src.application.server.database.records.BookSearchResult;
import src.application.server.database.records.FineSearchResult;
import src.application.server.database.records.FineSummaryResult;
import src.application.server.database.records.FinesAggregator;
import src.application.server.database.records.LoanSearchResult;

public class MainScreen extends AbstractWindow {
	
	private String[] book_search_options = { "Any", "ISBN", "Title", "Author" };
	private String[] loan_search_options = {"Any", "Card No.", "Name", "ISBN"};
	private String[] fine_search_options = {"Any", "Card No.", "Name"};
	
	// database search threads
	private Thread bookSearchThread;
	private Thread loanSearchThread;
	private Thread fineSearchThread;
	
	// table placeholders
	private Text book_search_progress;
	private Text loan_search_progress;
	private Text fine_search_progress;
	private Text fine_summary_progress;
	
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
	@FXML
	private Label error_label;
	
	// GUI components for the book search canvas opened by clicking the books
	// button on the home canvas
	@FXML
	private BorderPane book_canvas;
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
	@FXML
	private TableView<FineSearchResult> fine_result_table;
	@FXML
	private TableColumn<FineSearchResult, Integer> fineBorrowerIDCol;
	@FXML
	private TableColumn<FineSearchResult, String> fineNameCol;
	@FXML
	private TableColumn<FineSearchResult, Double> fineAmountCol;
	@FXML
	private TableColumn<FineSearchResult, Boolean> finePaidCol;
	@FXML
	private TableView<FineSummaryResult> fine_summary_table;
	@FXML
	private TableColumn<FineSummaryResult, Integer> summaryLoanIDCol;
	@FXML
	private TableColumn<FineSummaryResult, Integer> summaryBorrowerIDCol;
	@FXML
	private TableColumn<FineSummaryResult, Double> totalOwedCol;
	@FXML
	private TableColumn<FineSummaryResult, Double> totalPaidCol;
	
	/**
	 * Sets up the initial layout and displayed fields and binds the 
	 * visibility of the book search and loan search containers to 
	 * automaticly resize when toggled.
	 */
	@Override
	public void initialize() {
		super.initialize();
		bindVisibilty();
		setPlaceholderCanvases();
		initBookTableColumns();
		initLoanTableColumns();
		initFineSearchColumns();
		initFineSummaryColumns();
		
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
		this.fine_search_progress = new Text("Search for a fine.");
		this.fine_summary_progress = new Text("Search for a fine.");
		this.book_result_table.setPlaceholder(this.book_search_progress);
		this.loan_result_table.setPlaceholder(this.loan_search_progress);
		this.fine_result_table.setPlaceholder(this.fine_search_progress);
		this.fine_summary_table.setPlaceholder(this.fine_summary_progress);
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
	 * Sets the binding between columns and the respective properties
	 * defined in the FineSearchResult objects used to populate the fines
	 * table.
	 */
	private void initFineSearchColumns() {
		this.fineBorrowerIDCol.setCellValueFactory(new PropertyValueFactory<>("borrowerID"));
		this.fineNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		this.fineAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
		this.finePaidCol.setCellValueFactory(new PropertyValueFactory<>("isPaid"));
	}
	
	/**
	 * Sets the binding between columns and the respective properties
	 * defined in the FineSummaryResult objects used to populate the fines
	 * summary table.
	 */
	private void initFineSummaryColumns() {
		this.summaryLoanIDCol.setCellValueFactory(new PropertyValueFactory<>("borrowerID"));
		this.summaryBorrowerIDCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		this.totalOwedCol.setCellValueFactory(new PropertyValueFactory<>("totalOwed"));
		this.totalPaidCol.setCellValueFactory(new PropertyValueFactory<>("totalPaid"));
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
		this.error_label.setText("");
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
		this.search_type.getItems().clear();
		this.search_type.getItems().addAll(fine_search_options);
		this.search_type.setValue(fine_search_options[0]);
		this.action_button.setText("Pay Fine");
		this.action_button.setVisible(true);
		this.search_bar_container.setVisible(true);
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
		try {
			Dialog<LocalDate> dialog = SceneManager.getSingleton().openDialog(Scenes.CALENDAR_DIALOG);
			Optional<LocalDate> result = dialog.showAndWait();
			if (result.isPresent()) {
				DateUpdater.setDate(result.get());
				this.date.setText(result.get().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Clears the connection properties and returns to the application
	 * login screen.
	 */
	public void onLogoutClicked() {
		ConnectionManager.getSingleton().clearProperties();
		SceneManager.getSingleton().setMainStage(
			Scenes.LOGIN_SCREEN, Scenes.DEFAULT_STYLESHEET, 1200, 800);
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
		SelectionModel<FineSearchResult> selectionHandler 
		= this.fine_result_table.getSelectionModel();
		FineSearchResult selection = selectionHandler.getSelectedItem();
		
		if (selection == null)
			this.error_label.setText("No fine is selected.");
		else
			attemptPayFine(selection);
	}

	/**
	 * Updates the paid attribute of a fine to true indicating it
	 * has been paid off, and refreshes the fines table if successful.
	 * 
	 * @param loanID - the cardID
	 */
	private void attemptPayFine(FineSearchResult selection) {
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
			this.error_label.setText("No loan is selected.");
		else 
			attemptCheckinBook(selection);
	}

	/**
	 * Gets the current date and attempts to check the selected book in
	 * with the current date as its check in date.
	 * 
	 * @param loan - the selected book loan to check in.
	 */
	private void attemptCheckinBook(LoanSearchResult loan) {
		if (loan.getCheckinDate() == null) {
			LocalDate dayIn = LocalDate.now();
			if (CheckinHandler.checkinBook(loan.getLoanID(), dayIn)) {
				loan.setCheckinDate(dayIn);
				
				this.book_result_table.getItems().stream().filter(
					(book) -> book.getIsbn().equals(loan.getIsbn())
				).findAny().ifPresent(
					(book) -> { book.setIsAvailable(true); }
				);
				
				this.loan_result_table.refresh();
				this.book_result_table.refresh();
			} else
				this.error_label.setText(
					"Loan ID: (" + loan.getBorrowerID() + ") not found!");
		}
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
		this.error_label.setText("");
		
		if (selection == null)
			this.error_label.setText("No book is selected.");
		else if (!selection.getIsAvailable())
			this.error_label.setText("Book is unavailable!");
		else 
			attemptCheckoutBook(selection);
	}
	
	/**
	 * Opens a dialog for the user to enter a borrower card number
	 * and attempts to insert a new loan in the library database for
	 * the selected book and borrower.
	 * 
	 * @param book - the table row selected for checkout.
	 */
	private void attemptCheckoutBook(BookSearchResult book) {
		try {
			Dialog<Integer> dialog = SceneManager.getSingleton().openDialog(Scenes.CHECKOUT_DIALOG);
			Optional<Integer> result = dialog.showAndWait();
			if (result.isPresent()) {
				try {
					CheckoutHandler.checkoutBook(book.getIsbn(), result.get());
					book.setIsAvailable(false);
					this.book_result_table.refresh();
				} catch (UnknownBorrowerException e) {
					this.error_label.setText(
						"Borrower ID: (" + result.get() + ") not found!");
				} catch (MaximumLoanException e) {
					this.error_label.setText(
						"Borrower ID: (" + result.get() + ") has exceeded maximum allowed checkouts");
				} catch (SQLException e) {
					this.error_label.setText("An unknown error has occured.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		else if (this.fines_canvas.isVisible())
			onSearchFines(key, filter);
		else
			throw new RuntimeException(
				"User searched null field. No table selected.");
	}

	/**
	 * Performs a query on the database to return book loans matching
	 * the specified filter on the given search key.
	 * 
	 * @param key - the search term entered by the user.
	 * @param filter - the criteria category filter to match search results on.
	 */
	private void onSearchBooks(String key, String filter) {
		if (this.bookSearchThread == null || !this.bookSearchThread.isAlive()) {
			this.book_search_progress.setText("Searching books... please wait");
			this.book_result_table.getItems().clear();
			this.error_label.setText("");
			
			Runnable bookSearchTask = () -> {
				List<BookSearchResult> results = new BookSearchHandler().onLookup(key, filter);
				if (results.isEmpty())
					this.book_search_progress.setText("No books found");
				this.book_result_table.getItems().addAll(results);
			};
			this.bookSearchThread = new Thread(bookSearchTask);
			this.bookSearchThread.start();
		} else System.out.println("Book search already running...");
	}
	
	/**
	 * Performs a query on the database to return books and their availabilities
	 * matching the specified filter on the given search key.
	 * 
	 * @param key - the search term entered by the user.
	 * @param filter - the criteria category filter to match search results on.
	 */
	private void onSearchLoans(String key, String filter) {
		if (this.loanSearchThread == null || !this.loanSearchThread.isAlive()) {
			this.loan_search_progress.setText("Searching loans... please wait");
			this.loan_result_table.getItems().clear();
			this.error_label.setText("");
			
			Runnable loanSearchTask = () -> {
				List<LoanSearchResult> results = new LoanSearchHandler().onLookup(key, filter);
				if (results.isEmpty())
					this.loan_search_progress.setText("No loans found");
				this.loan_result_table.getItems().addAll(results);
			};
			this.loanSearchThread = new Thread(loanSearchTask);
			this.loanSearchThread.start();
		} else System.out.println("Loan search already running");
	}
	
	/**
	 * Performs a query on the database to return borrower fines and their 
	 * paid status matching the specified filter on the given search key.
	 * 
	 * @param key - the search term entered by the user.
	 * @param filter - the criteria category filter to match search results on.
	 */
	private void onSearchFines(String key, String filter) {
		if (this.fineSearchThread == null || !this.fineSearchThread.isAlive()) {
			String placeholder = "Searching fines... please wait";
			this.fine_search_progress.setText(placeholder);
			this.fine_summary_progress.setText(placeholder);
			this.fine_result_table.getItems().clear();
			this.error_label.setText("");
			
			Runnable fineSearchTask = () -> {
				List<FineSearchResult> results = new FineSearchHandler().onLookup(key, filter);
				if (results.isEmpty()) {
					String error = "No loans/fines found";
					this.fine_search_progress.setText(error);
					this.fine_summary_progress.setText(error);
				}
				this.fine_result_table.getItems().addAll(results);
				List<FineSummaryResult> summary = FinesAggregator.summarize(results);
				this.fine_summary_table.getItems().addAll(summary);
			};
			
			this.fineSearchThread = new Thread(fineSearchTask);
			this.fineSearchThread.start();
		} else System.out.println("Fine search already running");
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
