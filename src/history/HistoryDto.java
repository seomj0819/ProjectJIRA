package history;

public class HistoryDto {
	private int history_no;
	private String space_key;
	private int task_no;
	private int reply_no;
	private int user_no;
	private String field_name;
	private String action_type;
	private String created_at;
	private String old_value;
	private String new_value;

	HistoryDto(int history_no, String space_key, int task_no, int reply_no, int user_no, String field_name,
			String action_type, String created_at, String old_value, String new_value) {
		this.setHistory_no(history_no);
		this.setSpace_key(space_key);
		this.setTask_no(task_no);
		this.setReply_no(reply_no);
		this.setUser_no(user_no);
		this.setField_name(field_name);
		this.setAction_type(action_type);
		this.setCreated_at(created_at);
		this.setOld_value(old_value);
		this.setNew_value(new_value);
	}

	public int getHistory_no() {
		return history_no;
	}

	public void setHistory_no(int history_no) {
		this.history_no = history_no;
	}

	public String getSpace_key() {
		return space_key;
	}

	public void setSpace_key(String space_key) {
		this.space_key = space_key;
	}

	public int getTask_no() {
		return task_no;
	}

	public void setTask_no(int task_no) {
		this.task_no = task_no;
	}

	public int getReply_no() {
		return reply_no;
	}

	public void setReply_no(int reply_no) {
		this.reply_no = reply_no;
	}

	public int getUser_no() {
		return user_no;
	}

	public void setUser_no(int user_no) {
		this.user_no = user_no;
	}

	public String getField_name() {
		return field_name;
	}

	public void setField_name(String field_name) {
		this.field_name = field_name;
	}

	public String getAction_type() {
		return action_type;
	}

	public void setAction_type(String action_type) {
		this.action_type = action_type;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getOld_value() {
		return old_value;
	}

	public void setOld_value(String old_value) {
		this.old_value = old_value;
	}

	public String getNew_value() {
		return new_value;
	}

	public void setNew_value(String new_value) {
		this.new_value = new_value;
	}

	HistoryDto() {
	}

	@Override
	public String toString() {
		return "HistoryDto [history_no = " + history_no + ", space_key = " + space_key + ", task_no = " + task_no
				+ ", reply_no = " + reply_no + ", user_no = " + user_no + ", field_name = " + field_name
				+ ", action_type = " + action_type + ", created_at = " + created_at + ", old_value = " + old_value
				+ ", new_value = " + new_value + "]";
	}

	public static void main(String[] args) {
		
	}
}
