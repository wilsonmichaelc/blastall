package org.blastall.insert.sql.select;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.blastall.insert.sql.DBConnectionManager;
import org.blastall.xmlparser.objects.Statistics;
/**
 * This class handles the selection of a BLAST Hits from the database
 * 
 * @author Michael Wilson <wilsonmc@u.washington.edu>
 * @version Feb 2, 2012
 */
public class SelectBlastRequestStats {

	private static Logger log = Logger.getLogger(SelectBlastRequestStats.class);
	private static final String QUERY = "select db_num, db_len, hsp_len, eff_space, kappa, lambda, entropy from blastall.blast_request_stats where blastRequestId=?;";
	
	/**
	 * @param blastRequestId
	 * @param first
	 * @param last
	 * @return
	 * @throws Throwable
	 */
	public Statistics getHits(int blastRequestId) throws Throwable {

		Connection connect = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Statistics stats = null;
		
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

			// Create new stat
			stats = new Statistics();
			
			// Write the results to the screen
			while (resultSet.next()) {

				stats.setStatistics_db_num(resultSet.getInt("db_num"));
				stats.setStatistics_db_len(resultSet.getInt("db_len"));
				stats.setStatistics_hsp_len(resultSet.getInt("hsp_len"));
				stats.setStatistics_eff_space(resultSet.getInt("eff_space"));
				stats.setStatistics_kappa(resultSet.getDouble("db_num"));
				stats.setStatistics_lambda(resultSet.getDouble("kappa"));
				stats.setStatistics_entropy(resultSet.getDouble("entropy"));

			}

		} catch (Throwable t) {

			log.error("Could not execute the query: " + QUERY, t);
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
		return stats;
	}

}
