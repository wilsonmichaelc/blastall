package org.blastall.get.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.blastall.get.objects.Protein;

/**
 * This class handles the selection of a BLAST Request from the database
 * 
 * @author Michael Wilson <wilsonmc@u.washington.edu>
 * @version Feb 2, 2012
 */
public class SelectFromHits {

	private static Logger log = Logger.getLogger(SelectFromHits.class);
	private static final String QUERY = "SELECT a.proteinID, a.sequence FROM "+
										"pdr.proteinsToBlast AS a INNER JOIN blastall.blast_hit AS b ON a.proteinID = b.hitDef "+
										"WHERE a.proteinID NOT IN ( SELECT proteinId FROM blastall.blast_request ) "+
										"LIMIT 100";

	/**
	 * @param 
	 * @return list of Protein
	 * @throws Throwable
	 */
	public List<Protein> getResults() throws Throwable {

		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Protein protein = null;
		List<Protein> list = null;
		Connection connect = null;
		
		try {

			//Setup the db connection
			connect = DBConnectionManager.getConnection();

		} catch (Throwable t) {

			log.error("Could not get connection from DBConnection Manager", t);
			throw t;

		}
		
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");

			// Statements allow to issue SQL queries to the database
			statement = connect.prepareStatement(QUERY);

			// Result set get the result of the SQL query
			resultSet = statement.executeQuery();

			// Create new list object
			list = new ArrayList<Protein>();
			
			// set the request object properties.
			while (resultSet.next()) {

				protein = new Protein();
				
				protein.setProteinId(resultSet.getInt("proteinID"));
				protein.setProteinSequence(resultSet.getString("sequence"));
				
				list.add(protein);
			}
			
		} catch (Throwable t) {
			
			log.error("Could not execute query: " + QUERY, t);
			throw t;
			
		} finally {
			try {
				
				// Close the connections.
				if (resultSet != null) try {resultSet.close();} catch (SQLException e) { log.warn("Failed to close resultSet.", e); }
				resultSet = null;

				if (statement != null) try {statement.close();} catch (SQLException e) { log.warn("Failed to close preparedStatement.", e); }
				statement = null;
				
				if (connect != null) try {connect.close();} catch (SQLException e) { log.warn("Failed to close connect.", e); }
				connect = null;

			} catch (Throwable t) {

				log.warn("Database connection(s) were not closed.", t);
				
			}
		}
		return list;
	}
}