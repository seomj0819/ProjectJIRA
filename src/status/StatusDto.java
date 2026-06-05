package status;

public class StatusDto {
	private int status_no;
	private String space_key;
	private int status_order;
	private String status_title;
	private String status_color;
	private String color_code;
	
	StatusDto(int status_no, String space_key, int status_order, String status_title, String status_color) {
		this.status_no = status_no;
		this.space_key = space_key;
		this.status_order = status_order;
		this.status_title = status_title;
		this.status_color = status_color;
	}
	
	public int getStatusNo() {
		return status_no;
	}
	public void setStatusNo(int status_no) {
		this.status_no = status_no;
	}
	public String getSpaceKey() {
		return space_key;
	}
	public void setSpaceKey(String space_key) {
		this.space_key = space_key;
	}
	public int getStatusOrder() {
		return status_order;
	}
	public void setStatusOrder(int status_order) {
		this.status_order = status_order;
	}
	public String getStatusTitle() {
		return status_title;
	}
	public void setStatusTitle(String status_title) {
		this.status_title = status_title;
	}
	public String getStatusColor() {
		return status_color;
	}
	public void setStatusColor(String status_color) {
		this.status_color = status_color;
	}
	public String getColorCode() {
		return color_code;
	}
	public void setColorCode(String color_code) {
		this.color_code = color_code;
	}

	StatusDto(){}
	
	@Override
	public String toString() {
		return "StatusDto [status_no = " + status_no + ", space_key = " + space_key + ", status_order = "+ status_order + ", status_title = " + status_title +", status_color = " + status_color + "]";
	}
	public static void main(String[] args) {

	}
}
