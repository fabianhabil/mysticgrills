package mysticgrills;

public class GlobalState {
    
    private static GlobalState instance;
    
    private String role, name, email, password;
    private Integer id;

    public GlobalState() {
    }
    
    public static GlobalState getInstance() {
        if(instance == null) {
        	instance = new GlobalState();
            return instance;
        }
        return instance;
    }
    
    public void addUser(Integer id, String role, String name, String email, String password) {
        this.id = id;
        this.role = role;
        this.name = name;
        this.email = email;
        this.password = password;
    }

	public String getRole() {
		return role;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public Integer getId() {
		return id;
	}
	
	public void removeUser() {
		this.id = null;
		this.role = null;
		this.name = null;
		this.email = null;
		this.password = null;
		System.out.println("user removed");
	}
    
}