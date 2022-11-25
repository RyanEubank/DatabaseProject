package src.application.client.scenes.controllers;

import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import src.application.server.database.exceptions.LibraryRuleException;

public abstract class AbstractSearchPane<T>  extends AbstractPane {
	private static final String NULL_SELECTION = "No item is selected.";
	
	protected Text m_searchProgress;
	protected Runnable m_searchTask;
	
	@FXML
	protected TableView<T> m_table;
	
	private Thread m_searchThread;
	
	/**
	 * Initializes table colummns and sets up a runnable
	 * task to be called when the user searches for records.
	 */
	@Override
	public void postInitialize() {
		super.postInitialize();
		initColumns();
		initTablePlaceholder();
		initSearchTask();
	}
	
	/**
	 * Sets the runnable task that is called when the user
	 * performs a search.
	 */
	private void initSearchTask() {
		String key = this.m_parent.getSearchText();
		String filter = this.m_parent.getSearchFilter();
		this.m_searchTask = () -> searchTask(key, filter);
	}
	
	/**
	 * Adds a listener to update the search filter options and
	 * change the name of the action button in the parent screen.
	 */
	@Override
	protected void addVisibilityListener() {
		super.addVisibilityListener();
		this.getPane().visibleProperty().addListener(
			(obs, oldVal, newVal) -> {
				if (newVal) {
					String[] options = getFilterOptions();
					this.m_parent.setFilterOptions(options);
				}
			}
		);
	}
	
	/**
	 * Performs a database search to return records such as 
	 * books or loans.
	 */
	protected void onSearch() {
		if (this.m_searchThread == null || !this.m_searchThread.isAlive()) {
			clearSearchUI();
			if (m_searchTask != null) 
				startSearch();
			else throw new RuntimeException("Search task is null.");
		} else 
			System.out.println("Search already running...");
	}

	/**
	 * Sets up a new thread to perform a new search: Threads are
	 * not reusable, once finished a thread must be replaced.
	 */
	private void startSearch() {
		this.m_searchThread = new Thread(m_searchTask);
		this.m_searchThread.start();
	}

	/**
	 * Clears the previous error text and the search table in
	 * preparation for a new search.
	 */
	protected void clearSearchUI() {
		this.m_searchProgress.setText("Searching... please wait");
		this.m_table.getItems().clear();
		this.m_parent.setActionError("");
	}

	/**
	 * Returns the selected record in the search table.
	 * 
	 * @return
	 *  Returns the record object from the search table that the user
	 *  selected most recently.
	 */
	protected T getSelection() {
		SelectionModel<T> selectionHandler 
			= this.m_table.getSelectionModel();
		return selectionHandler.getSelectedItem();
	}

	/**
	 * Gets the selected search item and calls the action handler
	 * if the selection is valid.
	 */
	@Override
	protected void onPerformAction() {
		this.m_parent.setActionError("");
		T selection = getSelection();
		
		if (selection == null)
			this.m_parent.setActionError(NULL_SELECTION);
		else
			callActionHandler(selection);
	}
	
	/**
	 * Handles the pane's main action and refreshes the search table
	 * if the update is successful.
	 * 
	 * @param selection - the selected item to perform the update on.
	 */
	protected void callActionHandler(T selection) {
		try {
			if (updateDatabase(selection))
				updateTable(selection);
		} catch (Exception e) {
			displayError(e);
		}
	}

	/**
	 * Sets up the columns of the table to display the various
	 * fields returned in the query records.
	 */
	protected abstract void initColumns();
	
	/**
	 * Sets up the placeholder node displayed when the search table is 
	 * empty or has no records to display.
	 */
	protected abstract void initTablePlaceholder();
	
	/**
	 * Defines the runnable that is used to perform the database search
	 * and return result to the table.
	 * 
	 * @param key - the search term entered by the user.
	 * @param filter - the criteria category filter to match search results on.
	 */
	protected abstract void searchTask(String key, String filter);
	
	/**
	 * Returns the attribute names that the search can filter on.
	 * 
	 * @return
	 *  Returns a string array containing the different columns
	 *  a query can filter by.
	 */
	protected abstract String[] getFilterOptions();
	
	/**
	 * Performs the same update on the selection in the table 
	 * to match if the database is successsfully updated.
	 * 
	 * @param selection - the selection to update.
	 */
	protected abstract void updateTable(T selection);

	/**
	 * Updates the database with the appropriate insert or update
	 * statement for the screen's action.
	 * 
	 * @param selection - the selection to update in the database.
	 * 
	 * @return 
	 *  Returns true if the database was updated, false if the operation
	 *  was cancelled.
	 *  
	 * @throws Exception
	 * 	Throws an exception if there is an error with the update. 
	 */
	protected abstract boolean updateDatabase(T selection) 
		throws LibraryRuleException, SQLException, Exception;
}
