package history;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import reply.ReplyListDto;

public class HistoryDao {
	boolean CreateHistory(String space_key, int task_no, int reply_no, int user_no, String field_name, String action_type, String old_value, String new_value) {
		boolean isCreated = false;
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "jira";
		String dbPw = "1234";

		String sql = "INSERT INTO history(history_no, space_key, task_no, reply_no, user_no, field_name, action_type, created_at, old_value, new_value) VALUES (seq_history_no.nextVal, ?, ?, ?, ?, ?, ?, SYSDATE, ?, ?)";

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, space_key);
			pstmt.setInt(2, task_no);
			pstmt.setInt(3, reply_no);
			pstmt.setInt(4, user_no);
			pstmt.setString(5, field_name);
			pstmt.setString(6, action_type);
			pstmt.setString(7, old_value);
			pstmt.setString(8, new_value);
			int result = pstmt.executeUpdate();

			if (result > 0) {
				isCreated = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return isCreated;
	}
	
	List<HistoryDto> ShowHistory(int user_no) {
		List<HistoryDto> list = new ArrayList<>();
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "jira";
		String dbPw = "1234";

		String sql = "SELECT history_no, space_key, task_no, reply_no, user_no, field_name, action_type, created_at, old_value, new_value "
				+ "FROM history WHERE MONTHS_BETWEEN "
				+ "( sysdate, created_at ) <= 1 AND space_key "
				+ "IN(SELECT space_key "
				+ "FROM space_member "
				+ "WHERE user_no = ?) "
				+ "ORDER BY created_at DESC";

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, user_no);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					HistoryDto dto = new HistoryDto();
					dto.setHistory_no(rs.getInt("history_no"));
					dto.setSpace_key(rs.getNString("space_key"));
					dto.setTask_no(rs.getInt("task_no"));
					dto.setReply_no(rs.getInt("reply_no"));
					dto.setUser_no(rs.getInt("user_no"));
					dto.setField_name(rs.getString("field_name"));
					dto.setAction_type(rs.getString("action_type"));
					dto.setCreated_at(rs.getString("created_at"));
					dto.setOld_value(rs.getString("old_value"));
					dto.setNew_value(rs.getString("new_value"));

					list.add(dto);
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}
	
	List<HistoryDto> ShowTaskHistory(String space_key, int task_no) {
		List<HistoryDto> list = new ArrayList<>();
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "jira";
		String dbPw = "1234";

		String sql = "SELECT history_no, reply_no, user_no, \r\n"
				+ "FROM history \r\n"
				+ "WHERE space_key = current_space_key \r\n"
				+ "AND task_no = current_task_no\r\n"
				+ "ORDER BY history_no;\r\n"
				+ "";

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, user_no);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					HistoryDto dto = new HistoryDto();
					dto.setHistory_no(rs.getInt("history_no"));
					dto.setSpace_key(rs.getNString("space_key"));
					dto.setTask_no(rs.getInt("task_no"));
					dto.setReply_no(rs.getInt("reply_no"));
					dto.setUser_no(rs.getInt("user_no"));
					dto.setField_name(rs.getString("field_name"));
					dto.setAction_type(rs.getString("action_type"));
					dto.setCreated_at(rs.getString("created_at"));
					dto.setOld_value(rs.getString("old_value"));
					dto.setNew_value(rs.getString("new_value"));

					list.add(dto);
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public static void main(String[] args) {

	}
}
