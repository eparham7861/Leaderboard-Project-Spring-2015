package group.three;

import blackboard.platform.context.Context;
import blackboard.data.user.User;
import blackboard.persist.*;
import javax.servlet.http.HttpServletRequest;

public class LeaderboardBB {
	private UserBB currentUser;
	private CourseIDBB currentCourseID;
	private Context currentContext;
	
	public LeaderboardBB() {
		currentUser = new UserBB();
		currentCourseID = new CourseIDBB();
	}
	public void configureBoard(Context currentContext) {
		this.currentContext = currentContext;
		setCurrentUser();
		setCurrentCourseID();
	}
	
	private void setCurrentUser() {
		currentUser.setCurrentUser(currentContext.getUser());
	}
	
	private void setCurrentCourseID() {
		currentCourseID.setCourse(currentContext.getCourseId());
	}
}