package src.application.client.scenes.controllers;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import src.application.server.database.query.FineSearchHandler;
import src.application.server.database.records.FineSearchResult;
import src.application.server.database.records.FineSummaryResult;
import src.application.server.database.records.FinesAggregator;

public class FineSearchPane extends AbstractSearchPane<FineSearchResult> {

	private static final String[] FINE_SEARCH_FILTERS = {"Any", "Card No.", "Name"};
	
	protected Text m_summaryProgress;
	
	@FXML
	private TableColumn<FineSearchResult, Integer> fineBorrowerIDCol;
	@FXML
	private TableColumn<FineSearchResult, String> fineNameCol;
	@FXML
	private TableColumn<FineSearchResult, Double> fineAmountCol;
	@FXML
	private TableColumn<FineSearchResult, Boolean> finePaidCol;
	
	// Secondary table for summary information
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
		this.fineBorrowerIDCol.setCellValueFactory(new PropertyValueFactory<>("borrowerID"));
		this.fineNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		this.fineAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
		this.finePaidCol.setCellValueFactory(new PropertyValueFactory<>("isPaid"));
		this.summaryLoanIDCol.setCellValueFactory(new PropertyValueFactory<>("borrowerID"));
		this.summaryBorrowerIDCol.setCellValueFactory(new PropertyValueFactory<>("name"));
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
	 * Defines the runnable task to be executed when the user searches for fines 
	 * that performs a query on the database to return records matching
	 * the specified filter on the given search key.
	 * 
	 * @param key - the search term entered by the user.
	 * @param filter - the criteria category filter to match search results on.
	 */
	@Override
	protected void searchTask(String key, String filter) {
		List<FineSearchResult> results = new FineSearchHandler().onLookup(key, filter);
		if (results.isEmpty()) {
			String error = "No loans/fines found";
			this.m_searchProgress.setText(error);
			this.m_summaryProgress.setText(error);
		}
		this.m_table.getItems().addAll(results);
		List<FineSummaryResult> summary = FinesAggregator.summarize(results);
		this.fine_summary_table.getItems().addAll(summary);
	}
	

	@Override
	protected void onPerformAction() {
		// TODO Auto-generated method stub
	}
	

	/**
	 * Returns the name of the action performed by the fines
	 * search pane: 'Pay Fine' for marking the selected fine as paid.
	 */
	@Override
	protected String getActionName() {
		return "Pay Fine";
	}
}
