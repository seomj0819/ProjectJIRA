package reply;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReplyDao {
	// 명세 5.1
	// input : space_key, task_no, writer_no, reply_content, image_no
	// output : -
	// reply_content, image_no : 유저입력
	// created_at : SYSDATE
	// space_key, task_no, writer_no, task_id : current값
	boolean WriteReply(String space_key, int task_no, int writer_no, String reply_content, int image_no) {
		boolean isWrited = false;
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "jira";
		String dbPw = "1234";

		String sql = "INSERT INTO reply(reply_no, space_key, task_no, writer_no, reply_content, image_no, created_at, task_id) VALUES (seq_reply_no.nextVal, ?, ?, ?, ?, ?, SYSDATE, ?)";

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, space_key);
			pstmt.setInt(2, task_no);
			pstmt.setInt(3, writer_no);
			pstmt.setString(4, reply_content);
			pstmt.setInt(5, image_no);
			pstmt.setString(6, space_key + "-" + task_no);
			int result = pstmt.executeUpdate();

			if (result > 0) {
				isWrited = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return isWrited;
	}

	// 명세 5.2
	// input : reply_no, writer_no, new_reply_content, new_image_no
	// output : -
	// reply_no : 수정하려는 댓글 번호
	// writer_no : 작성자의 유저번호, 작성자만 수정 가능
	// new_reply_content : 새로운 댓글내용
	// new_image_no : 새로운 이미지 번호
	boolean UpdateReply(int reply_no, int writer_no, String new_reply_content, int new_image_no) {
		boolean isUpdated = false;
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "jira";
		String dbPw = "1234";

		String sql = "UPDATE reply SET reply_content = ?, image_no = ? " + "WHERE writer_no = ?" + "AND reply_no = ?) ";

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, new_reply_content);
			pstmt.setInt(2, new_image_no);
			pstmt.setInt(3, writer_no);
			pstmt.setInt(4, reply_no);
			int result = pstmt.executeUpdate();

			if (result > 0) {
				isUpdated = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return isUpdated;
	}

	// 명세 5.3
	// input : reply_no, writer_no
	// output : -
	// reply_no : 지우려는 댓글 번호
	// writer_no : 작성자의 유저번호, 작성자만 삭제가능
	boolean DeleteReply(int reply_no, int writer_no) {
		boolean isDeleted = false;
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "jira";
		String dbPw = "1234";

		String sql = "DELETE FROM reply " + "WHERE reply_no = ? " + "AND writer_no = ?";

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, reply_no);
			pstmt.setInt(2, writer_no);
			int result = pstmt.executeUpdate();

			if (result > 0) {
				isDeleted = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return isDeleted;
	}

	// 명세 5.4
	// input : space_key, task_no
	// output : List<ReplyListDto>
	// space_key, task_no : 현재 스페이스, 테스크
	List<ReplyListDto> ShowReplyList(String space_key, int task_no) {
		List<ReplyListDto> list = new ArrayList<>();
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "jira";
		String dbPw = "1234";

		String sql = "SELECT reply_no, space_key, task_no, writer_no, reply_content, image_no, created_at, task_id "
				+ "FROM reply WHERE space_key = ? AND task_no = ? " + "ORDER BY created_at DESC";

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {

			pstmt.setString(1, space_key);
			pstmt.setInt(2, task_no);

			while (rs.next()) {
				ReplyListDto dto = new ReplyListDto();
				dto.setReplyNo(rs.getInt("reply_no"));
				dto.setWriterNo(rs.getInt("writer_no"));
				dto.setReplyContent(rs.getString("reply_content"));
				dto.setImageNo(rs.getInt("image_no"));
				dto.setCreatedAt(rs.getString("created_at"));

				list.add(dto);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	public static void main(String[] args) {

	}
}
