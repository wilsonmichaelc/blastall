package org.blastall.insert.sql.insert;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.blastall.insert.objects.Request;
import org.blastall.insert.sql.DBConnectionManager;


/**
 * This class handles the insetion of BLAST Requests.
 * A request is generated when a user submits the request form on yeastrc.org/pdr/blast.jsp
 * 
 * @author Michael Wilson <wilsonmc@u.washington.edu>
 * @version Feb 2, 2012
 */
public class InsertBlastRequest {

	private static Logger log = Logger.getLogger(InsertBlastRequest.class);
	private static final String QUERY = "INSERT INTO blastall.blast_request (id, sequence, proteinId, submitDate) VALUES (NULL, ?, ?, NULL);";
	
	/**
	 * @param request
	 * @return blastRequestId
	 * @throws Throwable
	 */
	public int insert(Request request) throws Throwable {

		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet rskey = null;
		int blastRequestId = 0;
		
		try {

			//Setup the db connection
			connect = DBConnectionManager.getConnection();

		} catch (Throwable t) {

			log.error("Could not get connection from DBConnection Manager", t);
			throw t;

		}

		try {

			// Prepare the statement
			statement = connect.prepareStatement(QUERY, Statement.RETURN_GENERATED_KEYS);

			// Set the statement parameters
			statement.setString(1, request.getSequence());
			statement.setInt(2, request.getProteinId());

			// Execute the query
			statement.executeUpdate();

			// Get the last blastRequestId.
			rskey = statement.getGeneratedKeys();
			if (rskey != null && rskey.next()) {
				blastRequestId = rskey.getInt(1);
			}

		} catch (Throwable t) {
			
			log.error("Could not execute the query: " + QUERY, t);
			throw t;
			
		} finally {
			try {
				
				// Close the connections.
				if (rskey != null) try {rskey.close();} catch (SQLException e) { ; }
				rskey = null;
				
				if (statement != null) try {statement.close();} catch (SQLException e) { ; }
				statement = null;

				if (connect != null) try {connect.close();} catch (SQLException e) { ; }
				connect = null;
				

			} catch (Throwable t) {

				log.warn("Database connections were not closed.", t);

			}
		}

		return blastRequestId;
	}
}
