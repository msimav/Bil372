package beans;

public class User {

	private int id;
	private String name;
	private String email;
	private String passwd;
	private byte[] avatar;

	public User(int id, String name, String email, String passwd, byte[] avatar) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.passwd = passwd;
		this.avatar = avatar;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPasswd() {
		return passwd;
	}

	public byte[] getAvatar() {
		return avatar;
	}
}
