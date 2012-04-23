package org.blastall.insert.sql.select;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.blastall.insert.objects.Request;
import org.blastall.insert.sql.DBConnectionManager;

/**
 * This class handles the selection of a BLAST Request from the database
 * 
 * @author Michael Wilson <wilsonmc@u.washington.edu>
 * @version Feb 2, 2012
 */
public class SelectBlastRequest {

	private static Logger log = Logger.getLogger(SelectBlastRequest.class);

	private static final String QUERY = "select id, sequence, proteinId, submitDate from blastall.blast_request where id=?;";

	/**
	 * @param blastRequestId
	 * @return
	 * @throws Throwable
	 */
	public Request getResults(int blastRequestId) throws Throwable {

		Connection connect = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Request request = null;

		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DBConnectionManager.getConnection();

			// Statements allow to issue SQL queries to the database
			preparedStatement = connect.prepareStatement(QUERY);

			// Set the blastRequestId
			preparedStatement.setInt(1, blastRequestId);

			// Result set get the result of the SQL query
			resultSet = preparedStatement.executeQuery();

			// Create new request object
			request = new Request();
			
			// set the request object properties.
			while (resultSet.next()) {

				request.setSequence(resultSet.getString("sequence"));
				request.setProteinId(resultSet.getInt("proteinId"));
				request.setSubmitDate(resultSet.getString("submitDate"));

			}
			
		} catch (Throwable t) {
			
			log.error("Could not execute query: " + QUERY, t);
			throw t;
			
		} finally {
			try {
				
				// Close the connections.
				if (resultSet != null) try {resultSet.close();} catch (SQLException e) { log.warn("Failed to close resultSet.", e); }
				resultSet = null;

				if (preparedStatement != null) try {preparedStatement.close();} catch (SQLException e) { log.warn("Failed to close preparedStatement.", e); }
				preparedStatement = null;

				if (connect != null) try {connect.close();} catch (SQLException e) { log.warn("Failed to close connect.", e); }
				connect = null;

			} catch (Throwable t) {

				log.warn("Database connection(s) were not closed.", t);
				
			}
		}
		return request;
	}
}
