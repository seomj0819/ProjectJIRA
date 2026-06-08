package alarnchk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AlarmChkDao {
	//명세 10.1
	// input : history_no, user_no
	// output : -
	boolean CreateAlarmCheck(int history_no, int user_no) {
		boolean isCreated = false;
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "jira";
		String dbPw = "1234";

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		String sql = "INSERT INTO alarmchk(history_no, user_no) VALUES (?, ?)";

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, history_no);
			pstmt.setInt(2, user_no);
			int result = pstmt.executeUpdate();

			if (result > 0) {
				isCreated = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return isCreated;
	}
	//명세 10.2
	// input : history_no, user_no
	// output : -
	boolean DeleteAlarmCheck(int history_no, int user_no) {
		boolean isDeleted = false;
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "jira";
		String dbPw = "1234";

		String sql = "DELETE FROM alarmchk " + "WHERE history_no = ? " + "AND user_no = ?";

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, history_no);
			pstmt.setInt(2, user_no);
			int result = pstmt.executeUpdate();

			if (result > 0) {
				isDeleted = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return isDeleted;
	}
	//명세 10.3
	// input : user_no
	// output : AlarmChkDto
	AlarmChkDto CountAlarmCheck(int user_no) {
		AlarmChkDto dto = null;
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "jira";
		String dbPw = "1234";

		String sql = "SELECT COUNT(*) AS cnt FROM alarmchk WHERE user_no = ?";

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, user_no);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				dto = new AlarmChkDto();
				dto.setCount(rs.getInt("cnt"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dto;
	}
	public static void main(String[] args) {
		AlarmChkDao dao = new AlarmChkDao();
		//10.1 Create Alarm Check(Clear!)
		System.out.println(dao.CreateAlarmCheck(1, 1));
		//10.2 Delete Alarm Check(Clear!)
		System.out.println(dao.DeleteAlarmCheck(1, 1));
		//10.3
		System.out.println(dao.CountAlarmCheck(1));
	}
}
