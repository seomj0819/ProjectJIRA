package SearchCondition;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchConditionDao {
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:xe";
	String dbId = "test0424";
	String dbPw = "12345";
	
	// 명세 8.1
	// Input : 
	// Output : boolean
	boolean createSearchCondition (
			int currentUserNo,
			String spaceCanAccess,
			Integer userCanAccess,
			String accessType,
			String searchConditionTitle,
			String searchConditionDescription,
			String spaceKey, 
			String operatorSpace,
			Integer workerNo, 
			String operatorWorker,
			Integer creatorNo, 
			String operatorCreator,
			String priority, 
			String operatorPriority,
			Integer statusNo, 
			String operatorStatus,
			String dueDate, 
			String operatorDueDate
			) throws Exception {
	boolean isCreated = false;
		int chk = 0;
		int cnt = 2;
		String sql1 = "INSERT INTO search_condition (search_condition_no, search_condition_title, "
				+ "search_condition_description, operator_space, operator_worker, operator_creator, operator_priority, operator_status, operator_due_date) "
				+ "VALUES (seq_search_condition_no.nextVal, ?, ?, ?, ?, ?, ?, ?, ?)";
		String sql2 = "INSERT INTO search_condition_access (search_condition_no, user_no, space_key, access_type) "
				+ "VALUES (seq_search_condition_no.currVal, ?, null, 'owner')";
		String sql3 = "INSERT INTO search_condition_detail_space (search_condition_detail_space_no, search_condition_no, space_key) "
				+ "VALUES (seq_condition_detail_space_no.nextVal, seq_search_condition_no.currVal, ?)";
		String sql4 = "INSERT INTO search_condition_detail_worker (search_condition_detail_worker_no, search_condition_no, worker_no) "
				+ "VALUES (seq_condition_detail_worker_no.nextVal, seq_search_condition_no.currVal, ?)";
		String sql5 = "INSERT INTO search_condition_detail_creator (search_condition_detail_creator_no, search_condition_no, creator_no) "
				+ "VALUES (seq_condition_detail_creator_no.nextVal, seq_search_condition_no.currVal, ?)";
		String sql6 = "INSERT INTO search_condition_detail_priority (search_condition_detail_priority_no, search_condition_no, priority) "
				+ "VALUES (seq_condition_detail_priority_no.nextVal, seq_search_condition_no.currVal, ?)";
		String sql7 = "INSERT INTO search_condition_detail_status (search_condition_detail_status_no, search_condition_no, status_no) "
				+ "VALUES (seq_condition_detail_status_no.nextVal, seq_search_condition_no.currVal, ?)";
		String sql8 = "INSERT INTO search_condition_detail_due_date (search_condition_detail_due_date_no, search_condition_no, due_date) "
				+ "VALUES (seq_condition_detail_due_date_no.nextVal, seq_search_condition_no.currVal, ?)";
		String sql9 = "INSERT INTO search_condition_access (search_condition_no, user_no, space_key, access_type) "
				+ "VALUES (seq_search_condition_no.currVal, null, ?, ?)";
		String sql10 = "INSERT INTO search_condition_access (search_condition_no, user_no, space_key, access_type) "
				+ "VALUES (seq_search_condition_no.currVal, ?, null, ?)";
		
		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt1 = conn.prepareStatement(sql1);
				PreparedStatement pstmt2 = conn.prepareStatement(sql2);
				PreparedStatement pstmt3 = conn.prepareStatement(sql3);
				PreparedStatement pstmt4 = conn.prepareStatement(sql4);
				PreparedStatement pstmt5 = conn.prepareStatement(sql5);
				PreparedStatement pstmt6 = conn.prepareStatement(sql6);
				PreparedStatement pstmt7 = conn.prepareStatement(sql7);
				PreparedStatement pstmt8 = conn.prepareStatement(sql8);
				PreparedStatement pstmt9 = conn.prepareStatement(sql9);
				PreparedStatement pstmt10 = conn.prepareStatement(sql10)) {
			
			pstmt1.setString(1, searchConditionTitle);
			pstmt1.setString(2, searchConditionDescription);
			pstmt1.setObject(3, operatorSpace);
			pstmt1.setString(4, operatorWorker);
			pstmt1.setString(5, operatorCreator);
			pstmt1.setString(6, operatorPriority);
			pstmt1.setString(7, operatorStatus);
			pstmt1.setString(8, operatorDueDate);
			chk = pstmt1.executeUpdate();
			
			pstmt2.setInt(1, currentUserNo);
			chk += pstmt2.executeUpdate();
			
			if(spaceKey != null && !spaceKey.isBlank()) {
				pstmt3.setString(1, spaceKey);
				chk += pstmt3.executeUpdate();
				cnt++;
			}
			
			if(workerNo != null) {
				pstmt4.setInt(1, workerNo);
				chk += pstmt4.executeUpdate();
				cnt++;
			}
			
			if(creatorNo != null) {
				pstmt5.setInt(1, creatorNo);
				chk += pstmt5.executeUpdate();
				cnt++;
			}
			
			if(priority != null && !priority.isBlank()) {
				pstmt6.setString(1, priority);
				chk += pstmt6.executeUpdate();
			}
			
			if(statusNo != null) {
				pstmt7.setInt(1, statusNo);
				chk += pstmt7.executeUpdate();
				cnt++;
			}
			
			if(dueDate != null && !dueDate.isBlank()) {
				pstmt8.setString(1, dueDate);
				chk += pstmt8.executeUpdate();
				cnt++;
				
			}
			
			if(spaceCanAccess != null && !spaceCanAccess.isBlank()) {
				pstmt9.setString(1, spaceCanAccess);
				pstmt9.setString(2, accessType);
				chk += pstmt9.executeUpdate();
				cnt++;
				
			}
			
			if(userCanAccess != null) {
				pstmt10.setInt(1, userCanAccess);
				pstmt10.setString(2, accessType);
				chk += pstmt10.executeUpdate();
				cnt++;
				
			}
			
			if(chk == cnt) {
				isCreated = true;
				conn.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		int chk = 0;
		boolean isUpdated = false;
		String sql = "UPDATE ";
		
		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
		
		} catch(Exception e) {
			e.printStackTrace();
		}
		return isUpdated;
	}
	
	public static void main(String[] args) throws Exception {
		SearchConditionDao dao = new SearchConditionDao();
		
		// 8.1 Create Search Condition (clear!)
//		System.out.println(dao.createSearchCondition("나의 필터", null, 2, null, null, null, "ABCD", "20260604", "="));
	}
}
