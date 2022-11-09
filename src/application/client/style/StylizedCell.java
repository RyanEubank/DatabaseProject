package src.application.client.style;

import java.util.function.Function;

import javafx.scene.control.*;

public class StylizedCell<T, R> extends TableCell<T, R> {
	
	private static final String ROW_STYLE = "-fx-background-color: #f5e9ec";
	private final Function<R, Boolean> isActive;
	
	public StylizedCell(Function<R, Boolean> activeRow) {
		this.isActive = activeRow;
	}
	
	@Override
	protected void updateItem(R result, boolean empty) {
		super.updateItem(result, empty);
		TableRow<T> row = this.getTableRow();
		
		if (empty || isActive.apply(result)) {
			setStyleAvailable(row);
		} else if (!isActive.apply(result)) {
			setStyleUnavailable(row);
		}
	}

	/**
	 * @param rowStyleCheckedOut
	 * @param row
	 */
	private void setStyleUnavailable(TableRow<T> row) {
		row.setStyle(ROW_STYLE);
		this.setText("UNAVAILABLE");
		row.setDisable(true);
	}

	/**
	 * @param row
	 */
	private void setStyleAvailable(TableRow<T> row) {
		row.setStyle("");
		this.setText("");
		row.setDisable(false);
	}
}
