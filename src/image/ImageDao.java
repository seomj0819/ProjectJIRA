package image;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ImageDao {
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:xe";
	String dbId = "jira";
	String dbPw = "1234";
	boolean UploadImage(String image_title, String image_category) {
		boolean isUploaded = false;

		String sql = "INSERT INTO image(image_no, image_title, image_category) VALUES (seq_image_no.nextVal, ?, ?)";

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, image_title);
			pstmt.setString(2, image_category);
			int result = pstmt.executeUpdate();

			if (result > 0) {
				isUploaded = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return isUploaded;
	}

	boolean UpdateProfileImage(int user_no, String new_image_title) {
		boolean isUpdated = false;

		String sql = "UPDATE image SET image_title=? WHERE image_no=(SELECT image_no FROM users WHERE user_no=?)";

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, new_image_title);
			pstmt.setInt(2, user_no);
			int result = pstmt.executeUpdate();

			if (result > 0) {
				isUpdated = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return isUpdated;
	}

	boolean DeleteImageReply(int reply_no, int user_no) {
		boolean isDeleted0 = false;
		boolean isDeleted = false;
		boolean Deleted = false;
		
		String sql0 = "DELETE image_no FROM reply WHERE reply_no = ? AND writer_no = ?";
		String sql = "DELETE FROM image WHERE image_no IN(SELECT image_no FROM reply WHERE reply_no = ? AND writer_no = ?)";

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql0)) {
			pstmt.setInt(1, reply_no);
			pstmt.setInt(2, user_no);
			int result = pstmt.executeUpdate();

			if (result > 0) {
				isDeleted0 = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, reply_no);
			pstmt.setInt(2, user_no);
			int result = pstmt.executeUpdate();

			if (result > 0) {
				isDeleted = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (isDeleted0 && isDeleted) {
			Deleted = true;
		}

		return Deleted;
	}

	boolean DeleteImageTask(String space_key, int task_no, int user_no) {
		boolean isDeleted0 = false;
		boolean isDeleted = false;
		boolean Deleted = false;

		String sql0 = "DELETE image_no FROM task WHERE space_key = ? AND task_no = ? AND creator_no = ?";
		String sql = "DELETE FROM image WHERE image_no IN(SELECT image_no FROM task WHERE space_key = ? AND task_no = ? AND creator_no =?)";

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql0)) {
			pstmt.setString(1, space_key);
			pstmt.setInt(2, task_no);
			pstmt.setInt(3, user_no);
			int result = pstmt.executeUpdate();

			if (result > 0) {
				isDeleted0 = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, space_key);
			pstmt.setInt(2, task_no);
			pstmt.setInt(3, user_no);
			int result = pstmt.executeUpdate();

			if (result > 0) {
				isDeleted = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (isDeleted0 && isDeleted) {
			Deleted = true;
		}

		return Deleted;
	}

	boolean DeleteImageProfile(int quit_user_no) {
		boolean isDeleted = false;

		String sql = "DELETE FROM image WHERE image_no IN(SELECT iamge_no FROM users WHERE user_no = ?)";

		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, quit_user_no);
			int result = pstmt.executeUpdate();

			if (result > 0) {
				isDeleted = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return isDeleted;
	}

	public static void main(String[] args) {
		ImageDao dao = new ImageDao();
		
		//12.1 Upload Image(Clear!)
		System.out.println(dao.UploadImage("new_image.jpg", "task"));
		//12.2 Update Profile Image(Clear!)
		System.out.println(dao.UpdateProfileImage(1, "newnew_image.jpg"));
		//12.3 Delete Image(reply)(Clear!)
		System.out.println(dao.DeleteImageReply(1, 1));
		//12.4 Delete Image(task)(Clear!)
		System.out.println(dao.DeleteImageTask("ABCD", 1, 1));
		//12.5 Delete Image(Profile)
		System.out.println(dao.DeleteImageProfile(1));
	}
}
