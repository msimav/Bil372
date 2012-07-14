package beans;

public class User {

	private int id;
	private String name;
	private String email;
	private String passwd;
	//private String avatar; TODO: avatar'in nasil handle edilecegi sonradan kararlastirilacak

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

}
