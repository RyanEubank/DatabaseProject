package src.application.client.scenes.dialogs;

import java.time.LocalDate;
import java.time.format.*;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.util.StringConverter;

public class CalendarDialog extends AbstractDialogController<LocalDate> {
	
	@FXML
	private DatePicker calendar;
	
	private DateTimeFormatter m_format;
	
	/**
	 * Constructs a new CalendarDialog with the default selection
	 * set to the current system date.
	 */
	public CalendarDialog() {
		super(LocalDate.now());
		this.m_format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	}

	/**
	 * Sets the current date in the dialog box from the calendar selection.
	 */
	@Override
	public void onMakeSelection() {
		this.m_dialogRetVal = this.calendar.getValue();
	}
	
	/**
	 * Checks the validity of the date entered in the calendar text box
	 * and enables/disables the OK button based on parsing success.
	 * 
	 * @param date - the string to parse as the selected date.
	 */
	public void parseDate(String date) {
		try {
			LocalDate d = LocalDate.parse(date, this.m_format);
			this.calendar.setValue(d);
			this.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
		} catch (DateTimeParseException e) {
			this.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
		}
	}

	/**
	 * Sets the calendar date format to mm/dd/yyyy and sets its initial
	 * date and sets up a listener to automatically reparse the date
	 * if directly edited by the user. Adds a listener to prevent setting
	 * date backwards in time from its current selection.
	 */
	@Override
	public void initialize() {
		setCalendarFormat();
		this.calendar.setValue(m_dialogRetVal);
		this.calendar.getEditor().textProperty()
			.addListener((obs, oldDate, newDate) -> parseDate(newDate));
	}
	
	/**
	 * Sets the date format methods to convert the calendar's date to
	 * and from Date objects to strings.
	 */
	private void setCalendarFormat() {
		this.calendar.setConverter(new StringConverter<LocalDate>() {

			@Override
			public LocalDate fromString(String date) {
				if (date != null)
					return LocalDate.parse(date, m_format);
				else return null;
			}

			@Override
			public String toString(LocalDate date) {
				if (date != null)
					return m_format.format(date);
				else return "";
			}
			
		});
	}
	
	@Override
	public void postInitialize() {
		// does nothing, calendar does not require post initilization
	}

	/**
	 * Sets the calendar date.
	 * 
	 * @param date - the date to set.
	 */
	public void setDate(LocalDate date) {
		this.calendar.setValue(date);
	}
}
