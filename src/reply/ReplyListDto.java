package reply;

public class ReplyListDto {
	private int reply_no;
	private String space_key;
	private int task_no;
	private int writer_no;
	private String reply_content;
	private int image_no;
	private String created_at;
	private String task_id;
	
	ReplyListDto(int reply_no, String space_key, int task_no, int writer_no, String reply_content, int image_no, String created_at, String task_id) {
		this.reply_no = reply_no;
		this.space_key = space_key;
		this.task_no = task_no;
		this.writer_no = writer_no;
		this.reply_content = reply_content;
		this.image_no = image_no;
		this.created_at = created_at;
		this.task_id = task_id;
	}
	
	public int getReplyNo() {
		return reply_no;
	}


	public void setReplyNo(int reply_no) {
		this.reply_no = reply_no;
	}


	public String getSpaceKey() {
		return space_key;
	}


	public void setSpaceKey(String space_key) {
		this.space_key = space_key;
	}


	public int getTaskNo() {
		return task_no;
	}


	public void setTaskNo(int task_no) {
		this.task_no = task_no;
	}


	public int getWriter_no() {
		return writer_no;
	}


	public void setWriterNo(int writer_no) {
		this.writer_no = writer_no;
	}


	public String getReplyContent() {
		return reply_content;
	}


	public void setReplyContent(String reply_content) {
		this.reply_content = reply_content;
	}


	public int getImageNo() {
		return image_no;
	}


	public void setImageNo(int image_no) {
		this.image_no = image_no;
	}


	public String getCreatedAt() {
		return created_at;
	}


	public void setCreatedAt(String created_at) {
		this.created_at = created_at;
	}


	public String getTaskId() {
		return task_id;
	}


	public void setTaskId(String task_id) {
		this.task_id = task_id;
	}
	
	ReplyListDto(){}
	
	@Override
	public String toString() {
		return "ReplyListDto [reply_no = "+ reply_no +", space_key = "+ space_key +", task_no = "+ task_no +", writer_no = "+ writer_no +", reply_content = "+ reply_content + ", image_no = "+ image_no +", created_at = "+ created_at +", task_id = "+ task_id + "]";
	}														

	public static void main(String[] args) {
		
	}
}
