
public class UserProfileDto {
	private int userNo;
	private String userName;
	private String email;
	private String imageTitle;
	
	public UserProfileDto(int userNo, String userName, String email, String imageTitle) {
		this.userNo = userNo;
		this.userName = userName;
		this.email = email;
		this.imageTitle = imageTitle;
	}
	
	public int getUserNo() {return userNo;}
	public void setUserNo(int userNo) {this.userNo = userNo;}
	public String getUserName() {return userName;}
	public void setUserName(String userName) {this.userName = userName;}
	public String getEmail() {return email;}
	public void setEmail(String email) {this.email = email;}
	public String getImageTitle() {return imageTitle;}
	public void setImageTitle(String imageTitle) {this.imageTitle = imageTitle;}
	public UserProfileDto(){}
	
	@Override
	public String toString() {
		return "UserProfileDto [userNo = " + userNo + ", userName = " + userName + 
				", email = " + email + ", imageTitle = " + imageTitle + "]";
	}
}
