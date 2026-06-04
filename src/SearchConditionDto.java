
public class SearchConditionDto {
	private String searchConditionTitle;
	private int workerNo;
	private int creatorNo;
	private int statusNo;
	private String priority;
	private String labelTitle;
	private String spaceKey;
	private String dueDate;

	public SearchConditionDto(String searchConditionTitle, int workerNo, int creatorNo, int statusNo, String priority,
			String labelTitle, String spaceKey, String dueDate) {
		super();
		this.searchConditionTitle = searchConditionTitle;
		this.workerNo = workerNo;
		this.creatorNo = creatorNo;
		this.statusNo = statusNo;
		this.priority = priority;
		this.labelTitle = labelTitle;
		this.spaceKey = spaceKey;
		this.dueDate = dueDate;
	}

	public String getSearchConditionTitle() {
		return searchConditionTitle;
	}

	public void setSearchConditionTitle(String searchConditionTitle) {
		this.searchConditionTitle = searchConditionTitle;
	}

	public int getWorkerNo() {
		return workerNo;
	}

	public void setWorkerNo(int workerNo) {
		this.workerNo = workerNo;
	}

	public int getCreatorNo() {
		return creatorNo;
	}

	public void setCreatorNo(int creatorNo) {
		this.creatorNo = creatorNo;
	}

	public int getStatusNo() {
		return statusNo;
	}

	public void setStatusNo(int statusNo) {
		this.statusNo = statusNo;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getLabelTitle() {
		return labelTitle;
	}

	public void setLabelTitle(String labelTitle) {
		this.labelTitle = labelTitle;
	}

	public String getSpaceKey() {
		return spaceKey;
	}

	public void setSpaceKey(String spaceKey) {
		this.spaceKey = spaceKey;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	
	@Override
	public String toString() {
		return "SearchConditionDto [searchConditionTitle = " + searchConditionTitle 
				+ ", workerNo = " + workerNo + ", creatorNo = " + creatorNo + ", statusNo = " + statusNo 
				+ ", priority = " + priority + ", labelTitle = " + labelTitle + ", spaceKey = " + spaceKey 
				+ ", dueDate = " + dueDate + "]";
	}

}
