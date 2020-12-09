package com.github.koryu25.emf.eminefishing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;


public class DBManager {

	//インスタンスフィールド
	private Connection connection;
	private String host;
	private String database;
	private String username;
	private String password;
	private int port;
	private String tablename;
	//コンストラクタ
	public DBManager(String host, String database, String username, String password, int port, String tablename) {
		this.host = host;
		this.database = database;
		this.username = username;
		this.password = password;
		this.port = port;
		this.tablename = tablename;
	}
	public Boolean connectionTest() {
		try {
			openConnection();
			return true;
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	private void openConnection() throws SQLException, ClassNotFoundException {
		if (connection != null && !connection.isClosed()) return;
			synchronized (this) {
		if (connection != null && !connection.isClosed())return;
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
		}
	}
	public void newPlayer(Player player) {
		try {
			openConnection();
			String queryString = "INSERT INTO "+tablename+"(uuid, name) VALUES (?, ?)";
			PreparedStatement ps = connection.prepareStatement(queryString);
			ps.setString(1, player.getUniqueId().toString());
			ps.setString(2, player.getName());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean confirmPlayer(Player player) {
		try {
			openConnection();
			String queryString = "SELECT * FROM "+tablename+" WHERE uuid = ?";
			PreparedStatement ps = connection.prepareStatement(queryString);
			ps.setString(1, player.getUniqueId().toString());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public void setPoint(Player player, int point) {
		try {
			openConnection();
			String queryString = "UPDATE "+tablename+" SET point = ? WHERE uuid = ?";
			PreparedStatement ps = connection.prepareStatement(queryString);
			ps.setInt(1, point);
			ps.setString(2, player.getUniqueId().toString());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public int getPoint(Player player) {
		try {
			openConnection();
			String  queryString = "SELECT * FROM "+tablename+" WHERE uuid = ?";
			PreparedStatement ps = connection.prepareStatement(queryString);
			ps.setString(1, player.getUniqueId().toString());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt("point");
			}
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

}