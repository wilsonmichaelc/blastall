package org.blastall.insert.sql.remove;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.blastall.insert.sql.DBConnectionManager;


/**
 * This class handles the insetion of BLAST Requests.
 * A request is generated when a user submits the request form on yeastrc.org/pdr/blast.jsp
 * 
 * @author Michael Wilson <wilsonmc@u.washington.edu>
 * @version Feb 2, 2012
 */
public class RemoveFailedProteins {

	private static Logger log = Logger.getLogger(RemoveFailedProteins.class);
	
	private static final String QUERY1 = "DELETE FROM blastall.blast_hsp where hitId IN (SELECT id FROM blastall.blast_hit WHERE blastRequestId = ?);";
	private static final String QUERY2 = "DELETE FROM blastall.blast_request_stats WHERE blastRequestId = ?;";
	private static final String QUERY3 = "DELETE FROM blastall.blast_hit WHERE blastRequestId = ?;";
	private static final String QUERY4 = "DELETE FROM blastall.blast_request WHERE proteinId = ?;";

	/**
	 * @param request
	 * @return blastRequestId
	 * @throws Throwable
	 */
	public void remove(int proteinID) throws Throwable {

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
			statement = connect.prepareStatement(QUERY1);
			// Set the statement parameters
			statement.setInt(1, proteinID);
			// Execute the query
			statement.executeUpdate();
			
			// Prepare the statement
			statement = connect.prepareStatement(QUERY2);
			// Set the statement parameters
			statement.setInt(1, proteinID);
			// Execute the query
			statement.executeUpdate();
			
			// Prepare the statement
			statement = connect.prepareStatement(QUERY3);
			// Set the statement parameters
			statement.setInt(1, proteinID);
			// Execute the query
			statement.executeUpdate();
			
			// Prepare the statement
			statement = connect.prepareStatement(QUERY4);
			// Set the statement parameters
			statement.setInt(1, proteinID);
			// Execute the query
			statement.executeUpdate();


		} catch (Throwable t) {
			
			log.error("Could not execute the query.", t);
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
