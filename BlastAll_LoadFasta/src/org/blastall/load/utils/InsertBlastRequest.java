package org.blastall.load.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This class handles the insetion of BLAST Requests.
 * A request is generated when a user submits the request form on yeastrc.org/pdr/blast.jsp
 * 
 * @author Michael Wilson <wilsonmc@u.washington.edu>
 * @version Feb 2, 2012
 */
public class InsertBlastRequest {

	private static final String QUERY = "INSERT INTO blastall.proteinsToBlast (id, proteinId, sequence) VALUES (NULL, ?, ?);";
	private int blastRequestId;

	/**
	 * @param request
	 * @return blastRequestId
	 * @throws Throwable
	 */
	public int insert(int proteinId, String sequence) throws Throwable {

		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet result = null;

		try {

			//Setup the db connection
			connect = DBConnectionManager.getConnection();

		} catch (Throwable t) {

			System.out.println("Could not get connection from DBConnection Manager: \n" + t);
			throw t;

		}

		try {

			// Prepare the statement
			statement = connect.prepareStatement(QUERY);

			// Set the statement parameters
			statement.setInt(1, proteinId);
			statement.setString(2, sequence);


			// Execute the query
			statement.executeUpdate();

		} catch (Throwable t) {
			
			System.out.println("Could not execute the query: " + QUERY + "\n" + t);
			throw t;
			
		} finally {
			try {
				
				// Close the connections.
				if (result != null) try {result.close();} catch (SQLException e) { ; }
				result = null;

				if (statement != null) try {statement.close();} catch (SQLException e) { ; }
				statement = null;

				if (connect != null) try {connect.close();} catch (SQLException e) { ; }
				connect = null;

			} catch (Throwable t) {

				System.out.println("Database connections were not closed: \n" + t);

			}
		}

		return blastRequestId;
	}
}
