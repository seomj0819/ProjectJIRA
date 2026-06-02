import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TaskDao {
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:xe";
	String dbId = "test0424";
	String dbPw = "12345";
	
	// 명세 4.1
	// input : space_key, creator_no, worker_no, 
	//		   task_title, task_description, due_date, label_title, status, 
	//		   priority, upper_task_no, task_order, image_no, summary
	// output : -
	boolean createTask (
			String spaceKey, 
			int currentUserNo, 
			int workerNo, 
			String taskTitle, 
			String taskDescription, 
			String dueDate, 
			String labelTitle, 
			int statusNo, 
			String priority, 
			Integer upperTaskNo, 
			int taskOrder, 
			String summary
		) throws Exception {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean taskCreated = false;
		int result = 0;
		
		Class.forName(driver);
		conn = DriverManager.getConnection(url, dbId, dbPw);
		
		String sql = "INSERT INTO task (space_key, task_no, creator_no, worker_no, task_title, task_description, due_date, label_title, status, priority, upper_task_no, task_order, summary) "
				+ "VALUES (?, seq_task_no.nextval, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), ?, ?, ?, ?, ?, ?)";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, spaceKey);
		pstmt.setInt(2, currentUserNo);
		pstmt.setObject(3, workerNo);
		pstmt.setString(4, taskTitle);
		pstmt.setString(5, taskDescription);
		pstmt.setString(6, dueDate);
		pstmt.setString(7, labelTitle);
		pstmt.setInt(8, statusNo);
		pstmt.setString(9, priority);
		pstmt.setObject(10, upperTaskNo);
		pstmt.setInt(11, taskOrder);
		pstmt.setString(12, summary);
		result = pstmt.executeUpdate();
		
		if(result != 0) {
			taskCreated = true;
		}
		
		pstmt.close();
		conn.close();
		
		return taskCreated;
	}
	
	// 명세 4.2
	// input : space_key, creator_no, worker_no, 
	//		   task_title, task_description, due_date, label_title, status, 
	//		   priority, upper_task_no, task_order, image_no, summary
	// output : List<TaskInfoDto>
	List<TaskInfoDto> SearchTaskBySearchCondition (
			int currentUserNo,
			String searchKeyWord, 
			Integer searchWorkerNo, 
			Integer searchCreatorNo,
			String searchStatusNo,
			String searchSpaceKey,
			String searchPriority,
			String searchDueDate,
			String operatorDueDate // ==, ,!= >=, <=
		) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<TaskInfoDto> list = new ArrayList<>();
		Class.forName(driver);
		conn = DriverManager.getConnection(url, dbId, dbPw);
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT space_key, task_no, creator_no, worker_no, task_title, ");
	    sql.append("       task_description, due_date, label_title, status_no, priority, ");
	    sql.append("       upper_task_no, task_order, summary ");
	    sql.append("FROM task ");
	    sql.append("WHERE space_key IN (SELECT space_key FROM space_members WHERE user_no = ?) ");
		
		if (searchKeyWord != null && !searchKeyWord.isBlank()) {
			sql.append("AND (task_title LIKE '%' || ? || '%' OR (space_key || '-' || task_no) LIKE '%' || ? || '%') ");
		}
		
	    if(searchWorkerNo != null) {
	    	sql.append("AND worker_no = ? ");
	    }
	    
	    if(searchCreatorNo != null) {
	    	sql.append("AND creator_no = ? ");
	    }
	    
	    if(searchStatusNo != null && !searchStatusNo.isBlank()) {
	    	sql.append("AND status_no = ? ");
	    }
	    
	    if(searchSpaceKey != null && !searchSpaceKey.isBlank()) {
	    	sql.append("AND space_key = ? ");
	    }
		
		if(searchPriority != null && !searchPriority.isBlank()) {
			sql.append("AND priority = ? ");
		}
		
		if(searchDueDate != null 
				&& !searchDueDate.isBlank()
				&& operatorDueDate != null
				&& !operatorDueDate.isBlank()) {
			sql.append("AND due_date "+ operatorDueDate +" ? ");
		}
		
		sql.append("ORDER BY task_order ASC");
		
		int paramIndex = 1;
		pstmt.setInt(paramIndex++, currentUserNo);
		
		if (searchKeyWord != null && !searchKeyWord.isBlank()) {
			pstmt.setString(paramIndex++, searchKeyWord);
			pstmt.setString(paramIndex++, searchKeyWord);
		}
		
	    if(searchWorkerNo != null) {
	    	pstmt.setInt(paramIndex++, searchWorkerNo);
	    }
	    
	    if(searchCreatorNo != null) {
	    	pstmt.setInt(paramIndex++, searchCreatorNo);
	    }
	    
	    if(searchStatusNo != null && !searchStatusNo.isBlank()) {
	    	pstmt.setString(paramIndex++, searchStatusNo);
	    }
	    
	    if(searchSpaceKey != null && !searchSpaceKey.isBlank()) {
	    	pstmt.setString(paramIndex++, searchSpaceKey);
	    }
		
		if(searchPriority != null && !searchPriority.isBlank()) {
			pstmt.setString(paramIndex++, searchPriority);
		}
		
		if(searchDueDate != null 
				&& !searchDueDate.isBlank()
				&& operatorDueDate != null
				&& !operatorDueDate.isBlank()) {
			pstmt.setString(paramIndex++, searchDueDate);
			
		}
		
		rs = pstmt.executeQuery();
		
		while (rs.next()) {
			TaskInfoDto dto = new TaskInfoDto.Builder()
					.spaceKey(rs.getString("space_key"))
					.taskNo(rs.getInt("task_no"))
					.creatorNo(rs.getInt("creator_no"))
		            .workerNo(rs.getInt("worker_no"))
		            .taskTitle(rs.getString("task_title"))
		            .taskDescription(rs.getString("task_description"))
		            .dueDate(rs.getString("due_date"))
		            .labelTitle(rs.getString("label_title"))
		            .statusNo(rs.getString("status_no"))
		            .priority(rs.getString("priority"))
		            .upperTaskNo(rs.getObject("upper_task_no") != null ? rs.getInt("upper_task_no") : null)
		            .taskOrder(rs.getInt("task_order"))
		            .summary(rs.getString("summary"))
		            .build();
			list.add(dto);
		}
		
		rs.close();
		pstmt.close();
		conn.close();
		
		return list;
	}
	
	public static void main(String[] args)  throws Exception{
		TaskDao dao = new TaskDao();
		
		// 4.1 Create Task (clear!)
//		System.out.println(dao.createTask("ABCD", 2, 5, "담배피러가기", "2개 피우기", "2026-06-02", null, 1, "medium", null, 2, null));
	}
}
