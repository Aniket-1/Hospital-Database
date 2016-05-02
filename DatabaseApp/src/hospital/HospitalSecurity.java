package hospital;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.math.BigInteger;
import java.sql.*;
import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import application.Main;
import business.*;

public class HospitalSecurity extends Main {

	private static SecureRandom random = new SecureRandom();
	private Type type = null;

	public Type getUserType() {
		return type;
	}

	/**
	 * Creates an account for a given user name. If the user name already
	 * exists, or is not a valid user name, nothing will happen. Invalid user
	 * names include empty strings and null values
	 * 
	 * @param username
	 *            The user name
	 * @param password
	 *            The password
	 * @return True if the user was added to the database. False otherwise
	 */
	public boolean newUser(User user, String username, String password) {
		if (username == null || username.trim().equals("") || password == null || password.trim().equals("")) {
			System.out.println("Username or password cannot be null");
			return false;
		}

		// User does not exist
		if (!login(username, password)) {
			PreparedStatement stmt = db
					.prepareStatement("INSERT INTO users VALUES " + "(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

			String salt = getSalt();

			try {
				stmt.setString(1, username);
				stmt.setString(2, salt);
				stmt.setBytes(3, hash(password, salt));
				stmt.setString(4, user.getFirstname());
				stmt.setString(5, user.getLastname());
				stmt.setString(6, user.getPhone());
				stmt.setString(7, user.getEmail());
				stmt.setString(10, user.getType().toString());

				switch (user.getType()) {
				case Admin:
					stmt.setFloat(8, ((Admin) user).getSalary());
					stmt.setObject(9, null);
					break;
				case Doctor:
					stmt.setFloat(8, ((Doctor) user).getSalary());
					stmt.setObject(9, null);
					break;
				case Patient:
					stmt.setObject(8, null);
					stmt.setObject(9, ((Patient) user).getNotes());
					break;
				default:
					break;
				}
			} catch (SQLException e) {
			}

			try {
				// Add user
				db.executeUpdateStatement(stmt);
				System.out.println("Successfully added user " + username);
				return true;
			} catch (SQLException e) {
				System.out.println("SQLException: " + e.getMessage());
				System.out.println("SQLState: " + e.getSQLState());
				System.out.println("VendorError: " + e.getErrorCode());
				System.out.println("An error occured while updating table");
				return false;
			}
		}
		return false;
	}

	/**
	 * Checks whether an account exists for the given user name
	 * 
	 * @param username
	 *            The user name to be checked
	 * @param password
	 *            The password of the user
	 * @return True if the user name belongs to a valid user. False otherwise
	 */
	public boolean login(String username, String password) {
		if (username == null || username.trim().equals("")) {
			return false;
		}

		String salt;
		byte[] hash;

		try {
			// Create statement
			PreparedStatement stmt = db.prepareStatement(
					"SELECT salt, hash, type, user_id "
							+ "FROM users "
							+ "WHERE username=?;");

			stmt.setString(1, username);

			// Execute statement
			ResultSet rs = db.executeStatement(stmt);
			rs.next();

			// get salt
			salt = rs.getString(1);
			// get hash
			hash = rs.getBytes(2);
			// get type
			type = Type.valueOf(rs.getString(3));
			// get user id
			userId = rs.getInt(4);
		} catch (SQLException e) {
			return false;
		}

		// Return equality of hashes
		return Arrays.equals(hash, hash(password, salt));
	}

	/**
	 * Generates a random {@code String}
	 * 
	 * @return The randomly generated {@code String}
	 */
	public String getSalt() {
		return new BigInteger(140, random).toString(32);
	}

	/**
	 * Creates a hash from the given password and salt
	 * 
	 * @param password
	 *            The password to be used in the hash
	 * @param salt
	 *            The salt to be used in the hash
	 * @return The has
	 */
	public byte[] hash(String password, String salt) {
		if (password == null || password.equals("")) {
			return null;
		}

		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");

			PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 1024, 256);

			SecretKey key = skf.generateSecret(spec);
			byte[] hash = key.getEncoded();
			return hash;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new RuntimeException(e);
		}
	}
}
