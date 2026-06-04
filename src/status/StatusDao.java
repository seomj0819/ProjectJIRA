package status;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import space.SpaceListDto;

public class StatusDao {
	boolean CreateStatus(String space_key, int status_order, String status_title, String status_color) {
		boolean isCreated = false;
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "jira";
		String dbPw = "1234";

		String sql = "INSERT INTO status(status_no, space_key, status_order, status_title, status_color) VALUES (seq_status_no.nextVal, ?, ?, ?, ?)";

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, space_key);
			pstmt.setInt(2, status_order);
			pstmt.setString(3, status_title);
			pstmt.setString(4, status_color);
			int result = pstmt.executeUpdate();

			if (result > 0) {
				isCreated = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return isCreated;
	}

	boolean UpdateStatus(int status_no, String new_status_title, String new_status_color) {
		boolean isUpdated = false;
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "jira";
		String dbPw = "1234";

		String sql = "UPDATE status SET status_title = ?, status_color = ? " + "WHERE status_no = ? ";

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, new_status_title);
			pstmt.setString(2, new_status_color);
			pstmt.setInt(3, status_no);
			int result = pstmt.executeUpdate();

			if (result > 0) {
				isUpdated = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return isUpdated;
	}

	boolean ChangeOrder(int status_no, int status_order, int new_status_order) {
		boolean isChangedUp = false;
		boolean isChangedDown = false;
		boolean isChanged = false;
		boolean OrderChanged = false;
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "jira";
		String dbPw = "1234";

		String sql = "UPDATE status SET status_order = ? " + "WHERE status_no = ? ";
		String sqlUp = "UPDATE status SET status_order = status_order + 1 "
				+ "WHERE status_order >= ? AND status_order < ?";
		String sqlDown = "UPDATE status SET status_order = status_order - 1 "
				+ "WHERE status_order <= ? AND status_order > ?";
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sqlUp)) {

			pstmt.setInt(1, new_status_order);
			pstmt.setInt(2, status_order);
			int result = pstmt.executeUpdate();

			if (result > 0) {
				isChangedUp = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sqlDown)) {

			pstmt.setInt(1, new_status_order);
			pstmt.setInt(2, status_order);
			int result = pstmt.executeUpdate();

			if (result > 0) {
				isChangedDown = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, new_status_order);
			pstmt.setInt(2, status_no);
			int result = pstmt.executeUpdate();

			if (result > 0) {
				isChanged = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (isChangedUp && isChanged || isChangedDown && isChanged) {
			OrderChanged = true;
		}
		return OrderChanged;
	}

	boolean DeleteStatus(int status_no, int new_status_no) {
		boolean TaskUpdated = false;
		boolean OrderUpdated = false;
		boolean StatusDeleted = false;
		boolean isDeleted = false;
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "jira";
		String dbPw = "1234";

		String sql = "UPDATE task SET status = ? WHERE status = ?)";
		String sql1 = "UPDATE status SET status_order = status_order - 1 WHERE status_order > (SELECT status_order FROM status WHERE status_no = ?)";
		String sql2 = "DELETE FROM status WHERE status_no = ?";

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, new_status_no);
			pstmt.setInt(2, status_no);
			int result = pstmt.executeUpdate();

			if (result > 0) {
				TaskUpdated = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql1)) {

			pstmt.setInt(1, status_no);
			int result = pstmt.executeUpdate();

			if (result > 0) {
				OrderUpdated = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql2)) {

			pstmt.setInt(1, status_no);
			int result = pstmt.executeUpdate();

			if (result > 0) {
				StatusDeleted = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (TaskUpdated && OrderUpdated && StatusDeleted) {
			isDeleted = true;
		}
		return isDeleted;
	}
		// 명세 13.5
		// input : space_key
		// output : StatusDto(status_no, status_title, status_color, color_code), totalCount
		// space_key : 현재 스페이스키
	List<StatusDto> ShowStatus(String space_key) {
		List<StatusDto> list = new ArrayList<>();
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "jira";
		String dbPw = "1234";

		String sql = "SELECT " + "s.status_no, " + "s.status_title, " + "s.status_color, " + "c.color_code " + "FROM status s " + "INNER JOIN color c "
				+ "ON s.status_color = c.color_name " + "WHERE s.space_key = ? " + "ORDER BY s.status_order ASC";

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, space_key);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					StatusDto dto = new StatusDto();
					dto.setStatusNo(rs.getInt("status_no"));
					dto.setStatusTitle(rs.getString("status_title"));
					dto.setStatusColor(rs.getString("status_color"));
					dto.setColorCode(rs.getString("color_code"));

					list.add(dto);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	public static void main(String[] args) {
		StatusDao dao = new StatusDao();
		
		//13.5 ShowStatus(Clear!)
		List<StatusDto> list = dao.ShowStatus("ABCD");
		System.out.println(list);
		int totalCount = list.size();
		System.out.println(totalCount);
		
	}
}
