import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UsersDao {
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:xe";
	String dbId = "test0424";
	String dbPw = "12345";

	// 명세 1.1
	// input : email, pw
	// output : user_no
	// email : 유저 이메일
	// pw : 유저 비밀번호
	// 로그인 성공 시 user_no 반환, 실패시 0 반환
	int checkLocalLogin(String email, String pw) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int userNo = 0;
		Class.forName(driver);
		conn = DriverManager.getConnection(url, dbId, dbPw);
		String sql = "SELECT user_no " + "FROM users " + "WHERE email = ? AND pw = ?";

		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, email);
		pstmt.setString(2, pw);
		rs = pstmt.executeQuery();

		if (rs.next()) {
			userNo = rs.getInt("user_no");
		}

		rs.close();
		pstmt.close();
		conn.close();
		return userNo;
	}

	// 명세 1.2
	// input : email, google_api
	// output : user_no
	// email : 유저 이메일
	// google_api : GoogleUnique ID
	// 로그인 성공 시 user_no 반환, 실패시 0 반환
	int checkGoogleLogin(String email, String googleApi) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int userNo = 0;

		Class.forName(driver);
		conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "SELECT user_no " + "FROM users " + "WHERE email=? AND google_api =?";

		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, email);
		pstmt.setString(2, googleApi);
		rs = pstmt.executeQuery();

		if (rs.next()) {
			userNo = rs.getInt("user_no");
		}

		rs.close();
		pstmt.close();
		conn.close();

		return userNo;
	}

	// 명세 1.3
	// input : email, pw, user_name
	// output : -
	// email : 유저 이메일
	// pw : 유저 비밀번호
	// user_name : 유저 이름
	boolean localRegister(String email, String pw, String user_name) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isRegister = false;
		Class.forName(driver);
		conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "INSERT INTO users(user_no, email, pw, user_name, image_no, verification_code, expire_date) "
				+ "VALUES (seq_user_no.nextVal, ?, ?, ?, 1, null, null)";

		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, email);
		pstmt.setString(2, pw);
		pstmt.setString(3, user_name);
		int result = pstmt.executeUpdate();

		if (result > 0) {
			isRegister = true;
		}

		pstmt.close();
		conn.close();

		return isRegister;
	}

	// 명세 1.4
	// input : email, google_api, user_name
	// output : -
	// email : 유저 이메일
	// google_api : GoogleUnique ID
	// user_name : 유저 이름
	boolean googleRegister(String email, String google_api, String user_name) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isRegister = false;

		Class.forName(driver);
		conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "INSERT INTO users(user_no, email, google_api, user_name, image_no, verification_code, expire_date) "
				+ "VALUES (seq_user_no.nextVal, ?, ?, ?,default_image)";

		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, email);
		pstmt.setString(2, google_api);
		pstmt.setString(3, user_name);
		int result = pstmt.executeUpdate();

		if (result > 0) {
			isRegister = true;
		}

		pstmt.close();
		conn.close();

		return isRegister;
	}

	// 명세 1.10
	// input : email
	// output : -
	// email : 유저 이메일
	// google_api : GoogleUnique ID
	// user_name : 유저 이름
	// 회원가입 단계에서 eamil 중복 체크
	boolean emailCheck(String email) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean isExist = false;

		Class.forName(driver);
		conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "SELECT email FROM users WHERE email = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, email);
		rs = pstmt.executeQuery();

		if (rs.next()) {
			isExist = true;
		}

		rs.close();
		pstmt.close();
		conn.close();

		return isExist;
	}

	// 명세 1.5
	// input : email
	// output : pw
	// email : 유저 이메일
	// 입력된 email이 DB에 존재 할때 본인확인 Email 발송 후 인증 되면 pw 노출
	String findPassword(String email) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String pw = null;

		Class.forName(driver);
		conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "SELECT pw FROM users WHERE email = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, email);
		rs = pstmt.executeQuery();

		if (rs.next()) {
			pw = rs.getString("pw");
		} else {
			System.out.println("등록되지 않은 이메일 입니다.");
		}

		rs.close();
		pstmt.close();
		conn.close();

		return pw;
	}

	// 명세 1.11
	// input : email, verification_code
	// output : user_no
	// email : 유저 이메일
	// verification_code : 인증 코드
	// 비밀번호 찾기, 회원가입 시 진행, 작성한 인증 코드가 일치 하고 만료 시점 전이면 user_no 반환, 실패시 0 반환
	String createVerificationCode(String email) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		Class.forName(driver);
		conn = DriverManager.getConnection(url, dbId, dbPw);

		// Create Code
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		SecureRandom random = new SecureRandom();
		StringBuilder code = new StringBuilder();

		for (int i = 0; i < 6; i++) {
			int t = random.nextInt(chars.length());
			code.append(chars.charAt(t));
		}

		String verificationCode = code.toString();

		// Update Code
		String sql = "UPDATE users " + "SET verification_code = ?, " + "expire_date = SYSDATE + (1/24/60 * 10) "
				+ "WHERE email = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, verificationCode);
		pstmt.setString(2, email);
		pstmt.executeUpdate();

		if (rs.next()) {

		}

		rs.close();
		pstmt.close();
		conn.close();

		return verificationCode;
	}

	// VerificationCode Check
	int checkEmailVerification(String email, String inputCode) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int userNo = 0;

		Class.forName(driver);
		conn = DriverManager.getConnection(url, dbId, dbPw);

		// Check Code
		String sql = "SELECT user_no " + "FROM users "
				+ "WHERE email = ? AND verification_code = ? AND expire_date > SYSDATE";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, email);
		pstmt.setString(2, inputCode);
		rs = pstmt.executeQuery();

		if (rs.next()) {
			userNo = rs.getInt("user_no");
		}

		rs.close();
		pstmt.close();
		conn.close();

		return userNo;
	}

	// 명세 1.6
	// input : email, pw, inputCode
	// output : int (성공하면 1, 실패하면 0)
	// inputCode : 생성된 인증코드
	// 실행 전에 인증코드 생성 메서드 실행 할 것
	int deleteUser(String email, String pw, String inputCode) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int isDelete = 0;
		int result = 0;

		Class.forName(driver);
		conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "DELETE FROM users "
				+ "WHERE email = ? AND pw = ? AND verification_code = ? AND expire_date > SYSDATE;";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, email);
		pstmt.setString(2, pw);
		pstmt.setString(3, inputCode);
		result = pstmt.executeUpdate();

		if (result > 0) {
			isDelete = 1;
		}

		pstmt.close();
		conn.close();
		
		return isDelete;
	}

	//명세 1.7
	// input : email, pw, inputCode
	// output : int (성공하면 1, 실패하면 0)
	// inputCode : 생성된 인증코드
	// 실행 전에 인증코드 생성 메서드 실행 할 것
	int changePw(String email, String pw, String inputCode) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		int isChanged = 0;
		int result = 0;
		
		Class.forName(driver);
		conn = DriverManager.getConnection(url, dbId, dbPw);
		
		String sql = "UPDATE users "
				+ "SET pw = ? "
				+ "WHERE email = ? AND verification_code = ? AND expire_Date > SYSDATE";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, pw);
		pstmt.setString(2, email);
		pstmt.setString(3, inputCode);
		result = pstmt.executeUpdate();
		
		if(result > 0) {
			isChanged = 1;
		}
		
		pstmt.close();
		conn.close();
		
		return isChanged;
	}
	
	//명세 1.8
	// input : user_no
	// output : List<UserProfileDto>
	// 해당 유저의 프로필 가져오기
	UserProfileDto getUserProfile(int userNo) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		UserProfileDto dto = null;
		
		Class.forName(driver);
		conn = DriverManager.getConnection(url, dbId, dbPw);
		
		String sql = "SELECT u.user_no, u.user_name, u.email, i.image_title "
				+ "	FROM image i JOIN users u "
				+ "	ON i.image_no = u.image_no "
				+ "WHERE u.user_no = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, userNo);
		rs = pstmt.executeQuery();
		
		if(rs.next()) {
			dto = new UserProfileDto();
			dto.setUserNo(rs.getInt("user_no"));
			dto.setUserName(rs.getString("user_name"));
			dto.setEmail(rs.getString("email"));
			dto.setImageTitle(rs.getString("image_title"));
		}
		
		rs.close();
		pstmt.close();
		conn.close();
		
		return dto;
	}
	
	public static void main(String[] args) throws Exception {
		UsersDao dao = new UsersDao();
		int userNo = dao.checkGoogleLogin("admin@admin.com", "googleApi");
		System.out.println("로그인 유저 번호 : " + userNo);

	}
}
