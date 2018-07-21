package OS_Project;

public class User_Message {
	private String username;
	private int userId;
	private String password;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public User_Message(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
}
