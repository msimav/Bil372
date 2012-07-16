package beans;

public class PrivateMessage {
	private int id;
	private User to;
	private User from;
	private String date;
	private String message;

	public PrivateMessage(int id, User to, User from, String date, String message) {
		this.id = id;
		this.to = to;
		this.from = from;
		this.date = date;
		this.message = message;
	}

	public int getId() {
		return id;
	}

	public User getTo() {
		return to;
	}

	public User getFrom() {
		return from;
	}

	public String getDate() {
		return date;
	}

	public String getMessage() {
		return message;
	}
}
