package space;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SpaceDao {

	boolean CreateSpace(String space_key, String space_title, int space_order, String space_status, int image_no)
			throws Exception {
		boolean isCreated = false;
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "proj1";
		String dbPw = "1234";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "INSERT INTO space(space_key, space_title, space_order, space_status,  image_no) VALUES (?, ?, seq_space_order.nextVal, ?, default_image)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, space_key);
		pstmt.setString(2, space_title);
		pstmt.setString(3, space_status);
		int result = pstmt.executeUpdate();

		if (result > 0) {
			isCreated = true;
		}

		pstmt.close();
		conn.close();

		return isCreated;
	}

	boolean DeleteSpace(String space_key, int user_no) throws Exception { // user_no = current user_no
		boolean isDeleted = false;
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "proj1";
		String dbPw = "1234";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "DELETE FROM space WHERE space_key = ? AND space_key IN (SELECT space_key FROM space_members WHERE user_role = '관리자' AND user_no = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, space_key);
		pstmt.setInt(2, user_no);
		int result = pstmt.executeUpdate();

		if (result > 0) {
			isDeleted = true;
		}

		pstmt.close();
		conn.close();

		return isDeleted;
	}
	
	boolean UpdateSpace(String new_space_title, String new_space_key, int user_no) throws Exception {
		boolean isUpdated = false;
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "proj1";
		String dbPw = "1234";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "UPDATE space SET space_title = ?, space_key = ? WHERE space_key IN (SELECT space_key FROM space_members WHERE user_role = '관리자' AND user_no = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, new_space_title);
		pstmt.setString(2, new_space_key);
		pstmt.setInt(3, user_no);
		int result = pstmt.executeUpdate();

		if (result > 0) {
			isUpdated = true;
		}

		pstmt.close();
		conn.close();

		return isUpdated;
	}
	
	SpaceListDto ShowSpaceList(int user_no) throws Exception {
		SpaceListDto dto = null;
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "proj1";
		String dbPw = "1234";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "SELECT sm.space_key, space_title, image_no FROM space_member sm INNER JOIN space s ON sm.space_key = s.space_key WHERE sm.user_no = ? ORDER BY space_order";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, user_no);
		
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()) {
			dto = new SpaceListDto();
			dto.setSpaceKey(rs.getString("sm.space_key"));
			dto.setSpaceTitle(rs.getString("space_title"));
			dto.setImageNo(rs.getInt("image_no"));
		}
		rs.close();
		pstmt.close();
		conn.close();
		
		return dto;
	}
	
	SpaceListDto ShowSpaceProfile(String space_key) throws Exception {
		SpaceListDto dto = null;
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "proj1";
		String dbPw = "1234";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "SELECT space_key, space_title, image_no FROM space WHERE space_key = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, space_key);
		
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()) {
			dto = new SpaceListDto();
			dto.setSpaceKey(rs.getString("space_key"));
			dto.setSpaceTitle(rs.getString("space_title"));
			dto.setImageNo(rs.getInt("image_no"));
		}
		rs.close();
		pstmt.close();
		conn.close();
		
		return dto;
	}
	

	public static void main(String[] args) {

	}
}
