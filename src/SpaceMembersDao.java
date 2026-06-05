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
		boolean isDuplicate = false;
		String sql = "SELECT * FROM space_members WHERE space_key = ? AND user_no = ?";
		
		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, spaceKey);
			pstmt.setInt(2, currentUserNo);

			try (ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					isDuplicate = true;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return isDuplicate;
	}
	
	void createInviteCode(String spaceKey, int userNo, String userRole, int currentUserNo) throws Exception {
	    String checkSql = "SELECT 1 FROM space_members WHERE user_no = ? AND space_key = ?";
	    String insertSql = "INSERT INTO space_members(space_key, user_no, user_role, invite_code, expire_date) "
	                     + "VALUES (?, ?, ?, ?, SYSDATE + (1/24/60 * 10))";

	    try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
	         PreparedStatement checkPstmt = conn.prepareStatement(checkSql)) {
	        
	        checkPstmt.setInt(1, currentUserNo);
	        checkPstmt.setString(2, spaceKey);
	        
	        try (ResultSet rs = checkPstmt.executeQuery()) {
	            if (!rs.next()) {
	                throw new IllegalAccessException("초대 권한이 없습니다.");
	            }
	        }

	        // Insert after Authorize
	        try (PreparedStatement insertPstmt = conn.prepareStatement(insertSql)) {
	            insertPstmt.setString(1, spaceKey);
	            insertPstmt.setInt(2, userNo);
	            insertPstmt.setString(3, userRole);
	            insertPstmt.setString(4, RandomCodeUtil.generateRandomCode());
	            insertPstmt.executeUpdate();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	// Check Invite Code
	// input : inviteCode, userNo
	// output : boolean
	// userNo : invited user
	boolean checkInviteCode(String inviteCode, String spaceKey, int userNo) throws Exception {
		boolean check = false;
		String sql1 = "SELECT * " + "FROM space_members " + "WHERE invite_code = ? "
				+ "AND expire_date > SYSDATE AND user_no = ? AND space_key = ?";
		String sql2 = "UPDATE space_members " + "SET invite_code = null, expire_date = null " + "WHERE user_no = ? "
				+ "AND space_key = ?";
		String sql3 = "DELETE FROM space_members " + "WHERE user_no = ? AND space_key = ?";
		
		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
			PreparedStatement pstmt1 = conn.prepareStatement(sql1);
			PreparedStatement pstmt2 = conn.prepareStatement(sql2);
			PreparedStatement pstmt3 = conn.prepareStatement(sql3)) {
			
			// Check Invite Code
			pstmt1.setString(1, inviteCode);
			pstmt1.setInt(2, userNo);
			pstmt1.setString(3, spaceKey);
			try (ResultSet rs = pstmt1.executeQuery()) {
				
				if (rs.next()) {
					check = true;
					pstmt2.setInt(1, userNo);
					pstmt2.setString(2, spaceKey);
					pstmt2.executeUpdate();
					}
			else {
					pstmt3.setInt(1, userNo);
					pstmt3.setString(2, spaceKey);
					pstmt3.executeUpdate();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return check;
	}

	// 명세 3.2
	// input : space_key, user_no
	// output : user_role
	String searchUserRole(String spaceKey, int userNo) throws Exception {
		String userRole = null;
		String sql = "SELECT user_role " + "FROM space_members " + "WHERE space_key = ? AND user_no = ?";
		
		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, spaceKey);
			pstmt.setInt(2, userNo);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					userRole = rs.getString("user_role");
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return userRole;
	}

	// 명세 3.3
	// input : space_key, user_no
	// output : boolean
	boolean deleteSpaceMember(String spaceKey, int userNo) throws Exception {
		boolean isDeleted = false;
		String sql1 = "UPDATE task SET worker_no = null WHERE worker_no = ? AND space_key = ?";
		String sql2 = "UPDATE reply SET writer_no = null WHERE writer_no = ? AND space_key = ?";
		String sql3 = "DELETE FROM space_members " + "WHERE user_no = ? AND space_key = ?";
		
		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
			PreparedStatement pstmt1 = conn.prepareStatement(sql1);
			PreparedStatement pstmt2 = conn.prepareStatement(sql2);
			PreparedStatement pstmt3 = conn.prepareStatement(sql3)) {
			
			pstmt1.setInt(1, userNo);
			pstmt1.setString(2, spaceKey);
			pstmt1.executeUpdate();
			
			pstmt2.setInt(1, userNo);
			pstmt2.setString(2, spaceKey);
			pstmt2.executeUpdate();
	
			pstmt3.setInt(1, userNo);
			pstmt3.setString(2, spaceKey);
			pstmt3.executeUpdate();
			
			isDeleted = true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return isDeleted;
	}

	// 멍세 3.4
	// input : current_user_no
	// output : user_no, user_name, image_no
	// user_no : current_user_no를 제외한 나머지 user_no
	// 접속중인 유저와 같은 스페이스에 있는 모든 유저 출력
	List<UserProfileDto> getAllSpaceMembers(int currentUserNo) throws Exception{
		List<UserProfileDto> userList = new ArrayList<>();
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
		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			pstmt.setInt(1, currentUserNo);
			pstmt.setInt(2, currentUserNo);
			try (ResultSet rs = pstmt.executeQuery()) {
				while(rs.next()) {
					UserProfileDto dto = new UserProfileDto();
					dto.setUserNo(rs.getInt("user_no"));
					dto.setUserName(rs.getString("user_name"));
					dto.setEmail(rs.getString("email"));
					dto.setImageTitle(rs.getString("image_title"));
					userList.add(dto);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return userList;
	}
	
	// 명세 3.5
	// input : user_no, space_key
	// output : stored_image_file_name, user_no, user_name, email
	// stored_image_file_name : 저장된 이미지의 제목
	// 접속중인 스페이스의 모든 유저 출력
	List<UserProfileDto> getSpaceMemberList(String currentSpaceKey) throws Exception {
		List<UserProfileDto> memberList = new ArrayList<>();
		String sql = " SELECT i.image_title, u.user_no, u.user_name, u.email " + "FROM space_members s "
				+ "JOIN users u " + "ON s.user_no = u.user_no " + "LEFT OUTER JOIN image i "
				+ "ON u.image_no = i.image_no " + "WHERE s.space_key = ?";
		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
			PreparedStatement pstmt = conn.prepareStatement(sql)){
			
			pstmt.setString(1, currentSpaceKey);
			
			try (ResultSet rs = pstmt.executeQuery()) {

				while (rs.next()) {
					UserProfileDto dto = new UserProfileDto();
					dto.setUserNo(rs.getInt("user_no"));
					dto.setUserName(rs.getString("user_name"));
					dto.setEmail(rs.getString("email"));
					dto.setImageTitle(rs.getString("image_title"));
					memberList.add(dto);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
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
