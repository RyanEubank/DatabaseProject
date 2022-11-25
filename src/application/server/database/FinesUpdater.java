package src.application.server.database;

import java.time.*;

import src.application.server.database.query.*;

public class FinesUpdater {
	
	private long m_numTimesExecute;
	
	/**
	 * Updates the fines accrued for the provided date.
	 * 
	 * @parm current - the current date in the system.
	 * @param next - the new date to update the database with.
	 * 
	 * @return
	 *  Returns the total number of updates performed.
	 */
	public long onUpdateDate(LocalDate current, LocalDate next) {
		
		// simply passing dates will throw an exception, requires
		// time with seconds precision
		this.m_numTimesExecute = Duration.between(
			current.atStartOfDay(), next.atStartOfDay()
		).toDays();
		
		return executeScriptNumTimes(m_numTimesExecute, next);
	}

	/**
	 * Executes the update script the given number of times and returns
	 * the sum of the rows affected over all runs.
	 * 
	 * @param numRuns - the number of times to run the batch script.
	 * @param current - the current date in the system.
	 * 
	 * @return
	 *  Returns the total number of affected rows from every run
	 *  of the script.
	 */
	private long executeScriptNumTimes(long numRuns, LocalDate current) {
		long total = 0;
		for (int i = 0; i < m_numTimesExecute; i++) {
			current = current.plusDays(1);
			int inserts = new FineInsertHandler().insertFines(current);
			total += inserts;
			int updates = new FineUpdateHandler().updateFines();
			total += updates;
		}
		return total;
	}
}
