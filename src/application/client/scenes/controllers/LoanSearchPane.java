package src.application.client.scenes.controllers;


import java.time.LocalDate;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import src.application.server.database.query.*;
import src.application.server.database.records.*;

public class LoanSearchPane extends AbstractSearchPane<LoanSearchResult> {

	private static final String[] LOAN_SEARCH_FILTERS = {"Any", "Card No.", "Name", "ISBN"};
	
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

	/**
	 * Returns the list of options that loans can be filtered on.
	 */
	@Override
	protected String[] getFilterOptions() {
		return LoanSearchPane.LOAN_SEARCH_FILTERS;
	}
	
	/**
	 * Sets the binding between columns and the respective properties
	 * defined in the LoanSearchResult objects used to populate the loan table.
	 */
	@Override
	protected void initColumns() {
		this.loanIDCol.setCellValueFactory(new PropertyValueFactory<>("loanID"));
		this.cardIDCol.setCellValueFactory(new PropertyValueFactory<>("borrowerID"));
		this.borrowerCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		this.loanIsbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
		this.checkoutCol.setCellValueFactory(new PropertyValueFactory<>("checkoutDate"));
		this.dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
		this.checkinCol.setCellValueFactory(new PropertyValueFactory<>("checkinDate"));
	}

	/**
	 * Sest up the placeholder node displayed in the loan table when
	 * it is empty.
	 */
	@Override
	protected void initTablePlaceholder() {
		this.m_searchProgress = new Text("Search for a loan.");
		this.m_table.setPlaceholder(this.m_searchProgress);
	}

	/**
	 * Returns the name of the action performed by the loans
	 * search pane: 'Check In' for checking int the book identified
	 * by the selected loan.
	 */
	@Override
	protected String getActionName() {
		return "Check In Book";
	}
	
	/**
	 * Defines the runnable task to be executed when the user searches for loans 
	 * that performs a query on the database to return records matching
	 * the specified filter on the given search key.
	 * 
	 * @param key - the search term entered by the user.
	 * @param filter - the criteria category filter to match search results on.
	 */
	@Override
	protected void searchTask(String key, String filter) {
		List<LoanSearchResult> results = new LoanSearchHandler().onLookup(key, filter);
		if (results.isEmpty())
			this.m_searchProgress.setText("No loans found");
		this.m_table.getItems().addAll(results);
	}

	/**
	 * Updates the given loan's checkin date in the database.
	 * 
	 * @throws Exception
	 * 	Throws an exception if there is an error with the update.  
	 */
	@Override
	protected boolean updateDatabase(LoanSearchResult selection) 
		throws Exception 
	{
		new CheckinHandler().onCheckin(selection.getLoanID());
		return true;
	}
	
	/**
	 * Updates the given loan's checkin date to the current
	 * date, resets the books availability in the books table and 
	 * refreshes the tables' entries.
	 */
	@Override
	protected void updateTable(LoanSearchResult selection) {
		selection.setCheckinDate(LocalDate.now());
		BookSearchPane bookPane = (BookSearchPane) this.m_parent.getBooksPane();
		bookPane.updateAvailability(selection.getIsbn());
		this.m_table.refresh();
	}
}
