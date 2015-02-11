package group.three;

import blackboard.data.user.User;

public class UserBB {

	private User currentUser;
	
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
	
	@Override
	public String toString() {
		return "User [username=" + getUserName() + ", name=" + getName() + "]";
	}
	
	public String getUserName() {
		return currentUser.getUserName();
	}
	
	public String getStudentID() {
		return currentUser.getStudentId();
	}
	
	public String getName() {
		return currentUser.getGivenName() + " " + currentUser.getFamilyName();
	}
	
	public String getID() {
		return currentUser.getId().toString();
	}
}