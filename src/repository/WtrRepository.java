package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entities.Role;
import entities.User;
import entities.Worktime;

public class WtrRepository {

	private final String URL = "jdbc:mysql://127.0.0.1:3306/wtr";
	private final String USERNAME = "root";
	private final String PASS = "";

	private Connection connection;

	public WtrRepository() {
		initConnection();
	}

	private void initConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			this.connection = DriverManager.getConnection(URL, USERNAME, PASS);
		} catch (SQLException | ClassNotFoundException ex) {
			System.err.println("Database connection failed!");
		}
	}

	public User login(String username, String password) {
		String query = "SELECT u.*,r.id AS role_id, r.name AS role_name FROM user u "
				+ "		INNER JOIN user_role ur ON ur.user_id = u.id " + "		INNER JOIN role r ON r.id = ur.role_id "
				+ "WHERE u.username = ? AND u.password = ?";

		try {
			PreparedStatement prepStmt = connection.prepareStatement(query);
			prepStmt.setString(1, username);
			prepStmt.setString(2, password);
			ResultSet rs = prepStmt.executeQuery();
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setUsername(rs.getString("username"));

				Role role = new Role(rs.getInt("role_id"), rs.getString("role_name"));
				user.addRole(role);
				while (rs.next()) {
					role = new Role(rs.getInt("role_id"), rs.getString("role_name"));
					user.addRole(role);
				}

				return user;
			}
		} catch (SQLException ex) {
			System.err.println("Login error " + ex);
		}
		return null;
	}

	public List<User> findUserByRole(String role) {
		String query = "SELECT u.name,r.name AS role_name FROM user u" + " INNER JOIN user_role ur ON ur.user_id = u.id"
				+ " INNER JOIN role r ON r.id = ur.role_id" + " WHERE r.name = ?";
		List<User> result = new ArrayList<>();
		try {
			PreparedStatement prepStmt = connection.prepareStatement(query);
			prepStmt.setString(1, role);
			ResultSet rs = prepStmt.executeQuery();
			while (rs.next()) {
				User user = new User();
				user.setName(rs.getString("name"));
				result.add(user);

			}
		} catch (SQLException ex) {
			System.err.println("Login error " + ex);
		}

		return result;
	}

	public List<User> findUserByBossId(Integer bossid) {
		String query = "SELECT u.name,u.id FROM user u WHERE u.bossid = ?";
		List<User> result = new ArrayList<>();
		try {
			PreparedStatement prepStmt = connection.prepareStatement(query);
			prepStmt.setInt(1, bossid);
			ResultSet rs = prepStmt.executeQuery();
			while (rs.next()) {
				User user = new User();
				user.setName(rs.getString("name"));
				user.setId(rs.getInt("id"));
				result.add(user);

			}
		} catch (SQLException ex) {
			System.err.println("Login error " + ex);
		}

		return result;
	}

	public List<Worktime> findWorktimeByUserIdAndMonth(int year, int month, int userid) {
		String query = "SELECT `id`,`user_id`, `date`, `wstart`, `lstart`, `lend`, `wend` FROM `worktime` "
				+ "WHERE YEAR(date) = ? AND MONTH(date) = ? AND user_id = ?";
		List<Worktime> result = new ArrayList<>();

		try {
			PreparedStatement prepStmt = connection.prepareStatement(query);
			prepStmt.setInt(1, year);
			prepStmt.setInt(2, month);
			prepStmt.setInt(3, userid);
			ResultSet rs = prepStmt.executeQuery();
			while (rs.next()) {
				Worktime wt = new Worktime();
				wt.setId(rs.getInt("id"));
				java.sql.Date sqlDate = rs.getDate("date");
				wt.setDate(sqlDate.toLocalDate());
				java.sql.Time wstart = rs.getTime("wstart");
				wt.setWstart(wstart.toLocalTime());
				java.sql.Time lstart = rs.getTime("lstart");
				wt.setLstart(lstart.toLocalTime());
				java.sql.Time lend = rs.getTime("lend");
				wt.setLend(lend.toLocalTime());
				java.sql.Time wend = rs.getTime("wend");
				wt.setWend(wend.toLocalTime());
				wt.setUserid(rs.getInt("user_id"));
				result.add(wt);
			}
		} catch (SQLException ex) {
			System.err.println("Error in findWorktimeByUserIdAndMonth() " + ex);
		}

		return result;
	}

	public void addNewWorkTime(Worktime worktime) {
		String query = "INSERT INTO `worktime`( `user_id`, `date`, `wstart`, `lstart`, `lend`, `wend`) " + "VALUES "
				+ "(?,?,?,?,?,?)";
		try {
			PreparedStatement prepStmt = connection.prepareStatement(query);
			prepStmt.setInt(1, worktime.getUserid());
			prepStmt.setDate(2, java.sql.Date.valueOf(worktime.getDate()));
			prepStmt.setTime(3, java.sql.Time.valueOf(worktime.getWstart()));
			prepStmt.setTime(4, java.sql.Time.valueOf(worktime.getLstart()));
			prepStmt.setTime(5, java.sql.Time.valueOf(worktime.getLend()));
			prepStmt.setTime(6, java.sql.Time.valueOf(worktime.getWend()));
			prepStmt.executeUpdate();
		} catch (SQLException ex) {
			System.err.println("Error in addNewWorkTime() " + ex);
		}
	}

	public void updateWorkTime(Worktime worktime) {
		String query = "UPDATE worktime SET wstart = ?, lstart = ?, lend = ?, wend = ? WHERE id = ?";
		try {
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setTime(1, java.sql.Time.valueOf(worktime.getWstart()));
			pstmt.setTime(2, java.sql.Time.valueOf(worktime.getLstart()));
			pstmt.setTime(3, java.sql.Time.valueOf(worktime.getLend()));
			pstmt.setTime(4, java.sql.Time.valueOf(worktime.getWend()));
			pstmt.setInt(5, worktime.getId());
			pstmt.execute();
		} catch (SQLException ex) {
			System.err.println("Error in updateWorkTime()" + ex);
		}
	}

	public void deleteWorkTime(int id) {
		String query = "DELETE FROM worktime WHERE id = ?";
		try {
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, id);
			pstmt.execute();
		} catch (SQLException ex) {
			System.err.println("Error in deleteWorkTime()" + ex);
		}
	}

	public Worktime findWorkTimeById(int id) {
		String query = "SELECT * FROM worktime WHERE id = ?";
		try {
			PreparedStatement prepStmt = connection.prepareStatement(query);
			prepStmt.setInt(1, id);
			ResultSet rs = prepStmt.executeQuery();
			while (rs.next()) {
				Worktime wt = new Worktime();

				wt.setId(rs.getInt("id"));
				wt.setUserid(rs.getInt("user_id"));
				java.sql.Date sqlDate = rs.getDate("date");
				wt.setDate(sqlDate.toLocalDate());
				java.sql.Time wstart = rs.getTime("wstart");
				wt.setWstart(wstart.toLocalTime());
				java.sql.Time lstart = rs.getTime("lstart");
				wt.setLstart(lstart.toLocalTime());
				java.sql.Time lend = rs.getTime("lend");
				wt.setLend(lend.toLocalTime());
				java.sql.Time wend = rs.getTime("wend");
				wt.setWend(wend.toLocalTime());

				return wt;
			}
		} catch (SQLException ex) {
			System.err.println("Error in findWorktimeById() :" + ex);
		}
		return null;
	}
}
