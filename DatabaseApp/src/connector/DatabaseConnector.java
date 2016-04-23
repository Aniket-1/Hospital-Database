package connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnector {

	// The connection to the database
	private Connection conn = null;

	/**
	 * Creates a connection to the localhost database
	 */
	public DatabaseConnector(String username, String password) throws SQLException {
		this("jdbc:mysql://localhost", username, password);
	}

	/**
	 * Create a connection to a database
	 * 
	 * @param url
	 *            The URL of the database
	 * @param user
	 *            The username to be used to access the database
	 * @param password
	 *            The password to be used to access the database
	 * @throws SQLException
	 *             If a database connection error occurs
	 */
	public DatabaseConnector(String url, String user, String password) throws SQLException {

		if (user == null || user.trim() == "") {
			System.out.println("Cannot create connection");
			throw new SQLException("Cannot create connection with username " + user);
		}

		if (url == null || url.trim() == "") {
			url = "jdbc:mysql://localhost";
		}

		if (!url.startsWith("jdbc:mysql://")) {
			url = "jdbc:mysql://" + url;
		}

		try {
			System.out.println("Creating connection...");
			conn = DriverManager.getConnection(url, user, password);
			System.out.println("Connection created successfully");
		} catch (SQLException e) {
			System.out.println("Creating connection failed");
			throw e;
		}
	}

	/**
	 * Closes connection to database
	 */
	public void closeConnection() throws SQLException {
		try {
			System.out.println("\nClosing connection...");
			conn.close();
			System.out.println("Connection closed successfully");
		} catch (SQLException e) {
			System.out.println("Connection could not be closed successfully");
			throw e;
		}
	}

	/**
	 * Gets results from an executed SQL statement. If the statement does not
	 * return anything, null will be returns. If the statement cannot be
	 * successfully executed, an error message will be shown, and null will be
	 * returned.
	 * 
	 * @param statement
	 *            The MySQL statement to be queried
	 * @return The results of the executed statement
	 */
	public ResultSet executeStatement(String statement) {

		if (statement == null || statement.trim().equals("")) {
			return null;
		}

		return executeStatement(prepareStatement(statement));
	}

	/**
	 * Gets results from an executed SQL statement. If the statement does not
	 * return anything, null will be returned. If the statement cannot be
	 * successfully executed, an error message will be shown, and null will be
	 * returned.
	 * 
	 * @param statement
	 *            The MySQL statement to be queried
	 * @return The {@code ResultSet} of the executed statement
	 */
	public ResultSet executeStatement(PreparedStatement statement) {

		if (statement == null || statement.equals("")) {
			return null;
		}

		ResultSet rs = null;

		try {
			rs = statement.executeQuery();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			System.out.println("Query could not be executed successfully");
		}

		return rs;
	}

	/**
	 * Executes an update statement from a given {@code PreparedStatement}, and
	 * closes the statement once execution is completed
	 * 
	 * @param statement
	 *            The statement to be executed
	 * @throws SQLException
	 *             If the statement is not a valid DML statement
	 */
	public void executeUpdateStatement(PreparedStatement statement) throws SQLException {
		if (statement != null) {
			statement.executeUpdate();
			statement.close();
		}
	}

	/**
	 * Gets connection to the database
	 * 
	 * @return The {@code Connection} to the database
	 */
	public Connection getConnection() {
		return conn;
	}

	/**
	 * Returns a prepared statement from the given {@code String}. Returns null
	 * if the statement cannot be prepared.
	 * 
	 * @param sql
	 *            The statement to be prepared
	 * @return The updatable, scroll sensitive {@code PreparedStatement}
	 */
	public PreparedStatement prepareStatement(String sql) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		} catch (SQLException e) {
			System.out.println("An error occured with the prepared statement");
		}
		return stmt;
	}
}
