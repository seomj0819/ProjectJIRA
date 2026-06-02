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

	// 명세 3.1
	// input : space_key, user_no, user_role
	// output : -
	// 초대코드가 발송되었으면 true, 아니면 false
	// 초대된 유저가 접속코드 인증시 초대완료 (Verification Code, Expire Date -> null)
	boolean isSpaceMemberDuplicate (String spaceKey, String email, int currentUserNo) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		Class.forName(driver);
		conn = DriverManager.getConnection(url, dbId, dbPw);
		
		String sql = "SELECT * FROM space_members WHERE space_key = ? AND user_no = ?";
		
		return false;
	}
	
	void createInviteCode(String spaceKey, int userNo, String userRole, int currentUserNo) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;

		Class.forName(driver);
		conn = DriverManager.getConnection(url, dbId, dbPw);

		// Only Space Member Can Invite
		String sql = "SELECT * FROM space_members WHERE user_no = ? AND space_key = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, currentUserNo);
		pstmt.setString(2, spaceKey);
		rs = pstmt.executeQuery();

		
		
		if (rs.next()) {
			// Create Invite Code
			String inviteCode = RandomCodeUtil.generateRandomCode();

			sql = " INSERT INTO space_members(space_key, user_no, user_role, invite_code, expire_date) "
					+ "VALUES (?, ?, ?, ?, SYSDATE + (1/24/60 * 10))";
			pstmt2 = conn.prepareStatement(sql);
			pstmt2.setString(1, spaceKey);
			pstmt2.setInt(2, userNo);
			pstmt2.setString(3, userRole);
			pstmt2.setString(4, inviteCode);
			pstmt2.executeUpdate();

		}

		rs.close();
		pstmt2.close();
		pstmt.close();
		conn.close();

	}

	// Check Invite Code
	// input : inviteCode, userNo
	// output : boolean
	// userNo : invited user
	boolean checkInviteCode(String inviteCode, String spaceKey, int userNo) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		boolean check = false;

		Class.forName(driver);
		conn = DriverManager.getConnection(url, dbId, dbPw);

		// Check Invite Code
		String sql = "SELECT * " + "FROM space_members " + "WHERE invite_code = ? "
				+ "AND expire_date > SYSDATE AND user_no = ? AND space_key = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, inviteCode);
		pstmt.setInt(2, userNo);
		pstmt.setString(3, spaceKey);
		rs = pstmt.executeQuery();

		if (rs.next()) {
			check = true;
			sql = "UPDATE space_members " + "SET invite_code = null, expire_date = null " + "WHERE user_no = ? "
					+ "AND space_key = ?";
			pstmt2 = conn.prepareStatement(sql);
			pstmt2.setInt(1, userNo);
			pstmt2.setString(2, spaceKey);
			pstmt2.executeUpdate();
		} else {
			sql = "DELETE FROM space_members " + "WHERE user_no = ? AND space_key = ?";
			pstmt2 = conn.prepareStatement(sql);
			pstmt2.setInt(1, userNo);
			pstmt2.setString(2, spaceKey);
			pstmt2.executeUpdate();
			pstmt2.close();
		}

		rs.close();
		pstmt.close();
		conn.close();

		return check;
	}

	// 명세 3.2
	// input : space_key, user_no
	// output : user_role
	String searchUserRole(String spaceKey, int userNo) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String userRole = null;

		Class.forName(driver);
		conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "SELECT user_role " + "FROM space_members " + "WHERE space_key = ? AND user_no = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, spaceKey);
		pstmt.setInt(2, userNo);
		rs = pstmt.executeQuery();

		if (rs.next()) {
			userRole = rs.getString("user_role");
		}

		rs.close();
		pstmt.close();
		conn.close();

		return userRole;
	}

	// 명세 3.3
	// input : space_key, user_no
	// output : boolean
	boolean deleteSpaceMember(String spaceKey, int userNo) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean deleted = false;

		Class.forName(driver);
		conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "UPDATE task SET worker_no = null WHERE worker_no = ? AND space_key = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, userNo);
		pstmt.setString(2, spaceKey);
		pstmt.executeQuery();

		pstmt.close();
		
		sql = "UPDATE reply SET writer_no = null WHERE writer_no = ? AND space_key = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, userNo);
		pstmt.setString(2, spaceKey);
		pstmt.executeQuery();

		pstmt.close();
		
		sql = "DELETE FROM space_members " + "WHERE user_no = ? AND space_key = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, userNo);
		pstmt.setString(2, spaceKey);
		pstmt.executeQuery();
		
		pstmt.close();
		conn.close();

		return true;
	}

	// 멍세 3.4
	// input : current_user_no
	// output : user_no, user_name, image_no
	// user_no : current_user_no를 제외한 나머지 user_no
	// 접속중인 유저와 같은 스페이스에 있는 모든 유저 출력
	List<UserProfileDto> getAllSpaceMembers(int currentUserNo) throws Exception{
		List<UserProfileDto> userList = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		Class.forName(driver);
		conn = DriverManager.getConnection(url, dbId, dbPw);
		
		String sql = "SELECT "
				+ "    u.user_no, "
				+ "    u.user_name, "
				+ "    u.email, "
				+ "    i.image_title "
				+ "FROM users u "
				+ "LEFT OUTER JOIN image i "
				+ "	   ON u.image_no = i.image_no "
				+ "WHERE u.user_no IN ( "
				+ "    SELECT DISTINCT space.user_no "
				+ "    FROM space_members space "
				+ "    WHERE space.space_key IN ( "
				+ "        SELECT my_space.space_key "
				+ "        FROM space_members my_space "
				+ "        WHERE my_space.user_no = ? "
				+ "    ) "
				+ "    AND space.user_no != ? "
				+ ")";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, currentUserNo);
		pstmt.setInt(2, currentUserNo);
		rs = pstmt.executeQuery();
		
		while(rs.next()) {
			UserProfileDto dto = new UserProfileDto();
			dto.setUserNo(rs.getInt("user_no"));
			dto.setUserName(rs.getString("user_name"));
			dto.setEmail(rs.getString("email"));
			dto.setImageTitle(rs.getString("image_title"));
			userList.add(dto);
		}
		
		rs.close();
		pstmt.close();
		conn.close();
		
		return userList;
	}
	
	// 명세 3.5
	// input : user_no, space_key
	// output : stored_image_file_name, user_no, user_name, email
	// stored_image_file_name : 저장된 이미지의 제목
	// 접속중인 스페이스의 모든 유저 출력
	List<UserProfileDto> getSpaceMemberList(String currentSpaceKey) throws Exception {
		List<UserProfileDto> memberList = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		Class.forName(driver);
		conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = " SELECT i.image_title, u.user_no, u.user_name, u.email " + "FROM space_members s "
				+ "JOIN users u " + "ON s.user_no = u.user_no " + "LEFT OUTER JOIN image i "
				+ "ON u.image_no = i.image_no " + "WHERE s.space_key = ?";
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

	public static void main(String[] args) throws Exception {
		SpaceMembersDao dao = new SpaceMembersDao();
		// 3.1 Invite Member (clear!)
//		dao.createInviteCode("ABCD", 5, "구성원", 2);
//		System.out.println(dao.checkInviteCode("KfJCEW", "ABCD", 5));
		
		// 3.2 Search User Role (clear!)
//		System.out.println(dao.searchUserRole("ABCD", 1));
		
		// 3.3 Delete Space Member (clear!)
//		System.out.println(dao.deleteSpaceMember("ABCD", 5));
		
		// 3.4 Get All Space Members (clear!)
//		List<UserProfileDto> spaceMemberList = dao.getAllSpaceMembers(2);
//		if(spaceMemberList.isEmpty()) {
//			System.out.println("No One Here...");
//		}
//		else {
//			for (UserProfileDto dto : spaceMemberList) {
//				int userNo = dto.getUserNo();
//				String userName = dto.getUserName();
//				String userEmail = dto.getEmail();
//				String userImage = dto.getImageTitle();
//				System.out.println(userNo + ", " + userName + ", " + userEmail + ", " + userImage);
//			}
//		}
		
		// 3.5 Get Space Member Profile List (clear!)
//		List<UserProfileDto> allSpaceMembers = dao.getSpaceMemberList("ABCD");
//		if(allSpaceMembers.isEmpty()) {
//			System.out.println("No One Here...");
//		}
//		else {
//			for(UserProfileDto dto : allSpaceMembers) {
//				int userNo = dto.getUserNo();
//				String userName = dto.getUserName();
//				String userEmail = dto.getEmail();
//				String userImage = dto.getImageTitle();
//				System.out.println(userNo + ", " + userName + ", " + userEmail + ", " + userImage);
//			}
//		}
	}
}
