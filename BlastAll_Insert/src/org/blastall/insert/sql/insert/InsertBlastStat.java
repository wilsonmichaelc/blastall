package org.blastall.insert.sql.insert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.blastall.insert.sql.DBConnectionManager;
import org.blastall.xmlparser.objects.Statistics;


/**
 * This class handles the insetion of BLAST Requests.
 * A request is generated when a user submits the request form on yeastrc.org/pdr/blast.jsp
 * 
 * @author Michael Wilson <wilsonmc@u.washington.edu>
 * @version Feb 2, 2012
 */
public class InsertBlastStat {

	private static Logger log = Logger.getLogger(InsertBlastStat.class);
	private static final String QUERY = "INSERT INTO blastall.blast_request_stats (id, blastRequestId, db_num, db_len, hsp_len, eff_space, kappa, lambda, entropy) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?);";

	/**
	 * @param request
	 * @return blastRequestId
	 * @throws Throwable
	 */
	public void insert(int blastRequestId, Statistics stats) throws Throwable {

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
			statement = connect.prepareStatement(QUERY);

			// Set the statement parameters
			statement.setInt(1, blastRequestId);
			statement.setInt(2, stats.getStatistics_db_num());
			statement.setLong(3, stats.getStatistics_db_len());
			statement.setInt(4, stats.getStatistics_hsp_len());
			statement.setLong(5, stats.getStatistics_eff_space());
			statement.setDouble(6, stats.getStatistics_kappa());
			statement.setDouble(7, stats.getStatistics_lambda());
			statement.setDouble(8, stats.getStatistics_entropy());

			//DEBUG
			//log.debug("EffSpace: " + stat.getEffSpace());
			//System.out.println("EffSpace" + stat.getEffSpace());
			// Execute the query
			statement.executeUpdate();



		} catch (Throwable t) {
			
			log.error("Could not execute the query: " + QUERY, t);
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
