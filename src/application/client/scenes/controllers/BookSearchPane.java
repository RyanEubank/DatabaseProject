package src.application.client.scenes.controllers;

import java.sql.SQLException;
import java.util.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import src.application.client.scenes.*;
import src.application.client.style.StylizedCell;
import src.application.server.database.*;
import src.application.server.database.exceptions.*;
import src.application.server.database.query.BookSearchHandler;
import src.application.server.database.query.CheckoutHandler;
import src.application.server.database.records.BookSearchResult;

public class BookSearchPane extends AbstractSearchPane<BookSearchResult> {

	private static final String[] BOOK_SEARCH_FILTERS = { 
		"Any", "ISBN", "Title", "Author" 
	};
	
	private static final String PLACEHOLDER_TEXT = "Search for a book.";
	private static final String VOID_RESULT = "No books found";
	private static final String NULL_SELECTION = "No book is selected.";
	private static final String UNAVAILABLE = "Book is unavailable!";
	private static final String UNKNOWN_ERROR = "An unknown error has occured.";
	
	@FXML
	private TableColumn<BookSearchResult, String> isbnCol;
	@FXML
	private TableColumn<BookSearchResult, String> titleCol;
	@FXML
	private TableColumn<BookSearchResult, String> authorCol;
	@FXML
	private TableColumn<BookSearchResult, Boolean> isAvailableCol;
	
	/**
	 * Initializes the book pane search elements and sets up optional
	 * highlighting for books that are not available.
	 */
	@Override
	public void postInitialize() {
		super.postInitialize();
		setTableHighlighting();
	}
	
	/**
	 * Initializes the cell factory to style rows based on the book 
	 * availabilty value set in the isAvailable column.
	 */ 
	private void setTableHighlighting() {
		this.isAvailableCol.setCellFactory(
			tableCell -> new StylizedCell<BookSearchResult, Boolean>
				((availability) -> availability, "AVAILABLE", "UNAVAILABLE")
		);
	}
	
	/**
	 * Sets the binding between columns and the respective properties
	 * defined in the BookSearchResult objects used to populate the book 
	 * table.
	 */
	@Override
	protected void initColumns() {
		this.isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
		this.titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
		this.authorCol.setCellValueFactory(new PropertyValueFactory<>("authors"));
		this.isAvailableCol.setCellValueFactory(new PropertyValueFactory<>("isAvailable"));
	}
	
	/**
	 * Sest up the placeholder node displayed in the book table when
	 * it is empty.
	 */
	@Override
	protected void initTablePlaceholder() {
		this.m_searchProgress = new Text(PLACEHOLDER_TEXT);
		this.m_table.setPlaceholder(this.m_searchProgress);
	}
	
	
	/**
	 * Returns the list of options that books can be filtered on
	 * during search.
	 */
	@Override
	protected String[] getFilterOptions() {
		return BOOK_SEARCH_FILTERS;
	}
	
	/**
	 * Returns the name of the action performed by the books
	 * search pane: 'Checkout' for checking out the selected
	 * book.
	 */
	@Override
	protected String getActionName() {
		return "Checkout Book";
	}
	
	/**
	 * Defines the runnable task to be executed when the user searches for 
	 * books that performs a query on the database to return records matching
	 * the specified filter on the given search key.
	 * 
	 * @param key - the search term entered by the user.
	 * @param filter - the criteria category filter to match search results on.
	 */
	@Override
	protected void searchTask(String key, String filter) {
		List<BookSearchResult> results = new BookSearchHandler()
			.onLookup(key, filter);
		if (results.isEmpty())
			this.m_searchProgress.setText(VOID_RESULT);
		this.m_table.getItems().addAll(results);
	}
	
	/**
	 * Reads the currently selected row in the books table and calls
	 * the checkout handler if the book is available. Sets an error message
	 * if nothing is selected or book is not available.
	 */
	@Override
	protected void onPerformAction() {
		BookSearchResult selection = getSelection();
		this.m_parent.setActionError("");
		
		if (selection == null)
			this.m_parent.setActionError(NULL_SELECTION);
		else if (!selection.getIsAvailable())
			this.m_parent.setActionError(UNAVAILABLE);
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
		Optional<Integer> id = promptForID();
		if (id.isPresent()) 
			handleCheckout(id.get(), book);
	}
	
	/**
	 * Opens a dialog and prompts the user to enter the borrower ID
	 * of the borrower who is checking out a book from the library.
	 * 
	 * @return
	 *  Returns an optional value of the borrower ID if the user
	 *  enters one. Optional will be empty if the user cancels
	 *  the interaction.
	 */
	private Optional<Integer> promptForID() {
		try {
			Dialog<Integer> dialog = SceneManager.getSingleton()
				.openDialog(Scenes.CHECKOUT_DIALOG);
			return dialog.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}
	
	/**
	 * Calss the checkout handler to update the database and set the selected
	 * book as checked out.
	 * 
	 * @param borrowerID - the ID of the borrower checking out the book.
	 * @param book - the book record selected to checkout.
	 */
	private void handleCheckout(int borrowerID, BookSearchResult book) {
		try {
			CheckoutHandler.checkoutBook(book.getIsbn(), borrowerID);
			updateBookTable(book);
		} catch (UnknownBorrowerException | MaximumLoanException e) {
			this.m_parent.setActionError(e.getMessage());
		} catch (SQLException e) {
			this.m_parent.setActionError(UNKNOWN_ERROR);
		}
	}

	/**
	 * Sets the availability of the checkedout book to false
	 * and refreshes the book search table.
	 * 
	 * @param book - the book record that needs to be updated.
	 */
	private void updateBookTable(BookSearchResult book) {
		book.setIsAvailable(false);
		this.m_table.refresh();
	}
}
