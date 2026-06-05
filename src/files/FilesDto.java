package files;

public class FilesDto {
	private int file_no;
	private String space_key;
	private int task_no;
	private int reply_no;
	private String file_name;
	private String file_path;
	private String created_at;
	
	FilesDto(int file_no, String space_key, int task_no, int reply_no, String file_name, String file_path, String created_at) {
		this.setFile_no(file_no);
		this.setSpace_key(space_key);
		this.setTask_no(task_no);
		this.setReply_no(reply_no);
		this.setFile_name(file_name);
		this.setFile_path(file_path);
		this.setCreated_at(created_at);
	}
	
	public int getFile_no() {
		return file_no;
	}

	public void setFile_no(int file_no) {
		this.file_no = file_no;
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

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getFile_path() {
		return file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	
	FilesDto(){}
	
	@Override
	public String toString() {
		return "FilesDto [file_no = "+file_no+", space_key = "+space_key+", task_no = "+task_no+", reply_no = "+reply_no+", file_name = "+file_name+", file_path  = "+file_path+", created_at = "+created_at+"]";
	}

	public static void main(String[] args) {

	}
}
