package org.blastall.insert.sql.insert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.blastall.insert.sql.DBConnectionManager;
import org.blastall.xmlparser.objects.Hsp;

/**
 * This class handles the insertion of blast request Hsp's.
 * For each Hit, there can be one or more Hsp.
 * 
 * @author Michael Wilson <wilsonmc@u.washington.edu>
 * @version Feb 2, 2012
 */
public class InsertBlastHsp {

	private static Logger log = Logger.getLogger(InsertBlastHsp.class);
	private static final String QUERY = "INSERT INTO blastall.blast_hsp (hitId, hspNum, hspBitScore, hspScore, hspEValue, hspQueryFrom, " + 
	"hspQueryTo, hspHitFrom, hspHitTo, hspQueryFrame, " + 
	"hspIdentity, hspPositive, hspGaps, hspAlignmentLength," + 
	" hspQuerySeq, hspHitSeq, hspMidline)" +
	" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";


	/**
	 * Inserts an Hsp into the pdr database
	 * @param hsp
	 * @throws Throwable
	 */
	public void insert(Hsp hsp, int lastHitId) throws Throwable {

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

			// Set the statement parameters.
			statement.setInt(1 , lastHitId);
			statement.setInt(2,  hsp.getHsp_num());
			statement.setDouble(3, hsp.getHsp_bit_score());
			statement.setInt(4, hsp.getHsp_score());
			statement.setDouble(5, hsp.getHsp_evalue());
			statement.setInt(6, hsp.getHsp_query_from());
			statement.setInt(7, hsp.getHsp_query_to());
			statement.setInt(8, hsp.getHsp_hit_from());
			statement.setInt(9, hsp.getHsp_hit_to());
			statement.setInt(10, hsp.getHsp_query_frame());
			statement.setInt(11, hsp.getHsp_identity());
			statement.setInt(12, hsp.getHsp_positive());
			statement.setInt(13, hsp.getHsp_gaps());
			statement.setInt(14, hsp.getHsp_align_len());
			statement.setString(15, hsp.getHsp_qseq());
			statement.setString(16, hsp.getHsp_hseq());
			statement.setString(17, hsp.getHsp_midline());

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
