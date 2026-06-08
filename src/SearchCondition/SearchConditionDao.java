package SearchCondition;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
				cnt++;
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
	
	// Import User Roll for Search Condition
	String importSearchConditionAccessType (int searchConditionNo, int currentUserNo) throws Exception {
		String accessType = "";
		String sql = "SELECT access_type FROM search_condition_access WHERE search_condition_no = ? AND user_no = ?";
		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			pstmt.setInt(1, searchConditionNo);
			pstmt.setInt(2, currentUserNo);
			
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					accessType = rs.getString("access_type");
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return accessType;
	}
	
	// 명세 8.2
	// Input : currentUserNo, user_no, access_type, space_key, search_condition_no
	// Output : boolean
	// Create Access Type in Search Condition
	boolean createSearchConditionAccessType (int searchConditionNo, int userNo, String spaceKey, String accessType) throws Exception {
		boolean isCreated = false;
		String sql = "INSERT INTO search_condition_access (search_condition_no, user_no, space_key, access_type) "
				+ "VALUES (?, ?, ?, ?)";
		
		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, searchConditionNo);
			pstmt.setInt(2, userNo);
			pstmt.setString(3, spaceKey);
			pstmt.setString(4, accessType);
			
			if(pstmt.executeUpdate() != 0) {
				isCreated = true;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return isCreated;
	}
	
	// Delete Access Type in SearchCondition
	boolean deleteSearchConditionAccessType (int searchConditionNo, int userNo, String currentUserAccessType) throws Exception {
		boolean isDeleted = false;
		String sql = "DELETE FROM search_condition_access WHERE search_condition_no = ? AND user_no = ? ";
		
		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			if("owner".equals(currentUserAccessType) || "editor".equals(currentUserAccessType)) {
				pstmt.setInt(1, searchConditionNo);
				pstmt.setInt(2, userNo);
			}
			if(pstmt.executeUpdate() != 0) {
				isDeleted = true;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return isDeleted;
	}
	
	// 명세 8.3
	// Input : search_condition_no, current_user_no, access_type
	// Output : boolean
	boolean deleteSearchCondition(int searchConditionNo, int currentUserNo, String accessType) throws Exception {
		boolean isDeleted = false;
		String sql = "DELETE FROM search_condition WHERE search_condition_no = ?";
		
		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			if("owner".equals(accessType) || "editor".equals(accessType)) {
				pstmt.setInt(1, searchConditionNo);
			}
			else {
				return false;
			}
			if (pstmt.executeUpdate() != 0) {
				isDeleted = true;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return isDeleted;
	}
	
	// 명세 8.4
	// Input : current_user_no, access_type
	// Output : 
	List<SearchConditionDto> showSearchCondition (int currentUserNo, String accessType) throws Exception {
		List<SearchConditionDto> searchConditionList = new ArrayList<>();
		ArrayList<Integer> searchConditionNo = new ArrayList<>();
		
		String sql = "SELECT search_condition_no FROM search_condition_access WHERE user_no = ? AND access_type = 'owner'";
		String titleSql = "SELECT search_condition_title FROM search_condition WHERE search_condition_no = ?";
		String workerSql = "SELECT worker_no FROM search_condition_detail_worker WHERE search_condition_no = ?";
		String creatorSql = "SELECT creator_no FROM search_condition_detail_creator WHERE search_condition_no = ?";
		String statusSql = "SELECT status_no FROM search_condition_detail_status WHERE search_condition_no = ?";
		String prioritySql = "SELECT priority FROM search_condition_detail_priority WHERE search_condition_no = ?";
		String spaceKeySql = "SELECT space_key FROM search_condition_detail_space WHERE search_condition_no = ?";
		String dueDateSql = "SELECT due_date FROM search_condition_detail_due_date WHERE search_condition_no = ?";
		
		try (Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				PreparedStatement pstmt = conn.prepareStatement(sql);
				PreparedStatement pstmtTitle = conn.prepareStatement(titleSql);
				PreparedStatement pstmtWorker = conn.prepareStatement(workerSql);
				PreparedStatement pstmtCreator = conn.prepareStatement(creatorSql);
				PreparedStatement pstmtStatus = conn.prepareStatement(statusSql);
				PreparedStatement pstmtPriority = conn.prepareStatement(prioritySql);
				PreparedStatement pstmtSpaceKey = conn.prepareStatement(spaceKeySql);
				PreparedStatement pstmtDueDate = conn.prepareStatement(dueDateSql)) {
			
			// 유저가 owner인 searchConditionNo들을 조회
			pstmt.setInt(1, currentUserNo);
			try (ResultSet rs = pstmt.executeQuery()){
				while(rs.next()) {
					searchConditionNo.add(rs.getInt("search_condition_no"));
				}
			}
			
			// 조회된 searchConditionNo들의 정보들을 취합
			for (int n : searchConditionNo) {
				
				pstmtTitle.setInt(1, n);
				try (ResultSet rsTitle = pstmtTitle.executeQuery()) {
					if (rsTitle.next()) {
						SearchConditionDto dto = new SearchConditionDto();
						dto.setSearchConditionTitle(rsTitle.getString("search_condition_title"));
						
						pstmtWorker.setInt(1, n);
						try (ResultSet rsWorker = pstmtWorker.executeQuery()) {
							if (rsWorker.next()) {
								dto.setWorkerNo(rsWorker.getInt("worker_no"));
							}
						}
						
						pstmtCreator.setInt(1, n);
						try (ResultSet rsCreator = pstmtCreator.executeQuery()) {
							if (rsCreator.next()) {
								dto.setCreatorNo(rsCreator.getInt("creator_no"));
							}
						}
						
						pstmtStatus.setInt(1, n);
						try (ResultSet rsStatus = pstmtStatus.executeQuery()) {
							if (rsStatus.next()) {
								dto.setStatusNo(rsStatus.getInt("status_no"));
							}
						}
						
						pstmtPriority.setInt(1, n);
						try (ResultSet rsPriority = pstmtPriority.executeQuery()) {
							if (rsPriority.next()) {
								dto.setPriority(rsPriority.getString("priority"));
							}
						}
						
						pstmtSpaceKey.setInt(1, n);
						try (ResultSet rsSpaceKey = pstmtSpaceKey.executeQuery()) {
							if (rsSpaceKey.next()) {
								dto.setSpaceKey(rsSpaceKey.getString("space_key"));
							}
						}
						
						pstmtDueDate.setInt(1, n);
						try (ResultSet rsDueDate = pstmtDueDate.executeQuery()) {
							if (rsDueDate.next()) {
								dto.setDueDate(rsDueDate.getString("due_date"));
							}
						}
						searchConditionList.add(dto);
					}
					
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return searchConditionList;
	}
	
	public static void main(String[] args) throws Exception {
		SearchConditionDao dao = new SearchConditionDao();
		
		// 8.1 Create Search Condition (clear!)
//		System.out.println(dao.createSearchCondition("나의 필터", null, 2, null, null, null, "ABCD", "20260604", "="));
		
		System.out.println(dao.deleteSearchCondition(1, 2, "owner"));
	}
}
