package SearchCondition;

public class SearchConditionDto {
	private String searchConditionTitle;
	private Integer workerNo;
	private Integer creatorNo;
	private Integer statusNo;
	private String priority;
	private String spaceKey;
	private String dueDate;

	public SearchConditionDto(String searchConditionTitle, Integer workerNo, Integer creatorNo, Integer statusNo, String priority,
			String labelTitle, String spaceKey, String dueDate) {
		super();
		this.searchConditionTitle = searchConditionTitle;
		this.workerNo = workerNo;
		this.creatorNo = creatorNo;
		this.statusNo = statusNo;
		this.priority = priority;
		this.spaceKey = spaceKey;
		this.dueDate = dueDate;
	}

	public String getSearchConditionTitle() {
		return searchConditionTitle;
	}

	public void setSearchConditionTitle(String searchConditionTitle) {
		this.searchConditionTitle = searchConditionTitle;
	}

	public Integer getWorkerNo() {
		return workerNo;
	}

	public void setWorkerNo(Integer workerNo) {
		this.workerNo = workerNo;
	}

	public Integer getCreatorNo() {
		return creatorNo;
	}

	public void setCreatorNo(Integer creatorNo) {
		this.creatorNo = creatorNo;
	}

	public Integer getStatusNo() {
		return statusNo;
	}

	public void setStatusNo(Integer statusNo) {
		this.statusNo = statusNo;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
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
	
	public SearchConditionDto() {}
	
	@Override
	public String toString() {
		return "SearchConditionDto [searchConditionTitle = " + searchConditionTitle 
				+ ", workerNo = " + workerNo + ", creatorNo = " + creatorNo + ", statusNo = " + statusNo 
				+ ", priority = " + priority + ", spaceKey = " + spaceKey 
				+ ", dueDate = " + dueDate + "]";
	}

}
