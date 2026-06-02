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
	
	private TaskInfoDto(Builder builder) {
		this.spaceKey = builder.spaceKey;
		this.taskNo = builder.taskNo;
		this.creatorNo = builder.creatorNo;
		this.workerNo = builder.workerNo;
		this.taskTitle = builder.taskTitle;
		this.taskDescription = builder.taskDescription;
		this.dueDate = builder.dueDate;
		this.labelTitle = builder.labelTitle;
		this.statusNo = builder.statusNo;
		this.priority = builder.priority;
		this.upperTaskNo = builder.upperTaskNo;
		this.taskOrder = builder.taskOrder;
		this.imageNo = builder.imageNo;
		this.summary = builder.summary;
	}

	public static class Builder {
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
		private int imageNo = 0;
		private String summary = null;

		public Builder spaceKey(String spaceKey) {this.spaceKey = spaceKey; return this;}
		public Builder taskNo(int taskNo) { this.taskNo = taskNo; return this; }
		public Builder creatorNo(int creatorNo) { this.creatorNo = creatorNo; return this; }
		public Builder workerNo(int workerNo) { this.workerNo = workerNo; return this; }
		public Builder taskTitle(String taskTitle) { this.taskTitle = taskTitle; return this; }
		public Builder taskDescription(String taskDescription) { this.taskDescription = taskDescription; return this; }
		public Builder dueDate(String dueDate) { this.dueDate = dueDate; return this; }
		public Builder labelTitle(String labelTitle) { this.labelTitle = labelTitle; return this; }
		public Builder statusNo(String statusNo) { this.statusNo = statusNo; return this; }
		public Builder priority(String priority) { this.priority = priority; return this; }
		public Builder upperTaskNo(Integer upperTaskNo) { this.upperTaskNo = upperTaskNo; return this; }
		public Builder taskOrder(int taskOrder) { this.taskOrder = taskOrder; return this; }
		public Builder imageNo(int imageNo) { this.imageNo = imageNo; return this; }
		public Builder summary(String summary) { this.summary = summary; return this; }

		public TaskInfoDto build() {
			return new TaskInfoDto(this);
		}
	}

	public String getSpaceKey() { return spaceKey; }
	public void setSpaceKey(String spaceKey) { this.spaceKey = spaceKey; }
	public int getTaskNo() { return taskNo; }
	public void setTaskNo(int taskNo) { this.taskNo = taskNo; }
	public int getCreatorNo() { return creatorNo; }
	public void setCreatorNo(int creatorNo) { this.creatorNo = creatorNo; } // 대문자 C 수정
	public int getWorkerNo() { return workerNo; }
	public void setWorkerNo(int workerNo) { this.workerNo = workerNo; }
	public String getTaskTitle() { return taskTitle; }
	public void setTaskTitle(String taskTitle) { this.taskTitle = taskTitle; }
	public String getTaskDescription() { return taskDescription; }
	public void setTaskDescription(String taskDescription) { this.taskDescription = taskDescription; }
	public String getDueDate() { return dueDate; }
	public void setDueDate(String dueDate) { this.dueDate = dueDate; }
	public String getLabelTitle() { return labelTitle; }
	public void setLabelTitle(String labelTitle) { this.labelTitle = labelTitle; }
	public String getStatusNo() { return statusNo; }
	public void setStatusNo(String statusNo) { this.statusNo = statusNo; }
	public String getPriority() { return priority; }
	public void setPriority(String priority) { this.priority = priority; }
	public Integer getUpperTaskNo() { return upperTaskNo; }
	public void setUpperTaskNo(Integer upperTaskNo) { this.upperTaskNo = upperTaskNo; }
	public int getTaskOrder() { return taskOrder; }
	public void setTaskOrder(int taskOrder) { this.taskOrder = taskOrder; }
	public int getImageNo() { return imageNo; }
	public void setImageNo(int imageNo) { this.imageNo = imageNo; }
	public String getSummary() { return summary; }
	public void setSummary(String summary) { this.summary = summary; }

	@Override
	public String toString() {
		return "TaskInfoDto [spaceKey=" + spaceKey + ", taskNo=" + taskNo + ", creatorNo=" + creatorNo + ", workerNo="
				+ workerNo + ", taskTitle=" + taskTitle + ", taskDescription=" + taskDescription + ", dueDate="
				+ dueDate + ", labelTitle=" + labelTitle + ", statusNo=" + statusNo + ", priority=" + priority
				+ ", upperTaskNo=" + upperTaskNo + ", taskOrder=" + taskOrder + ", imageNo=" + imageNo + ", summary="
				+ summary + "]";
	}
}