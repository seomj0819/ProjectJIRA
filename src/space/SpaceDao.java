package space;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SpaceDao {
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:xe";
	String dbId = "jira";
	String dbPw = "1234";
	
	// 명세 2.1
	// input : space_key, space_title, space_status, image_no
	// output : -
	// space_key, space_title : 유저입력
	// space_status : 공개(Y)/비공개(N)
	// image_no : default 디폴트 스페이스 이미지 번호(1)
	boolean CreateSpace(String space_key, String space_title, String space_status) {
		boolean isCreated = false;

		String sql = "INSERT INTO space(space_key, space_title, space_order, space_status, image_no) VALUES (?, ?, seq_space_order.nextVal, ?, 1)";

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, space_key);
			pstmt.setString(2, space_title);
			pstmt.setString(3, space_status);
			int result = pstmt.executeUpdate();

			if (result > 0) {
				isCreated = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return isCreated;
	}

	// 명세 2.2
	// input : space_key, user_no
	// output : -
	// space_key : 지우려는 스페이스키
	// user_no : 현재 유저번호
	// user_role = '관리자' 인 경우만 삭제가능
	boolean DeleteSpace(String space_key, int user_no) {
		boolean isDeleted0 = false;
		boolean isDeleted = false;

		String sql0 = "DELETE FROM space_members " + "WHERE space_key = ? " + "AND space_key IN ( "
				+ "SELECT space_key FROM space_members " + "WHERE user_role = '관리자' " + "AND user_no = ? " + ")";
		String sql = "DELETE FROM space " + "WHERE space_key = ? ";

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt0 = conn.prepareStatement(sql0);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt0.setString(1, space_key);
			pstmt0.setInt(2, user_no);
			int result0 = pstmt0.executeUpdate();

			if (result0 > 0) {
				isDeleted0 = true;
			}

			pstmt.setString(1, space_key);
			int result = pstmt.executeUpdate();

			if (result > 0 && isDeleted0) {
				isDeleted = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return isDeleted;
	}

	// 명세 2.3
	// input : space_key, new_space_title, new_space_key, user_no
	// output : -
	// space_key : 수정하려는 스페이스키
	// new_space_key : 새로운 스페이스키
	// new_space_title : 새로운 스페이스타이틀
	// user_no : 현재 유저번호
	// user_role = '관리자' 인 경우만 수정가능
	// 테이블에 트리거 적용
	boolean UpdateSpace(String space_key, String new_space_title, String new_space_key, int user_no) {
		boolean isUpdated = false;

		String sql = "UPDATE space SET space_title = ?, space_key = ? " + "WHERE space_key IN (" + "SELECT space_key "
				+ "FROM space_members " + "WHERE user_role = '관리자' " + "AND user_no = ?) " + "AND space_key = ?";

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, new_space_title);
			pstmt.setString(2, new_space_key);
			pstmt.setInt(3, user_no);
			pstmt.setString(4, space_key);
			int result = pstmt.executeUpdate();

			if (result > 0) {
				isUpdated = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return isUpdated;
	}

	// 명세 2.4
	// input : user_no
	// output : List<SpaceListDto>
	// user_no : 현재 유저번호
	List<SpaceListDto> ShowSpaceList(int user_no) {
		List<SpaceListDto> list = new ArrayList<>();

		String sql = "SELECT sm.space_key, space_title, image_no FROM space_members sm INNER JOIN space s ON sm.space_key = s.space_key WHERE sm.user_no = ? ORDER BY space_order";

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, user_no);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					SpaceListDto dto = new SpaceListDto();
					dto.setSpaceKey(rs.getString("space_key"));
					dto.setSpaceTitle(rs.getString("space_title"));
					dto.setImageNo(rs.getInt("image_no"));

					list.add(dto);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	// 명세 2.5
	// input : space_key
	// output : SpaceListDto(space_key, space_title, image_no)
	// space_key : 현재 스페이스키
	SpaceListDto ShowSpaceProfile(String space_key) {
		SpaceListDto dto = null;

		String sql = "SELECT space_key, space_title, image_no FROM space WHERE space_key = ?";

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, space_key);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				dto = new SpaceListDto();
				dto.setSpaceKey(rs.getString("space_key"));
				dto.setSpaceTitle(rs.getString("space_title"));
				dto.setImageNo(rs.getInt("image_no"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dto;
	}

	public static void main(String[] args) throws Exception {
		SpaceDao dao = new SpaceDao();
		
		// 2.1 Create Space(clear!) 
		// System.out.println(dao.CreateSpace("ABCD", "Project1", "Y"));
		// 2.2 Delete Space(clear!)
		// System.out.println(dao.DeleteSpace("ABCD", 1));
		// 2.3 Update Space(clear!)
		// System.out.println(dao.UpdateSpace("ABCD", "Newproj1", "EFGH", 1));
		// 2.4 Show Space List(clear!)
		// System.out.println(dao.ShowSpaceList(1));
		// 2.5 Show Space Profile(clear!)
		// System.out.println(dao.ShowSpaceProfile("ABCD"));
	}
}
