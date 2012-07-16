package beans;

public class Post {

	private int id;
	private User user;
	private Topic topic;
	private String date;
	private String post;
	private Post reply;

	public Post(int id, User user, Topic topic, String date, String post, Post reply) {
		this.id = id;
		this.user = user;
		this.topic = topic;
		this.date = date;
		this.post = post;
		this.reply = reply;
	}

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
		return date;
	}

	public String getPost() {
		return post;
	}

	public Post getReply() {
		return reply;
	}
}
