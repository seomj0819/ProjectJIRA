package files;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FilesDao {
	//명세 11.1
	// input : space_key, task_no, reply_no, file_name, file_path
	// output : -
	//file_name, file_path : 유저입력
	// created_at : SYSDATE
	// space_key, task_no, reply_no : current값
	boolean UploadFile(String space_key, int task_no, int reply_no, String file_name, String file_path) {
		boolean isUploaded = false;
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "jira";
		String dbPw = "1234";

		String sql = "INSERT INTO files(file_no, space_key, task_no, reply_no, file_name, file_path, created_at) VALUES (seq_file_no.nextVal, ?, ?, ?, ?, ?, sysdate)";

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
			pstmt.setString(4, file_name);
			pstmt.setString(5, file_path);
			int result = pstmt.executeUpdate();

			if (result > 0) {
				isUploaded = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return isUploaded;
	}

	public static void main(String[] args) {
		FilesDao dao = new FilesDao();
		//11.1 Upload File(Clear!)
		System.out.println(dao.UploadFile("ABCD", 1, 1, "file1", "filefjie?"));
	}
}
