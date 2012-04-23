package org.blastall.insert.sql.select;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.blastall.insert.objects.Hit;
import org.blastall.insert.sql.DBConnectionManager;
/**
 * This class handles the selection of a BLAST Hits from the database
 * 
 * @author Michael Wilson <wilsonmc@u.washington.edu>
 * @version Feb 2, 2012
 */
public class SelectBlastRequestHits {

	private static Logger log = Logger.getLogger(SelectBlastRequestHits.class);

	private static final String QUERY = "select id, hitNum, hitAccession, hitLength, hitId, hitDef from blastall.blast_hit where (blastRequestId=? && hitNum>=? && hitNum<=?) order by hitNum;";

	
	/**
	 * @param blastRequestId
	 * @param first
	 * @param last
	 * @return
	 * @throws Throwable
	 */
	public List<Hit> getHits(int blastRequestId, int first, int last) throws Throwable {

		Connection connect = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Hit> hitList = null;
		Hit hit = null;
		SelectBlastRequestHsps selectHsps = null;
		
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DBConnectionManager.getConnection();

			// Statements allow to issue SQL queries to the database
			preparedStatement = connect.prepareStatement(QUERY);

			// Set the blastRequestId
			preparedStatement.setInt(1, blastRequestId);
			preparedStatement.setInt(2, first);
			preparedStatement.setInt(3, last);

			// Result set get the result of the SQL query
			resultSet = preparedStatement.executeQuery();

			// Create new list
			hitList = new ArrayList<Hit>();
			selectHsps = new SelectBlastRequestHsps();
			
			// Write the results to the screen
			while (resultSet.next()) {

				// Create new hit object
				hit = new Hit();

				hit.setHit_num(resultSet.getInt("hitNum"));
				hit.setHit_accession(resultSet.getInt("hitAccession"));
				hit.setHit_len(resultSet.getInt("hitLength"));
				hit.setHit_id(resultSet.getString("hitId"));
				hit.setHit_def(resultSet.getString("hitDef"));		
				hit.setHit_hsps(selectHsps.getHsps((resultSet.getInt("id"))));
				//hit.setBestEValue(bestEValue)
				// Add this result to the list
				hitList.add(hit);
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
		return hitList;
	}

	public int getTotalHits(int blastRequestId) throws Throwable {
		
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int total_hits = 0;

		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DBConnectionManager.getConnection();

			// Statements allow to issue SQL queries to the database
			preparedStatement = connect.prepareStatement("SELECT COUNT(*) AS TotalHits FROM blast_hit WHERE blastRequestId=?;");

			// Set the blastRequestId
			preparedStatement.setInt(1, blastRequestId);

			// Result set get the result of the SQL query
			resultSet = preparedStatement.executeQuery();

			// Get the total number of results.
			while (resultSet.next()){
				total_hits = resultSet.getInt("TotalHits");
			}

		} catch (Throwable t) {

			log.error("Could not execute the query.", t);
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

		return total_hits;
	}

}
