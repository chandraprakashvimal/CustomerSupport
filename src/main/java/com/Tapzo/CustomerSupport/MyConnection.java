package com.Tapzo.CustomerSupport;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MyConnection {

	public static Statement getConnection(String host, String port, String dbuser, String dbpassword)
			throws SQLException {
		Connection conn;
		// host = "172.16.0.75"; dbuser = "appuser"; dbpassword = "appuser";
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			System.out.println("JDBC Driver Not connected ?");
			e.printStackTrace();
			return null;
		}

		// System.out.println("MySQL JDBC Driver Registered!");

		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port, dbuser, dbpassword);
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return null;
		}

		if (conn == null) {

			System.out.println("Failed to make connection!");
		}

		Statement s = conn.createStatement();
		return s;
	}

	public static Integer update(Statement s, String query) throws SQLException {
		int rs = s.executeUpdate(query);
		// rs.next();
		return rs;
	}

	public static ResultSet execute(Statement s, String query) throws Exception {
		ResultSet rs = s.executeQuery(query);
		if (rs.next())
			return rs;
		else
			throw new Exception("No data found in db");
		// System.out.println("select user_android_id from
		// helpchat.user_access_code where akosha_access_code="+"'"+auth+"'");

		// ResultSet rs = stmt.executeQuery(
		// "select user_android_id from helpchat.user_access_code where
		// akosha_access_code=" + "'" + auth + "'");

		// while (rs.next()) {
		// System.out.println("FRom db =" + rs.getString("user_android_id"));
		// }
	}
}
