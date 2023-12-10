package mysticgrills;

import mysticgrills.model.User;

public class GlobalState {

	private static GlobalState instance;

	private User currentLoggedInUser;

	public GlobalState() {
	}

	public static GlobalState getInstance() {
		if (instance == null) {
			instance = new GlobalState();
			return instance;
		}
		return instance;
	}

	public void removeUser() {
		currentLoggedInUser = null;
		System.out.println("user removed");
	}

	public void addUser(User user) {
		currentLoggedInUser = user;
	}

	public User getCurrentLoggedInUser() {
		return currentLoggedInUser;
	}

}