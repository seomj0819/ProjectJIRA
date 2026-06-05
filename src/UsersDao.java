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
	// 비밃런호를 암호화 해서 저장해야 할 수도?
	int checkLocalLogin(String email, String pw) throws Exception {
		int userNo = 0;
		String sql = "SELECT user_no " + "FROM users " + "WHERE email = ? AND pw = ?";
		
		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, email);
			pstmt.setString(2, pw);
			try(ResultSet rs = pstmt.executeQuery()){
				if (rs.next()) {
					userNo = rs.getInt("user_no");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userNo;
	}

	// 명세 1.2
	// input : email, google_api
	// output : user_no
	// email : 유저 이메일
	// google_api : GoogleUnique ID
	// 로그인 성공 시 user_no 반환, 실패시 0 반환
	int checkGoogleLogin(String email, String googleApi) throws Exception {
		int userNo = 0;
		String sql = "SELECT user_no " + "FROM users " + "WHERE email=? AND google_api =?";
		
			try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
					PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, email);
			pstmt.setString(2, googleApi);
			try (ResultSet rs = pstmt.executeQuery()){
				if (rs.next()) {
					userNo = rs.getInt("user_no");
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return userNo;
	}

	// 명세 1.3
	// input : email, pw, user_name
	// output : -
	// email : 유저 이메일
	// pw : 유저 비밀번호
	// user_name : 유저 이름
	boolean localRegister(String email, String pw, String user_name) throws Exception {
		boolean isRegister = false;
		String sql = "INSERT INTO users(user_no, email, pw, user_name, image_no, verification_code, expire_date) "
				+ "VALUES (seq_user_no.nextVal, ?, ?, ?, 1, null, null)";
		
		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, email);
			pstmt.setString(2, pw);
			pstmt.setString(3, user_name);
			int result = pstmt.executeUpdate();
	
			if (result > 0) {
				isRegister = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isRegister;
	}

	// 명세 1.4
	// input : email, google_api, user_name
	// output : -
	// email : 유저 이메일
	// google_api : GoogleUnique ID
	// user_name : 유저 이름
	boolean googleRegister(String email, String google_api, String user_name) throws Exception {
		boolean isRegister = false;
		String sql = "INSERT INTO users(user_no, email, google_api, user_name, image_no, verification_code, expire_date) "
				+ "VALUES (seq_user_no.nextVal, ?, ?, ?, 1, null, null)";
		
		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, email);
			pstmt.setString(2, google_api);
			pstmt.setString(3, user_name);
			int result = pstmt.executeUpdate();
	
			if (result > 0) {
				isRegister = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isRegister;
	}

	// 명세 1.5
	// input : email
	// output : pw
	// email : 유저 이메일
	// 입력된 email이 DB에 존재 할때 본인확인 Email 발송 후 인증 되면 pw 노출
	String findPassword(String email) throws Exception {
		String pw = null;
		String sql = "SELECT pw FROM users WHERE email = ?";
		
		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, email);
			try(ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					pw = rs.getString("pw");
				} else {
					System.out.println("등록되지 않은 이메일 입니다.");
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return pw;
	}

	// 명세 1.6
	// input : email, pw, inputCode
	// output : int (성공하면 1, 실패하면 0)
	// inputCode : 생성된 인증코드
	// 실행 전에 인증코드 생성 메서드 실행 할 것
	boolean deleteUser(String email, String pw, String inputCode) throws Exception {
		boolean isDelete = false;
		int result = 0;
		String sql = "DELETE FROM users "
				+ "WHERE email = ? AND pw = ? AND verification_code = ? AND expire_date > SYSDATE";
		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, email);
			pstmt.setString(2, pw);
			pstmt.setString(3, inputCode);
			result = pstmt.executeUpdate();
			if (result > 0) {
				isDelete = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isDelete;
	}

	// 명세 1.7
	// input : email, pw, inputCode
	// output : int (성공하면 1, 실패하면 0)
	// inputCode : 생성된 인증코드
	// 실행 전에 인증코드 생성 메서드 실행 할 것
	boolean changePw(String email, String pw, String inputCode) throws Exception {
		boolean isChanged = false;
		int result = 0;
		String sql = "UPDATE users " + "SET pw = ? "
				+ "WHERE email = ? AND verification_code = ? AND expire_date > SYSDATE";
		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, pw);
			pstmt.setString(2, email);
			pstmt.setString(3, inputCode);
			result = pstmt.executeUpdate();
	
			if (result > 0) {
				isChanged = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isChanged;
	}

	// 명세 1.8
	// input : user_no
	// output : List<UserProfileDto>
	// 해당 유저의 프로필 가져오기
	UserProfileDto getUserProfile(int userNo) throws Exception {
		UserProfileDto dto = null;
		String sql = "SELECT u.user_no, u.user_name, u.email, i.image_title " + "	FROM image i JOIN users u "
				+ "	ON i.image_no = u.image_no " + "WHERE u.user_no = ?";
		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userNo);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					dto = new UserProfileDto();
					dto.setUserNo(rs.getInt("user_no"));
					dto.setUserName(rs.getString("user_name"));
					dto.setEmail(rs.getString("email"));
					dto.setImageTitle(rs.getString("image_title"));
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} 
		return dto;
	}

	// 명세 1.10
	// input : email
	// output : -
	// email : 유저 이메일
	// google_api : GoogleUnique ID
	// user_name : 유저 이름
	// 회원가입 단계에서 eamil 중복 체크
	boolean emailCheck(String email) throws Exception {
		boolean isExist = false;
		String sql = "SELECT email FROM users WHERE email = ?";
		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, email);
			try(ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					isExist = true;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} 
		return isExist;
	}

	// 명세 1.11
	// input : email, verification_code
	// output : user_no
	// email : 유저 이메일
	// verification_code : 인증 코드
	// 비밀번호 찾기, 회원가입 시 진행, 작성한 인증 코드가 일치 하고 만료 시점 전이면 user_no 반환, 실패시 0 반환
	String createVerificationCode(String email) throws Exception {
		// Create Code
		String verificationCode = RandomCodeUtil.generateRandomCode();
		
		// Update Code
		String sql = "UPDATE users " + "SET verification_code = ?, " + "expire_date = SYSDATE + (1/24/60 * 10) "
				+ "WHERE email = ?";
		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, verificationCode);
			pstmt.setString(2, email);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return verificationCode;
	}

	// VerificationCode Check
	boolean checkEmailVerification(int userNo, String inputCode) throws Exception {
		boolean verification = false;
		
		// Check Code
		String sql = "SELECT user_no " + "FROM users "
				+ "WHERE user_no = ? AND verification_code = ? AND expire_date > SYSDATE";
		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userNo);
			pstmt.setString(2, inputCode);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					verification = true;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} 
		return verification;
	}

	public static void main(String[] args) throws Exception {
		UsersDao dao = new UsersDao();
		
		// 1.1 Local Login (clear!)
//		System.out.println(dao.checkLocalLogin("seomj081923@gmail.com", "12345"));

		// 1.2 Check Google Login (clear!)
//		System.out.println(dao.checkGoogleLogin("abc@abc.com", "google_api"));

		// 1.3 Local Register (clear!)
//		if(dao.localRegister("seomj081923@gmail.com", "12345", "MJ")) {
//			System.out.println("성공!");
//		}

		// 1.4 Google Retister (clear!)
//		if(dao.googleRegister("abc@abc.com", "google_api", "SK")) {
//			System.out.println("성공!");
//		}

		// 1.5 Find Password (clear!)
//		System.out.println(dao.findPassword("seomj081923@gmail.com"));

		// 1.6 Delete User (clear!)
//		System.out.println(dao.deleteUser("abc@abc.com", null, null));

		// 1.7 Change Password (clear!)
//		System.out.println(dao.changePw("abc@abc.com", null, null));

		// 1.8 Get User Profile (clear!)
		UserProfileDto dto = dao.getUserProfile(2);
		System.out.println(dto);

		// 1.10 Email Check (clear!)
//		if(dao.emailCheck("seomj081923@gmail.com")) {
//			System.out.println("중복됨!");
//		}

		// 1.11 Email Verification (clear!)
//		dao.createVerificationCode("abc@abc.com");
//		System.out.println(dao.checkEmailVerification(5, "X5r7G2"));
	}
}
