package beans;

public class Topic {
	
	private int id;
	private User user;
	private String date;
	private String title;
	private Tag[] tags;
	
	public int getId() {
		return id;
	}
	
	public User getUser() {
		return user;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDate() {
		return date;
	}
	
	public Tag[] getTags() {
		return tags;
	}
}
