package src.application.client.style;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import src.application.server.database.BookSearchResult;

public class StylizedCell extends TableCell<BookSearchResult, Boolean> {
	
	private static final String ROW_STYLE = "-fx-background-color: #f5e9ec";
	
	@Override
	protected void updateItem(Boolean result, boolean empty) {
		super.updateItem(result, empty);
		TableRow<BookSearchResult> row = this.getTableRow();
		
		if (empty || result) {
			setStyleAvailable(row);
		} else if (!result) {
			setStyleUnavailable(row);
		}
	}

	/**
	 * @param rowStyleCheckedOut
	 * @param row
	 */
	private void setStyleUnavailable(TableRow<BookSearchResult> row) {
		row.setStyle(ROW_STYLE);
		this.setText("UNAVAILABLE");
		row.setDisable(true);
	}

	/**
	 * @param row
	 */
	private void setStyleAvailable(TableRow<BookSearchResult> row) {
		row.setStyle("");
		this.setText("");
		row.setDisable(false);
	}
}
