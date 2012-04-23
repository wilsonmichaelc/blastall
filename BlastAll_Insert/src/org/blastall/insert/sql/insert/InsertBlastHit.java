package org.blastall.insert.sql.insert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.blastall.insert.sql.DBConnectionManager;
import org.blastall.xmlparser.objects.Hit;
import org.blastall.xmlparser.objects.Hsp;


/**
 * This class handles the insertion of Hits.
 * A Hit is a protein sequence that matches closely to the query protein sequence.
 * 
 * @author Michael Wilson <wilsonmc@u.washington.edu>
 * @version Feb 3, 2012
 */
public class InsertBlastHit {

	private static Logger log = Logger.getLogger(InsertBlastHit.class);
	private static final String QUERY = "INSERT INTO blastall.blast_hit (id, blastRequestId, hitNum, hitId, hitDef, hitAccession, hitLength) VALUES (NULL, ?, ?, ?, ?, ?, ?);";

	/**
	 * Inserts a Hit into the pdr database.
	 * @param blastRequestId
	 * @param hit
	 * @throws Throwable
	 */
	public void insert(int blastRequestId, Hit hit) throws Throwable {

		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet rskey = null;
		InsertBlastHsp insertHsp = null;
		int lastHitId = 0;

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
			statement.setInt(1 , blastRequestId);
			statement.setInt(2,  hit.getHit_num());
			statement.setString(3, hit.getHit_id());
			statement.setString(4, hit.getHit_def());
			statement.setInt(5,  hit.getHit_accession());
			statement.setInt(6,  hit.getHit_len());
			// Execute the query
			statement.executeUpdate();

			// Get the last hitId that was inserted.
			rskey = statement.getGeneratedKeys();
			if (rskey != null && rskey.next()) {
				lastHitId = rskey.getInt(1);
			}

			try {
				
				// New instance of InsertBlastHsp
				insertHsp = new InsertBlastHsp();

				// Iterate over the list of hsp's
				for (Hsp hsp: hit.getHit_hsps()){

					// insert the hsp into the database
					insertHsp.insert(hsp, lastHitId);	
				}
			} catch (Throwable t) {
				log.error("Could not insert Hsp", t);
				throw t;
			}


		} catch (Throwable t) {
			
			log.error("Could not execute query: " + QUERY, t);
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
	}
}
