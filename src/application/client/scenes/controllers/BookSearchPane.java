package src.application.client.scenes.controllers;

import java.util.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import src.application.client.scenes.*;
import src.application.client.style.StylizedCell;
import src.application.server.database.query.*;
import src.application.server.database.records.BookSearchResult;

public class BookSearchPane extends AbstractSearchPane<BookSearchResult> {

	private static final String[] BOOK_SEARCH_FILTERS = { 
		"Any", "ISBN", "Title", "Author" 
	};
	
	private static final String PLACEHOLDER_TEXT = "Search for a book.";
	private static final String VOID_RESULT = "No books found";
	
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
	 * @param book - the book record selected to checkout.
	 * 
	 * @throws Exception 
	 *  Throws an exception if there is an error with the search.
	 */
	protected boolean updateDatabase(BookSearchResult book) 
		throws Exception 
	{
		Optional<Integer> id = promptForID();
		if (id.isPresent()) {
			new CheckoutHandler().onCheckout(book.getIsbn(), id.get());
			return true;
		}
		return false;
	}

	/**
	 * Sets the availability of the checkedout book to false
	 * and refreshes the book search table.
	 * 
	 * @param book - the book record that needs to be updated.
	 */
	protected void updateTable(BookSearchResult book) {
		book.setIsAvailable(false);
		this.m_table.refresh();
	}

	/**
	 * Updates the book availability of the selected ISBN
	 * if the table contains a matching record.
	 * 
	 * @param isbn - the isbn of the book to mark available again.
	 */
	public void updateAvailability(String isbn) {
		this.m_table.getItems().stream().filter(
				(book) -> book.getIsbn().equals(isbn)
			).findAny().ifPresent(
				(book) -> { 
					System.out.println(book);
					book.setIsAvailable(true); }
			);
		this.m_table.refresh();
	}
}
