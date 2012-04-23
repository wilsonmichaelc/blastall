package org.blastall.load.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionManager {

	private static Connection connect = null;
	
	public static Connection getConnection() throws SQLException {

		connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/blastall?user=root");

		return connect;
	}

}
