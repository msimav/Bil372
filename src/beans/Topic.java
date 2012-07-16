package beans;

public class Topic {

	private int id;
	private User user;
	private String date;
	private String title;
	private Tag[] tags;
	private Post firstPost;

	public Topic(int id, User user, String date, String title, Tag[] tags, Post firstPost) {
		this.id = id;
		this.user = user;
		this.date = date;
		this.title = title;
		this.tags = tags;
		this.firstPost = firstPost;
	}

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

	public Post getFirstPost() {
		return firstPost;
	}
}
