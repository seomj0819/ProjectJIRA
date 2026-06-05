package alarnchk;

public class AlarmChkDto {
	private int history_no;
	private int user_no;

	AlarmChkDto(int history_no, int user_no) {
		this.setHistory_no(history_no);
		this.setUser_no(user_no);
	}

	public int getHistory_no() {
		return history_no;
	}

	public void setHistory_no(int history_no) {
		this.history_no = history_no;
	}

	public int getUser_no() {
		return user_no;
	}

	public void setUser_no(int user_no) {
		this.user_no = user_no;
	}

	AlarmChkDto() {
	}

	@Override
	public String toString() {
		return "AlarmChkDto [history_no = " + history_no + ", user_no = " + user_no + "]";
	}

	public static void main(String[] args) {

	}
}
