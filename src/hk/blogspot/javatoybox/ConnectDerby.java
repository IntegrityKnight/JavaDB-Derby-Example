package hk.blogspot.javatoybox;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

// This example use Java 7 feature, you must update your OpenJDK before run this program.
public class ConnectDerby {

	private String dbURL;
	private Connection con;

	public ConnectDerby(String url) {
		dbURL = url;
		
		Properties properties = new Properties();
        properties.put("create", "true");
        properties.put("user", "Stanley");
        properties.put("password", "javatoybox");
        
		try {
			con = DriverManager.getConnection(dbURL, properties);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Create, read, update and delete test
	private void testingCRUD() {
		
		//Create
		writeBLOB("INSERT INTO acg_character.profile (media_1_filename,media_1,product) VALUES (?,?,\'ご注文はうさぎですか？\')", 
				"A:\\javatoybox\\test_data\\test\\insert.jpg");
				
		//read
		read("SELECT id, actor, product FROM acg_character.profile");
		
		//delete
		delete("DELETE FROM acg_character.profile WHERE id = ?", 3);
		
		//update
		writeBLOB("UPDATE acg_character.profile SET media_1_filename = ?, media_1 = ? WHERE id = 1", 
				"A:\\javatoybox\\test_data\\test\\insert.jpg");
		
		
		//export BLOB to a file
		extractBLOB("SELECT media_1_filename, media_1 FROM acg_character.profile WHERE id = 1", 
				"media_1_filename");
		
	}

	public void delete(String sql, int record_row_id) {
		try {
			
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, record_row_id);
			ps.execute();
			ps.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeBLOB(String sql, String filename) {

		try {
			Path path = Paths.get(filename);
			File file = path.toFile();
			FileInputStream inputStream = new FileInputStream(file);

			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, file.getName());
			ps.setBlob(2, inputStream);
			ps.execute();
			ps.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void extractBLOB(String sql, String column_name) {
		try {

			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery(sql);
			Blob music = null;

			if (rs.next())
				music = rs.getBlob(2);

			Path file = Paths.get("A:\\javatoybox\\test_data\\", rs.getString(column_name));
			Files.copy(music.getBinaryStream(), file);

			rs.close();
			s.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void create(String sql) {
	}

	/*
	 * Why don't use Java Iterator ( enhanced for loop ) to list column name in
	 * ResultSet.
	 * https://stackoverflow.com/questions/1870022/java-iterator-backed-by-a-
	 * resultset
	 */
	public void read(String sql) {
		try {
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int cols = rsmd.getColumnCount();
			for (int i = 1; i <= cols; i++) {
				// print Column Names
				System.out.print(rsmd.getColumnLabel(i) + "\t");
			}

			System.out.println("\n---------------------------------------------");

			while (rs.next()) {
				int id = rs.getInt(1);
				String actor = rs.getString(2);
				String product = rs.getString(3);
				System.out.format("%-7d %-15s %-20s %n", id, actor, product);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void shudown() {
		try {
			if (con != null) {
				DriverManager.getConnection(dbURL + ";shutdown=true");
				con.close();
			}
		} catch (SQLException e) {
			System.err.print("Derby connection shutdown.");
		}
	}
}
