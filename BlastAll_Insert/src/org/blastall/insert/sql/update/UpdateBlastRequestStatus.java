package org.blastall.insert.sql.update;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.blastall.insert.sql.DBConnectionManager;

/**
 * This class handles the update of the BLAST Request status code
 * 
 * @author Michael Wilson <wilsonmc@u.washington.edu>
 * @version Feb 7, 2012
 */
public class UpdateBlastRequestStatus {

	private static Logger log = Logger.getLogger(UpdateBlastRequestStatus.class);
	private static String query = "UPDATE blastall.blast_request SET status = ? WHERE id = ?;";

	/**
	 * @param blastRequestId
	 * @param status
	 * @throws Throwable
	 */
	public void update(int blastRequestId, int status) throws Throwable{


		Connection connect = null;
		PreparedStatement statement = null;

		try {

			//Setup the db connection
			connect = DBConnectionManager.getConnection();

		} catch (Throwable t) {

			log.error("Could not get connection from DBConnection Manager", t);
			throw t;

		}

		try {

			// Prepare the statement
			statement = connect.prepareStatement(query);

			// Set the statement parameters
			statement.setInt(1, status);
			statement.setInt(2, blastRequestId);

			// Execute the query
			statement.executeUpdate();

		} catch (Throwable t) {

			log.error("Could not execute the query: " + query, t);
			throw t;

		} finally {
			try {

				// Close the connections.
				if (statement != null) try {statement.close();} catch (SQLException e) { ; }
				statement = null;

				if (connect != null) try {connect.close();} catch (SQLException e) { ; }
				connect = null;

			} catch (Throwable t) {

				log.warn("Database connections were not closed.", t);

			}
		}



	}

}
