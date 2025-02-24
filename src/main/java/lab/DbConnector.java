package lab;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DbConnector {

	private static final String JDBC_CONECTIN_STRING = "jdbc:h2:file:./scoreDB";

	public List<Score> getAll() {
		return queryScore("select * from scores;");
	}

	public List<Score> getFirstTen() {
		return queryScore("select * from scores order by points  desc limit 10;");
	}

	private List<Score> queryScore(String query) {
		List<Score> result = new ArrayList<>();
		try (Connection con = DriverManager.getConnection(JDBC_CONECTIN_STRING);
				Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(query);) {
			while (rs.next()) {
				result.add(new Score(rs.getString("nick"), rs.getInt("points")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public void init() {
		try (Connection con = DriverManager.getConnection(JDBC_CONECTIN_STRING);
				Statement stm = con.createStatement();) {
			stm.executeUpdate("CREATE TABLE if not exists scores (nick VARCHAR(50) NOT NULL, points INT NOT NULL);");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertScore(Score score) {
		try (Connection con = DriverManager.getConnection(JDBC_CONECTIN_STRING);
				PreparedStatement stm = con.prepareStatement("INSERT INTO scores VALUES (?, ?)");) {
			stm.setString(1, score.getName());
			stm.setInt(2, score.getPoints());
			stm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
