import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SearchConditionDao {
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:xe";
	String dbId = "test0424";
	String dbPw = "12345";
	
	// 명세 8.1
	// Input : 
	// Output : boolean
	boolean createSearchCondition (
			String searchConditionTitle, 
			Integer workerNo, 
			Integer creatorNo, 
			Integer statusNo, 
			String priority, 
			String labelTitle, 
			String spaceKey, 
			String dueDate, 
			String operatorDueDate
			) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		int chk = 0;
		boolean isCreated = false;
		
		Class.forName(driver);
		conn = DriverManager.getConnection(url, dbId, dbPw);
		
		String sql = "INSERT INTO search_condition (search_condition_no, search_condition_title, "
				+ "worker_no, creator_no, priority, label_title, space_key, due_date, operator_due_date) "
				+ "VALUES (seq_search_condition_no.nextVal, ?, ?, ?, ?, ?, ?, ?, ?)";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, searchConditionTitle);
		pstmt.setObject(2, workerNo);
		pstmt.setObject(3, creatorNo);
		pstmt.setString(4, priority);
		pstmt.setString(5, labelTitle);
		pstmt.setString(6, spaceKey);
		pstmt.setString(7, dueDate);
		pstmt.setString(8, operatorDueDate);
		chk = pstmt.executeUpdate();
		
		if(chk != 0) {
			isCreated = true;
		}
		
		return isCreated;
	}
	
	// 명세 8.2
	// Input : 
	// Output : boolean
	boolean updateSearchCondition(
			String searchConditionTitle, 
			Integer workerNo, 
			Integer creatorNo, 
			Integer statusNo, 
			String priority, 
			String labelTitle, 
			String spaceKey, 
			String dueDate, 
			String operatorDueDate
			) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		int chk = 0;
		boolean isUpdated = false;
		
		Class.forName(driver);
		conn = DriverManager.getConnection(url, dbId, dbPw);
		
		return isUpdated;
	}
	
	public static void main(String[] args) throws Exception {
		SearchConditionDao dao = new SearchConditionDao();
		
		// 8.1 Create Search Condition (clear!)
//		System.out.println(dao.createSearchCondition("나의 필터", null, 2, null, null, null, "ABCD", "20260604", "="));
	}
}
