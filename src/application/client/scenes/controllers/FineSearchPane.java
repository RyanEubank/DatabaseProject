package src.application.client.scenes.controllers;

import java.util.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import src.application.server.database.query.*;
import src.application.server.database.records.*;

public class FineSearchPane extends AbstractSearchPane<FineSearchResult> {

	private static final String[] FINE_SEARCH_FILTERS = {"Any", "Card No.", "Name"};
	
	private List<FineSearchResult> m_results = new ArrayList<>();
	private List<FineSearchResult> m_paid = new ArrayList<>();
	private List<FineSummaryResult> m_summary = new ArrayList<>();
	
	private Text m_summaryProgress;
	
	// Primary search table
	@FXML
	private TableColumn<FineSearchResult, Integer> fineLoanIDCol;
	@FXML
	private TableColumn<FineSearchResult, Integer> fineBorrowerIDCol;
	@FXML
	private TableColumn<FineSearchResult, String> fineNameCol;
	@FXML
	private TableColumn<FineSearchResult, Double> fineAmountCol;
	@FXML
	private TableColumn<FineSearchResult, Boolean> finePaidCol;
	@FXML
	private CheckBox hide_fines_checkbox;
	
	// Secondary table for summary information
	@FXML
	private TableView<FineSummaryResult> fine_summary_table;
	@FXML
	private TableColumn<FineSummaryResult, Integer> summaryBorrowerIDCol;
	@FXML
	private TableColumn<FineSummaryResult, String> summaryNameIDCol;
	@FXML
	private TableColumn<FineSummaryResult, Double> totalOwedCol;
	@FXML
	private TableColumn<FineSummaryResult, Double> totalPaidCol;
	
	/**
	 * Sets up the handler for the check box hiding/showing paid fines.
	 */
	@Override
	public void initialize() {
		super.initialize();
		this.hide_fines_checkbox.setOnAction(e -> onHidePaidFines());
	}
	
	/**
	 * Returns the list of options that fines can be filtered on.
	 */
	@Override
	protected String[] getFilterOptions() {
		return FineSearchPane.FINE_SEARCH_FILTERS;
	}
	
	@Override
	protected void clearSearchUI() {
		super.clearSearchUI();
		this.m_summaryProgress.setText("Searching... please wait");
	}
	
	/**
	 * Sets the binding between columns and the respective properties
	 * defined in the FineSearchResult objects used to populate the fines
	 * table and summary table.
	 */
	@Override
	protected void initColumns() {
		this.fineLoanIDCol.setCellValueFactory(new PropertyValueFactory<>("loanID"));
		this.fineBorrowerIDCol.setCellValueFactory(new PropertyValueFactory<>("borrowerID"));
		this.fineNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		this.fineAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
		this.finePaidCol.setCellValueFactory(new PropertyValueFactory<>("isPaid"));
		this.summaryBorrowerIDCol.setCellValueFactory(new PropertyValueFactory<>("borrowerID"));
		this.summaryNameIDCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		this.totalOwedCol.setCellValueFactory(new PropertyValueFactory<>("totalOwed"));
		this.totalPaidCol.setCellValueFactory(new PropertyValueFactory<>("totalPaid"));
	}
	
	/**
	 * Sest up the placeholder node displayed in the fine record table 
	 *  and summary table when they are empty.
	 */
	@Override
	protected void initTablePlaceholder() {
		this.m_searchProgress = new Text("Search for fines.");
		this.m_table.setPlaceholder(this.m_searchProgress);
		this.m_summaryProgress = new Text("Search for fines.");
		this.fine_summary_table.setPlaceholder(this.m_summaryProgress);
	}

	/**
	 * Returns the name of the action performed by the fines
	 * search pane: 'Pay Fine' for marking the selected fine as paid.
	 */
	@Override
	protected String getActionName() {
		return "Pay Fine";
	}
	
	/**
	 * Defines the runnable task to be executed when the user searches 
	 * for fines that performs a query on the database to return records 
	 * matching the specified filter on the given search key.
	 * 
	 * @param key - the search term entered by the user.
	 * @param filter - the criteria category filter to match search 
	 * 	results on.
	 */
	@Override
	protected void searchTask(String key, String filter) {
		List<FineSearchResult> results = new FineSearchHandler()
			.onLookup(key, filter);
		cacheResults(results);
		
		if (this.m_results.isEmpty()) 
			emptySearch();
		else {
			clearTables();
			populateTables();
		}
	}

	/**
	 * Caches the given results into the full lookup list, the paid sublist, 
	 * and summary.
	 * 
	 * @param results - the list of fines to cache.
	 */
	private void cacheResults(List<FineSearchResult> results) {
		clearCache();
		this.m_results.addAll(results);
		this.m_paid.addAll(this.m_results.stream()
			.filter(fine -> !fine.getIsPaid()).toList());
		this.m_summary = FinesAggregator.summarize(this.m_results);
	}
	
	/**
	 * Clears all cached fines from the results list, paid sublist,
	 * and summary list.
	 */
	private void clearCache() {
		this.m_results.clear();
		this.m_paid.clear();
		this.m_summary.clear();
	}

	/**
	 * Sets the error text displayed in the tables  if a search 
	 * yeilds no result.
	 */
	private void emptySearch() {
		String error = "No loans/fines found";
		this.m_searchProgress.setText(error);
		this.m_summaryProgress.setText(error);
	}

	/**
	 * Fills the fine and summary table with search results.
	 */
	private void populateTables() {
		if (this.hide_fines_checkbox.isSelected())
			this.m_table.getItems().addAll(this.m_paid);
		else
			this.m_table.getItems().addAll(this.m_results);
		this.fine_summary_table.getItems().addAll(this.m_summary);
	}

	/**
	 * Clears all entries from the fines table and summary table.
	 */
	private void clearTables() {
		this.m_table.getItems().clear();
		this.fine_summary_table.getItems().clear();
	}
	
	/**
	 * Updates the given fine record in the database by attempting to
	 * set its paid attribute to true.
	 * 
	 * @throws Exception 
	 *  Throws an exception if there is a problem updating the database
	 *  or if the action violates a trigger such as paying a fine
	 *  that was already paid off.
	 */
	@Override
	protected boolean updateDatabase(FineSearchResult fine) 
		throws Exception 
	{
		new FinePaymentHandler().onPayFine(fine.getLoanID());
		return true;
	}
	
	/**
	 * Sets the paid status of the given fine record to true
	 * and refreshes the fines tables.
	 * 
	 * @param book - the book record that needs to be updated.
	 */
	protected void updateTable(FineSearchResult fine) {
		fine.setIsPaid(true);
		
		// update cached results
		this.m_results.stream().filter((f) -> f.equals(fine))
			.findAny().ifPresent((f) -> f.setIsPaid(true));
		List<FineSearchResult> copy = List.copyOf(this.m_results);
		cacheResults(copy);
		
		// update the tables
		clearTables();
		populateTables();
		
		this.m_table.refresh();
		this.fine_summary_table.refresh();
	}
	
	/**
	 * Hides/shows the already paid fines in the search result table.
	 */
	private void onHidePaidFines() {
		if (this.hide_fines_checkbox.isSelected()) {
			this.m_table.getItems().clear();
			this.m_table.getItems().addAll(this.m_paid);
		} else {
			this.m_table.getItems().clear();
			this.m_table.getItems().addAll(this.m_results);
		}
		
		if (this.m_table.getItems().isEmpty())
			this.m_searchProgress.setText("No fines to display.");
	}
}
