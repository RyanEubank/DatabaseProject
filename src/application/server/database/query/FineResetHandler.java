package src.application.server.database.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FineResetHandler extends AbstractUpdateHandler {

	/**
	 * Clears all fines from the library database. Should be
	 * used only when setting system time backwards to
	 * recalculate fines.
	 */
	public static void reset() {
		new FineResetHandler().onExecute();
	}
	
	@Override
	protected String getQuery() {
		return "DELETE FROM Library.Fines";
	}

	@Override
	protected void setSubqueries(
		PreparedStatement statement, Object... subqueries
	) throws SQLException {
		// does not have any subqueries.
	}
}
