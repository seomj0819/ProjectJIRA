package space;

public class SpaceListDto {
	private String space_key;
	private String space_title;
	private int space_order;
	private String space_status;
	private int image_no;
	
	public SpaceListDto(String space_key, String space_title, int space_order, String space_status, int image_no) {
		this.space_key = space_key;
		this.space_title = space_title;
		this.space_order = space_order;
		this.space_status = space_status;
		this.image_no = image_no;
	}
	
	public String getSpaceKey() {
		return space_key;
	}
	public void setSpaceKey(String space_key) {
		this.space_key = space_key;
	}
	
	public String getSpaceTitle() {
		return space_title;
	}
	public void setSpaceTitle(String space_title) {
		this.space_title = space_title;
	}
	
	public int getSpaceOrder() {
		return space_order;
	}
	public void setSpaceOrder(int space_order) {
		this.space_order = space_order;
	}
	
	public String getSpaceStatus() {
		return space_status;
	}
	public void setSpaceStatus(String space_status) {
		this.space_status = space_status;
	}
	
	public int getImageNo() {
		return image_no;
	}
	public void setImageNo(int image_no) {
		this.image_no = image_no;
	}
	
	public SpaceListDto() {}
	@Override
	public String toString() {
		return "SpaceListDto [space_key = "+ space_key +", space_title = "+ space_title +", space_order = "+ space_order +", space_status = "+ space_status +", image_no = "+ image_no + "]";
	}
	
	public static void main(String[] args) { 
		
	}
}
