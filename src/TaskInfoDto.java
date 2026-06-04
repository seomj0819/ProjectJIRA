public class TaskInfoDto {
	private String spaceKey;
	private int taskNo;
	private int creatorNo;
	private int workerNo;
	private String taskTitle;
	private String taskDescription;
	private String dueDate;
	private String labelTitle;
	private String statusNo;
	private String priority;
	private Integer upperTaskNo;
	private int taskOrder;
	private int imageNo;
	private String summary; 
	
	public TaskInfoDto(String spaceKey, int taskNo, int creatorNo, int workerNo, String taskTitle,
			String taskDescription, String dueDate, String labelTitle, String statusNo, String priority,
			Integer upperTaskNo, int taskOrder, int imageNo, String summary) {
		super();
		this.spaceKey = spaceKey;
		this.taskNo = taskNo;
		this.creatorNo = creatorNo;
		this.workerNo = workerNo;
		this.taskTitle = taskTitle;
		this.taskDescription = taskDescription;
		this.dueDate = dueDate;
		this.labelTitle = labelTitle;
		this.statusNo = statusNo;
		this.priority = priority;
		this.upperTaskNo = upperTaskNo;
		this.taskOrder = taskOrder;
		this.imageNo = imageNo;
		this.summary = summary;
	}
	
	public String getSpaceKey() { 
		return spaceKey; 
	}
	
	public void setSpaceKey(String spaceKey) { 
		this.spaceKey = spaceKey; 
	}
	
	public int getTaskNo() { 
		return taskNo;
	}
	
	public void setTaskNo(int taskNo) { 
		this.taskNo = taskNo; 
	}
	
	public int getCreatorNo() { 
		return creatorNo; 
	}
	
	public void setCreatorNo(int creatorNo) { 
		this.creatorNo = creatorNo; 
	}
	
	public int getWorkerNo() { 
		return workerNo; 
	}
	
	public void setWorkerNo(int workerNo) { 
		this.workerNo = workerNo; 
	}
	
	public String getTaskTitle() { 
		return taskTitle; 
	}
	
	public void setTaskTitle(String taskTitle) { 
		this.taskTitle = taskTitle; 
	}
	
	public String getTaskDescription() { 
		return taskDescription; 
	}
	
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription; 
	}
	
	public String getDueDate() { 
		return dueDate; 
	}
	
	public void setDueDate(String dueDate) { 
		this.dueDate = dueDate; 
	}
	
	public String getLabelTitle() { 
		return labelTitle; 
	}
	
	public void setLabelTitle(String labelTitle) {
		this.labelTitle = labelTitle; 
	}
	
	public String getStatusNo() { 
		return statusNo; 
	}
	
	public void setStatusNo(String statusNo) { 
		this.statusNo = statusNo; 
	}
	
	public String getPriority() { 
		return priority; 
	}
	
	public void setPriority(String priority) { 
		this.priority = priority; 
	}
	
	public Integer getUpperTaskNo() { 
		return upperTaskNo; 
	}
	
	public void setUpperTaskNo(Integer upperTaskNo) { 
		this.upperTaskNo = upperTaskNo; 
	}
	
	public int getTaskOrder() { 
		return taskOrder; 
	}
	
	public void setTaskOrder(int taskOrder) { 
		this.taskOrder = taskOrder; 
	}
	
	public int getImageNo() { 
		return imageNo;
	}
	
	public void setImageNo(int imageNo) {
		this.imageNo = imageNo; 
	}
	
	public String getSummary() { 
		return summary; 
	}
	
	public void setSummary(String summary) { 
		this.summary = summary; 
	}
	
	public TaskInfoDto() {};

	@Override
	public String toString() {
		return "TaskInfoDto [spaceKey=" + spaceKey + ", taskNo=" + taskNo + ", creatorNo=" + creatorNo + ", workerNo="
				+ workerNo + ", taskTitle=" + taskTitle + ", taskDescription=" + taskDescription + ", dueDate="
				+ dueDate + ", labelTitle=" + labelTitle + ", statusNo=" + statusNo + ", priority=" + priority
				+ ", upperTaskNo=" + upperTaskNo + ", taskOrder=" + taskOrder + ", imageNo=" + imageNo + ", summary="
				+ summary + "]";
	}
}