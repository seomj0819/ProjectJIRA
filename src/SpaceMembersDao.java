import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SpaceMembersDao {
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:xe";
	String dbId = "test0424";
	String dbPw = "12345";
	
	//명세 3.5
	// input : user_no, space_key
	// output : stored_image_file_name, user_no, user_name, email 
	// stored_image_file_name = 저장된 이미지의 제목
	// 접속중인 스페이스의 모든 유저 출력
	List<UserProfileDto> getSpaceMemberList (String currentSpaceKey) throws Exception{
		List<UserProfileDto> memberList = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		Class.forName(driver);
		conn = DriverManager.getConnection(url, dbId, dbPw);
		
		String sql = " SELECT i.image_title, u.user_no, u.user_name, u.email "
				+ "FROM space_members s "
				+ "JOIN users u "
				+ "ON s.user_no = u.user_no "
				+ "LEFT OUTER JOIN image i "
				+ "ON u.image_no = i.image_no "
				+ "WHERE s.space_key = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, currentSpaceKey);
		rs = pstmt.executeQuery();
		
		while (rs.next()) {
			UserProfileDto dto = new UserProfileDto();
			dto.setUserNo(rs.getInt("user_no"));
			dto.setUserName(rs.getString("user_name"));
			dto.setEmail(rs.getString("email"));
			dto.setImageTitle(rs.getString("image_title"));
			memberList.add(dto);
		}
		
		rs.close();
		pstmt.close();
		conn.close();
		
		return memberList;
	}
	public static void main(String[] args) {

	}
}

