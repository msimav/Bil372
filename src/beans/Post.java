package beans;

public class Post {

	private int id;
	private User user;
	private Topic topic;
	private String Date;
	private String post;
	private Post reply;
	
	public int getId() {
		return id;
	}
	
	public User getUser() {
		return user;
	}
	
	public Topic getTopic() {
		return topic;
	}
	
	public String getDate() {
		return Date;
	}
	
	public String getPost() {
		return post;
	}
	
	public Post getReply() {
		return reply;
	}
}
