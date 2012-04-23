package org.blastall.insert.sql.select;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.blastall.insert.objects.Hsp;
import org.blastall.insert.sql.DBConnectionManager;


/**
 * This class handles the selection of a BLAST Hsp's from the database
 * 
 * @author Michael Wilson <wilsonmc@u.washington.edu>
 * @version Feb 2, 2012
 */
public class SelectBlastRequestHsps {
	
	private static Logger log = Logger.getLogger(SelectBlastRequestHsps.class);
	private static final String QUERY = "SELECT hitId, hspNum, hspBitScore, hspScore, hspEValue, hspQueryFrom, " + 
	"hspQueryTo, hspHitFrom, hspHitTo, hspQueryFrame, " + 
	"hspIdentity, hspPositive, hspGaps, hspAlignmentLength," + 
	" hspQuerySeq, hspHitSeq, hspMidline" +
	" FROM blastall.blast_hsp WHERE hitId=?;";
	
	/**
	 * @param hitId
	 * @return
	 * @throws Throwable
	 */
	public List<Hsp> getHsps(int hitId) throws Throwable {
		
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Hsp> hspList = null;
		Hsp hsp = null;
		
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DBConnectionManager.getConnection();

			// Statements allow to issue SQL queries to the database
			preparedStatement = connect.prepareStatement(QUERY);
			
			// Set the blastRequestId
			preparedStatement.setInt(1, hitId);
			
			// Result set get the result of the SQL query
			resultSet = preparedStatement.executeQuery();
			
			// Create new list
			hspList = new ArrayList<Hsp>();
				
			// Write the results to the screen
			while (resultSet.next()) {

				// Create new hsp object
				hsp = new Hsp();

				hsp.setHit_id(hitId);
				hsp.setHsp_num(resultSet.getInt("hspNum"));
				hsp.setHsp_bit_score(resultSet.getDouble("hspBitScore"));
				hsp.setHsp_score(resultSet.getInt("hspScore"));
				hsp.setHsp_evalue(resultSet.getDouble("hspEValue"));
				hsp.setHsp_query_from(resultSet.getInt("hspQueryFrom"));
				hsp.setHsp_query_to(resultSet.getInt("hspQueryTo"));
				hsp.setHsp_hit_from(resultSet.getInt("hspHitFrom"));
				hsp.setHsp_hit_to(resultSet.getInt("hspHitTo"));
				hsp.setHsp_query_frame(resultSet.getInt("hspQueryFrame"));
				hsp.setHsp_identity(resultSet.getInt("hspIdentity"));
				hsp.setHsp_positive(resultSet.getInt("hspPositive"));
				hsp.setHsp_gaps(resultSet.getInt("hspGaps"));
				hsp.setHsp_align_len(resultSet.getInt("hspAlignmentLength"));
				hsp.setHsp_qseq(resultSet.getString("hspQuerySeq"));
				hsp.setHsp_hseq(resultSet.getString("hspHitSeq"));
				hsp.setHsp_midline(resultSet.getString("hspMidline"));
				
				// Add this result to the list
				hspList.add(hsp);
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
		return hspList;
	}
}
