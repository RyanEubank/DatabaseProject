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
	 */
	public void onUpdateDate(LocalDate current, LocalDate next) {
		
		// simply passing dates will throw an exception, requires
		// time with seconds precision
		this.m_numTimesExecute = Duration.between(
			current.atStartOfDay(), next.atStartOfDay()
		).toDays();
		
		// if the new date is in the past from the current date then
		// fines are reset and recalculated, otherwise they are update as normal
		if (this.m_numTimesExecute > 0)
			executeScriptNumTimes(this.m_numTimesExecute, current);
		else {
			FineResetHandler.reset();
			executeScriptNumTimes(1, current.plusDays(-1));
		}
	}

	/**
	 * Executes the update script the given number of times and returns
	 * the sum of the rows affected over all runs.
	 * 
	 * @param numRuns - the number of times to run the batch script.
	 * @param current - the current date in the system.
	 */
	private void executeScriptNumTimes(long numRuns, LocalDate current) {
		Runnable task = () -> {
			long total = 0;
			
			for (int i = 0; i < numRuns; i++) {
				LocalDate date = current.plusDays(i + 1);
				int inserts = new FineInsertHandler().insertFines(date);
				total += inserts;
				int updates = new FineUpdateHandler().updateFines(date);
				total += updates;
				System.out.println("Update for " + date);
			}
			
			System.out.println("Total updates: " + total);
		};
		new Thread(task).start();;
	}
}
