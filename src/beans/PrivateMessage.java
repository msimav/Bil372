package beans;

public class PrivateMessage {
	private int id;
	private User to;
	private User from;
	private String date;
	private String message;
	
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
