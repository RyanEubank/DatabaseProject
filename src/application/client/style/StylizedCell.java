package src.application.client.style;

import java.util.function.Predicate;

import javafx.scene.control.*;

public class StylizedCell<T, R> extends TableCell<T, R> {
	
	private static final String ROW_STYLE = "-fx-background-color: #f5e9ec";
	private final Predicate<R> m_activation;
	private final String m_activeText;
	private final String m_inactiveText;
	
	/**
	 * Constructs a StylizedCell with the specified activation
	 * predicate -> the function that determines whether the
	 * cell should be enabled or not.
	 * 
	 * @param activeRow - the predicate which when evaluated will
	 * determine the style and if the cell's row is active or disabled.
	 * @param text - the text to display if the row is disabled.
	 */
	public StylizedCell(Predicate<R> activateRow, String activeText, String inactiveText) {
		this.m_activation = activateRow;
		this.m_activeText = activeText;
		this.m_inactiveText = inactiveText;
	}
	
	/**
	 * Updates the cell text and row style properties based
	 * on the activation property returned by the StylizedCell's
	 * activation function.
	 */
	@Override
	protected void updateItem(R result, boolean empty) {
		super.updateItem(result, empty);
		TableRow<T> row = this.getTableRow();
		
		if (empty || m_activation.test(result)) {
			setStyleActive(row);
		} else if (!m_activation.test(result)) {
			setStyleInactive(row);
		}
	}

	/**
	 * Sets the row highlight and disabled property for rows
	 * determined inactive by the cell's activation predicate.
	 * 
	 * @param row - the row to be styled.
	 */
	private void setStyleInactive(TableRow<T> row) {
		row.setStyle(ROW_STYLE);
		this.setText(m_inactiveText);
		row.setDisable(true);
	}

	/**
	 * Resets the row style and enables it for empty rows or rows
	 * containing books that are available.
	 * 
	 * @param row - the row to be reset.
	 */
	private void setStyleActive(TableRow<T> row) {
		row.setStyle("");
		this.setText(m_activeText);
		row.setDisable(false);
	}
}
