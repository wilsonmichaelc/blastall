package org.blastall.insert.sql;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBConnectionManager {

	public static Connection getConnection() throws SQLException {

		try {
			Context ctx = new InitialContext();
			DataSource ds;
			Connection conn;

			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/blastall");

			if (ds != null) {
				conn = ds.getConnection();
				if (conn != null) { return conn; }
				else { throw new SQLException("Got a null connection..."); }
			}

			throw new SQLException("Got a null DataSource...");
		} catch (NamingException ne) {
			throw new SQLException("Naming exception: " + ne.getMessage());
		}
	}
}