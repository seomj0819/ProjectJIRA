package image;

public class ImageDto {
	private int image_no;
	private String image_title;
	private String image_category;

	ImageDto(int image_no, String image_title, String image_category) {
		this.setImage_no(image_no);
		this.setImage_title(image_title);
		this.setImage_category(image_category);
	}

	public int getImage_no() {
		return image_no;
	}

	public void setImage_no(int image_no) {
		this.image_no = image_no;
	}

	public String getImage_title() {
		return image_title;
	}

	public void setImage_title(String image_title) {
		this.image_title = image_title;
	}

	public String getImage_category() {
		return image_category;
	}

	public void setImage_category(String image_category) {
		this.image_category = image_category;
	}

	ImageDto() {
	}

	@Override
	public String toString() {
		return "ImageDto [image_no = " + image_no + ", image_title = " + image_title + ", image_category = "
				+ image_category + "]";
	}

	public static void main(String[] args) {

	}
}
