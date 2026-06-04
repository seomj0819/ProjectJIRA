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
	// task_title, task_description, due_date, label_title, status,
	// priority, upper_task_no, task_order, image_no, summary
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
			String summary
			) throws Exception {

		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean taskCreated = false;
		int result = 0;

		Class.forName(driver);
		conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "INSERT INTO task (space_key, task_no, creator_no, "
				+ "						worker_no, task_title, task_description, due_date, label_title, "
				+ "						status_no, priority, upper_task_no, task_order, summary) "
				+ "VALUES (?, seq_task_no.nextval, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), ?, ?, ?, ?, seq_task_order.nextval, ?)";
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
		pstmt.setString(11, summary);
		result = pstmt.executeUpdate();

		if (result != 0) {
			taskCreated = true;
		}

		pstmt.close();
		conn.close();

		return taskCreated;
	}

	// 명세 4.2
	// input : space_key, creator_no, worker_no,
	// task_title, task_description, due_date, label_title, status,
	// priority, upper_task_no, task_order, image_no, summary
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

		if (searchWorkerNo != null) {
			sql.append("AND worker_no = ? ");
		}

		if (searchCreatorNo != null) {
			sql.append("AND creator_no = ? ");
		}

		if (searchStatusNo != null && !searchStatusNo.isBlank()) {
			sql.append("AND status_no = ? ");
		}

		if (searchSpaceKey != null && !searchSpaceKey.isBlank()) {
			sql.append("AND space_key = ? ");
		}

		if (searchPriority != null && !searchPriority.isBlank()) {
			sql.append("AND priority = ? ");
		}

		if (searchDueDate != null && !searchDueDate.isBlank() && operatorDueDate != null
				&& !operatorDueDate.isBlank()) {
			sql.append("AND TO_CHAR(due_date) " + operatorDueDate + " ? ");
		}

		sql.append("ORDER BY task_order ASC");

		pstmt = conn.prepareStatement(sql.toString());

		int paramIndex = 1;
		pstmt.setInt(paramIndex++, currentUserNo);

		if (searchKeyWord != null && !searchKeyWord.isBlank()) {
			pstmt.setString(paramIndex++, searchKeyWord);
			pstmt.setString(paramIndex++, searchKeyWord);
		}

		if (searchWorkerNo != null) {
			pstmt.setInt(paramIndex++, searchWorkerNo);
		}

		if (searchCreatorNo != null) {
			pstmt.setInt(paramIndex++, searchCreatorNo);
		}

		if (searchStatusNo != null && !searchStatusNo.isBlank()) {
			pstmt.setString(paramIndex++, searchStatusNo);
		}

		if (searchSpaceKey != null && !searchSpaceKey.isBlank()) {
			pstmt.setString(paramIndex++, searchSpaceKey);
		}

		if (searchPriority != null && !searchPriority.isBlank()) {
			pstmt.setString(paramIndex++, searchPriority);
		}

		if (searchDueDate != null && !searchDueDate.isBlank() && operatorDueDate != null
				&& !operatorDueDate.isBlank()) {
			pstmt.setString(paramIndex++, searchDueDate);
		}

		rs = pstmt.executeQuery();

		while (rs.next()) {
			TaskInfoDto dto = new TaskInfoDto();
					dto.setSpaceKey(rs.getString("space_key"));
					dto.setTaskNo(rs.getInt("task_no"));
					dto.setCreatorNo(rs.getInt("creator_no"));
					dto.setWorkerNo(rs.getInt("worker_no"));
					dto.setTaskTitle(rs.getString("task_title"));
					dto.setTaskDescription(rs.getString("task_description"));
					dto.setDueDate(rs.getString("due_date"));
					dto.setLabelTitle(rs.getString("label_title"));
					dto.setStatusNo(rs.getString("status_no"));
					dto.setPriority(rs.getString("priority"));
					dto.setUpperTaskNo(rs.getObject("upper_task_no") != null ? rs.getInt("upper_task_no") : null);
					dto.setTaskOrder(rs.getInt("task_order"));
					dto.setSummary(rs.getString("summary"));
				list.add(dto);
		}

		rs.close();
		pstmt.close();
		conn.close();

		return list;
	}

	// 명세 4.3
	// input : task_id
	boolean updateTask (
			String currentSpaceKey, 
			int taskNo, 
			String updateTitle,
			String updateDescription, 
			String updateDueDate, 
			Integer updateWorkerNo, 
			String updateLabel, 
			String updatePriority,
			Integer updateStatusNo
			) throws Exception {

		Connection conn = null;
		PreparedStatement pstmt = null;
		int chk = 0;
		boolean isUpdated = false;

		Class.forName(driver);
		conn = DriverManager.getConnection(url, dbId, dbPw);

		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE task ");
		sql.append("SET task_no = task_no ");

		if (updateDescription != null && !updateDescription.isBlank()) {
			sql.append(", task_description = ? ");
		}

		if (updateTitle != null && !updateTitle.isBlank()) {
			sql.append(", task_title = ? ");
		}

		if (updateDueDate != null && !updateDueDate.isBlank()) {
			sql.append(", due_date = ? ");
		}

		if (updateWorkerNo != null) {
			sql.append(", worker_no = ? ");
		}

		if (updateLabel != null && !updateLabel.isBlank()) {
			sql.append(", label_title = ? ");
		}

		if (updatePriority != null && !updatePriority.isBlank()) {
			sql.append(", priority = ? ");
		}

		if (updateStatusNo != null) {
			sql.append(", status_no = ? ");
		}

		sql.append("WHERE space_key = ? ");
		sql.append("AND task_no = ?");
		pstmt = conn.prepareStatement(sql.toString());
		int paramIdx = 1;
		if (updateDescription != null && !updateDescription.isBlank()) {
			pstmt.setString(paramIdx++, updateDescription);
		}

		if (updateTitle != null && !updateTitle.isBlank()) {
			pstmt.setString(paramIdx++, updateTitle);
		}

		if (updateDueDate != null && !updateDueDate.isBlank()) {
			pstmt.setString(paramIdx++, updateDueDate);
		}

		if (updateWorkerNo != null) {
			pstmt.setInt(paramIdx++, updateWorkerNo);
		}

		if (updateLabel != null && !updateLabel.isBlank()) {
			pstmt.setString(paramIdx++, updateLabel);
		}

		if (updatePriority != null && !updatePriority.isBlank()) {
			pstmt.setString(paramIdx++, updatePriority);
		}

		if (updateStatusNo != null) {
			pstmt.setInt(paramIdx++, updateStatusNo);
		}

		pstmt.setString(paramIdx++, currentSpaceKey);
		pstmt.setInt(paramIdx++, taskNo);
		chk = pstmt.executeUpdate();

		if (chk != 0) {
			isUpdated = true;
		}

		pstmt.close();
		conn.close();

		return isUpdated;
	}

	// Update Task Order
	boolean updateTaskOrder (int selectedTaskNo, int beforeMovedTaskNo, int afterMovedTaskNo, int statusNo, String currentSpaceKey)
			throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int chk1 = 0;
		int chk2 = 0;
		int chk3 = 0;
		boolean isMoved = false;

		Class.forName(driver);
		conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "UPDATE task " + "SET task_order = task_order + 1 " + "WHERE status_no = ? "
				+ "AND task_order >= ? " + "AND task_order < ? " + "AND ? > ? AND space_key = ? ";

		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, statusNo);
		pstmt.setInt(2, beforeMovedTaskNo);
		pstmt.setInt(3, afterMovedTaskNo);
		pstmt.setInt(4, beforeMovedTaskNo);
		pstmt.setInt(5, afterMovedTaskNo);
		pstmt.setString(6, currentSpaceKey);
		chk1 = pstmt.executeUpdate();
		pstmt.close();

		sql = "UPDATE task " + " SET task_order = task_order - 1 " + " WHERE status_no = ? "
				+ " AND task_order <= ? " + " AND task_order > ? " + " AND ? < ? AND space_key = ?";

		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, statusNo);
		pstmt.setInt(2, afterMovedTaskNo);
		pstmt.setInt(3, beforeMovedTaskNo);
		pstmt.setInt(4, beforeMovedTaskNo);
		pstmt.setInt(5, afterMovedTaskNo);
		pstmt.setString(6, currentSpaceKey);
		chk2 = pstmt.executeUpdate();
		pstmt.close();

		sql = "UPDATE task" + " SET task_order = ? " + " WHERE task_no = ? AND space_key = ? ";

		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, afterMovedTaskNo);
		pstmt.setInt(2, selectedTaskNo);
		pstmt.setString(3, currentSpaceKey);
		chk3 = pstmt.executeUpdate();
		pstmt.close();

		if (chk1 != 0 && chk2 != 0 && chk3 != 0) {
			isMoved = true;
		}

		conn.close();

		return isMoved;
	}

	// 명세 4.4
	// Input : current_space_key, task_no
	// Output : -
	boolean deleteTask(String currentSpaceKey, int taskNo) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int chk = 0;
		boolean isDeleted = false;

		Class.forName(driver);
		conn = DriverManager.getConnection(url, dbId, dbPw);

		// delete reply first
		String sql = "DELETE FROM task " + "WHERE space_key = ? AND task_no = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, currentSpaceKey);
		pstmt.setInt(2, taskNo);
		chk = pstmt.executeUpdate();

		if (chk != 0) {
			isDeleted = true;
		}

		return isDeleted;
	}

	// 명세 4.6
	// Input : current_space_key
	// Output : List<TaskInfoDto>
	List<TaskInfoDto> showTaskList(String currentSpaceKey) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<TaskInfoDto> list = new ArrayList<>();

		Class.forName(driver);
		conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "SELECT space_key, task_no, creator_no, worker_no, task_title, "
				+ "    task_description, due_date, label_title, status_no, priority, "
				+ "    upper_task_no, task_order, summary " + "	  FROM task " + "   WHERE space_key = ? "
				+ "	  ORDER BY task_order ASC ";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, currentSpaceKey);

		rs = pstmt.executeQuery();

		while (rs.next()) {
			TaskInfoDto dto = new TaskInfoDto();
					dto.setSpaceKey(rs.getString("space_key"));
					dto.setTaskNo(rs.getInt("task_no"));
					dto.setCreatorNo(rs.getInt("creator_no"));
					dto.setWorkerNo(rs.getInt("worker_no"));
					dto.setTaskTitle(rs.getString("task_title"));
					dto.setTaskDescription(rs.getString("task_description"));
					dto.setDueDate(rs.getString("due_date"));
					dto.setLabelTitle(rs.getString("label_title"));
					dto.setStatusNo(rs.getString("status_no"));
					dto.setPriority(rs.getString("priority"));
					dto.setUpperTaskNo(rs.getObject("upper_task_no") != null ? rs.getInt("upper_task_no") : null);
					dto.setTaskOrder(rs.getInt("task_order"));
					dto.setSummary(rs.getString("summary"));
				list.add(dto);
		}

		rs.close();
		pstmt.close();
		conn.close();

		return list;
	}

	// 명세 4.7
	// Input : current_space_key, selected_task_no
	// Output : List<TaskInfoDto>
	List<TaskInfoDto> showUpperTaskList(String currentSpaceKey) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<TaskInfoDto> list = new ArrayList<>();

		Class.forName(driver);
		conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "SELECT space_key, task_no, creator_no, worker_no, task_title, "
				+ "    task_description, due_date, label_title, status_no, priority, "
				+ "    upper_task_no, task_order, summary " + "	  FROM task "
				+ "   WHERE space_key = ? AND upper_task_no IS NULL " + "	  ORDER BY task_order ASC ";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, currentSpaceKey);
		rs = pstmt.executeQuery();

		while (rs.next()) {
			TaskInfoDto dto = new TaskInfoDto();
					dto.setSpaceKey(rs.getString("space_key"));
					dto.setTaskNo(rs.getInt("task_no"));
					dto.setCreatorNo(rs.getInt("creator_no"));
					dto.setWorkerNo(rs.getInt("worker_no"));
					dto.setTaskTitle(rs.getString("task_title"));
					dto.setTaskDescription(rs.getString("task_description"));
					dto.setDueDate(rs.getString("due_date"));
					dto.setLabelTitle(rs.getString("label_title"));
					dto.setStatusNo(rs.getString("status_no"));
					dto.setPriority(rs.getString("priority"));
					dto.setUpperTaskNo(rs.getObject("upper_task_no") != null ? rs.getInt("upper_task_no") : null);
					dto.setTaskOrder(rs.getInt("task_order"));
					dto.setSummary(rs.getString("summary"));
				list.add(dto);
		}

		rs.close();
		pstmt.close();
		conn.close();

		return list;
	}

	// 명세 4.8
	// Input :
	// Output :
	List<TaskInfoDto> showLowerTaskList(String currentSpaceKey, int selectedTaskNo) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<TaskInfoDto> list = new ArrayList<>();

		Class.forName(driver);
		conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "SELECT space_key, task_no, creator_no, worker_no, task_title, "
				+ "    task_description, due_date, label_title, status_no, priority, "
				+ "    upper_task_no, task_order, summary " + "	  FROM task "
				+ "   WHERE space_key = ? AND upper_task_no = ? " + "   ORDER BY task_order ASC ";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, currentSpaceKey);
		pstmt.setInt(2, selectedTaskNo);
		rs = pstmt.executeQuery();

		while (rs.next()) {
			TaskInfoDto dto = new TaskInfoDto();
					dto.setSpaceKey(rs.getString("space_key"));
					dto.setTaskNo(rs.getInt("task_no"));
					dto.setCreatorNo(rs.getInt("creator_no"));
					dto.setWorkerNo(rs.getInt("worker_no"));
					dto.setTaskTitle(rs.getString("task_title"));
					dto.setTaskDescription(rs.getString("task_description"));
					dto.setDueDate(rs.getString("due_date"));
					dto.setLabelTitle(rs.getString("label_title"));
					dto.setStatusNo(rs.getString("status_no"));
					dto.setPriority(rs.getString("priority"));
					dto.setUpperTaskNo(rs.getObject("upper_task_no") != null ? rs.getInt("upper_task_no") : null);
					dto.setTaskOrder(rs.getInt("task_order"));
					dto.setSummary(rs.getString("summary"));
				list.add(dto);
		}

		rs.close();
		pstmt.close();
		conn.close();

		return list;
	}

	public static void main(String[] args) throws Exception {
		TaskDao dao = new TaskDao();

		// 4.1 Create Task (clear!)
//		System.out.println(dao.createTask("ABCD", 2, 5, "집가기", "지하철타고 가기", "2026-06-06", null, 1, "Low", null, null));

		// 4.2 Search Task By SearchCondition (clear!)
//		List<TaskInfoDto> filteredTask = dao.SearchTaskBySearchCondition(2, "담배", null, 2, null, null, null, "20260602", "!=");
//		if(filteredTask.isEmpty()) {
//			System.out.println("No Task Here...");
//		}
//		else {
//			for (TaskInfoDto dto : filteredTask) {
//				String spaceKey = dto.getSpaceKey();
//				int taskNo = dto.getTaskNo();
//				int creatorNo = dto.getCreatorNo();
//				int workerNo = dto.getWorkerNo();
//				String taskTitle = dto.getTaskTitle();
//				String taskDescription = dto.getTaskDescription();
//				String dueDate = dto.getDueDate();
//				String labelTitle = dto.getLabelTitle();
//				String statusNo = dto.getStatusNo();
//				String priority = dto.getPriority();
//				Integer upperTaskNo = dto.getUpperTaskNo();
//				int taskOrder = dto.getTaskOrder();
//				int imageNo = dto.getImageNo();
//				String summary = dto.getSummary();
//				System.out.println(spaceKey + ", " + taskNo + ", " + creatorNo 
//									+ ", " + workerNo + ", " + taskTitle + ", " + taskDescription + ", " + dueDate 
//									+ ", " + labelTitle + ", " + statusNo + ", " + priority + ", " + upperTaskNo 
//									+ ", " + taskOrder + ", " + imageNo + ", " + summary);
//				}
		// 4.3 Update Task
		// Update Task (clear!)
//		System.out.println(dao.updateTask("ABCD", 9, "수정된 제목", "수정된 내용", null, null, null, null, null));
		
		// Update Task Order (fail!)
//		System.out.println(dao.updateTaskOrder(3, 1, 2, 1, "ABCD"));
		
		// 4.4 Delete Task (clear!)
//		System.out.println(dao.deleteTask("ABCD", 9));
		
		// 4.6 Show Task List (clear!)
//		List<TaskInfoDto> list = dao.showTaskList("ABCD");
//		if(list.isEmpty()) {
//			System.out.println("No Task Yet...");
//		}
//		else {
//			for(TaskInfoDto dto : list) {
//				String spaceKey = dto.getSpaceKey();
//				int taskNo = dto.getTaskNo();
//				int creatorNo = dto.getCreatorNo();
//				int workerNo = dto.getWorkerNo();
//				String taskTitle = dto.getTaskTitle();
//				String taskDescription = dto.getTaskDescription();
//				String dueDate = dto.getDueDate();
//				String labelTitle = dto.getLabelTitle();
//				String statusNo = dto.getStatusNo();
//				String priority = dto.getPriority();
//				Integer upperTaskNo = dto.getUpperTaskNo();
//				int taskOrder = dto.getTaskOrder();
//				int imageNo = dto.getImageNo();
//				String summary = dto.getSummary();
//				System.out.println(spaceKey + ", " + taskNo + ", " + creatorNo 
//									+ ", " + workerNo + ", " + taskTitle + ", " + taskDescription + ", " + dueDate 
//									+ ", " + labelTitle + ", " + statusNo + ", " + priority + ", " + upperTaskNo 
//									+ ", " + taskOrder + ", " + imageNo + ", " + summary);
//			}
//		}
		
		// 4.7 Show Upper Task Only (clear!)
//		List<TaskInfoDto> list = dao.showUpperTaskList("ABCD");
//		if(list.isEmpty()) {
//			System.out.println("No Task Yet...");
//		}
//		else {
//			for(TaskInfoDto dto : list) {
//				String spaceKey = dto.getSpaceKey();
//				int taskNo = dto.getTaskNo();
//				int creatorNo = dto.getCreatorNo();
//				int workerNo = dto.getWorkerNo();
//				String taskTitle = dto.getTaskTitle();
//				String taskDescription = dto.getTaskDescription();
//				String dueDate = dto.getDueDate();
//				String labelTitle = dto.getLabelTitle();
//				String statusNo = dto.getStatusNo();
//				String priority = dto.getPriority();
//				Integer upperTaskNo = dto.getUpperTaskNo();
//				int taskOrder = dto.getTaskOrder();
//				int imageNo = dto.getImageNo();
//				String summary = dto.getSummary();
//				System.out.println(spaceKey + ", " + taskNo + ", " + creatorNo 
//									+ ", " + workerNo + ", " + taskTitle + ", " + taskDescription + ", " + dueDate 
//									+ ", " + labelTitle + ", " + statusNo + ", " + priority + ", " + upperTaskNo 
//									+ ", " + taskOrder + ", " + imageNo + ", " + summary);
//			}
//		}
		
		// 4.8 Show Lower Task List (clear!)
//		List<TaskInfoDto> list = dao.showLowerTaskList("ABCD", 7);
//		if(list.isEmpty()) {
//			System.out.println("No Task Yet...");
//		}
//		else {
//			for(TaskInfoDto dto : list) {
//				String spaceKey = dto.getSpaceKey();
//				int taskNo = dto.getTaskNo();
//				int creatorNo = dto.getCreatorNo();
//				int workerNo = dto.getWorkerNo();
//				String taskTitle = dto.getTaskTitle();
//				String taskDescription = dto.getTaskDescription();
//				String dueDate = dto.getDueDate();
//				String labelTitle = dto.getLabelTitle();
//				String statusNo = dto.getStatusNo();
//				String priority = dto.getPriority();
//				Integer upperTaskNo = dto.getUpperTaskNo();
//				int taskOrder = dto.getTaskOrder();
//				int imageNo = dto.getImageNo();
//				String summary = dto.getSummary();
//				System.out.println(spaceKey + ", " + taskNo + ", " + creatorNo 
//									+ ", " + workerNo + ", " + taskTitle + ", " + taskDescription + ", " + dueDate 
//									+ ", " + labelTitle + ", " + statusNo + ", " + priority + ", " + upperTaskNo 
//									+ ", " + taskOrder + ", " + imageNo + ", " + summary);
//			}
//		}
	}
}
