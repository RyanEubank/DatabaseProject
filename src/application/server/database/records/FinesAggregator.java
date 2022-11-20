package src.application.server.database.records;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class FinesAggregator {

	/**
	 * Aggregates the query results for fines into a list of total
	 * owed and total already paid grouped by borrower.
	 * 
	 * @param results - the fines retrieved from a database query.
	 * @return
	 *  Returns a list of rows with summarized fine information.
	 */
	public static List<FineSummaryResult> summarize(List<FineSearchResult> results) {
		List<FineSummaryResult> summary = new ArrayList<>();
		results.stream().collect(
			Collectors.groupingBy(fine -> fine.getBorrowerID(), 
			Collectors.groupingBy(fine -> fine.getIsPaid())
		)).entrySet().forEach((map) -> { addBorrowerSummary(summary, map); });
		return summary;
	}

	/**
	 * Gets the list of paid fines and list of unpaid fines from
	 * tha given mapping collected by borrower and adds those sums to
	 * the summary list.
	 * 
	 * @param summary - the borrower summary list to be populated.
	 * @param map - the mapping from a borrower to their list of unpaid
	 * 			 	and paid fines.
	 */
	private static void addBorrowerSummary(
		List<FineSummaryResult> summary,
		Entry<Integer, Map<Boolean, List<FineSearchResult>>> map
	) {
		Map<Boolean, List<FineSearchResult>> borrowerSummary = map.getValue();
		List<FineSearchResult> unpaidFines = borrowerSummary.get(false);
		List<FineSearchResult> paidFines = borrowerSummary.get(true);
		int borrowerID = map.getKey();
		addSumsToList(borrowerID, paidFines, unpaidFines, summary);
	}

	/**
	 * Sums the fines in the list of paid and unpaid fines and updates
	 * the borrower summary list with a new entry for the given borrower.
	 * 
	 * @param borrowerID - the library card ID of the borrower being summarized.
	 * @param paidFines - the list of already paid fine records for the borrower.
	 * @param unpaidFines - the list of outstanding fine records for the borrower.
	 * @param summary - the summary list to add the new entry to.
	 */
	private static void addSumsToList(
		int borrowerID,
		List<FineSearchResult> paidFines, 
		List<FineSearchResult> unpaidFines,
		List<FineSummaryResult> summary
	) {
		String name = findBorrowerName(paidFines, unpaidFines);
		double totalOwed = sumFines(unpaidFines);
		double totalPaid = sumFines(paidFines);		
		FineSummaryResult summaryRecord = new FineSummaryResult(
			borrowerID, name, totalOwed, totalPaid
		);
		summary.add(summaryRecord);
	}

	/**
	 * Gets the borrower name from the first available record in either
	 * the paid of unpaid list of fines. Borrowers may have 1 or the other,
	 * or both.
	 * 
	 * @param paidFines - the list of already paid fine records for the borrower.
	 * @param unpaidFines - the list of outstanding fine records for the borrower.
	 * @return
	 */
	private static String findBorrowerName(
		List<FineSearchResult> paidFines, List<FineSearchResult> unpaidFines
	) {
		return (paidFines != null)
			? paidFines.get(0).getName()
			: unpaidFines.get(0).getName();
	}
	
	/**
	 * Sums the fine amount column in all records in the given list and
	 * returns the total.
	 * 
	 * @param fines - a list of fines to be summed.
	 * @return
	 *  Returns a double value for the sum of the total amount of all 
	 *  fines in the given list.
	 */
	private static double sumFines(List<FineSearchResult> fines) {
		return (fines != null)
			? fines.stream().mapToDouble((fine) -> fine.getAmount()).sum()
			: 0.0f;
	}
}
