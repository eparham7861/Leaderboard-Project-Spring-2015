package group.three;

import blackboard.platform.context.Context;
import blackboard.data.user.User;
import blackboard.persist.*;
import javax.servlet.http.HttpServletRequest;

public class LeaderboardBB {
	private UserBB currentUser;
	private String sessionUserRole, sessionUserID;
	private CourseIDBB currentCourseID;
	private Context currentContext;
	private GradebookManagerBB currentGradebook;
	
	public LeaderboardBB() {
		sessionUserRole = "";
		sessionUserID = "";
		currentUser = new UserBB();
		currentCourseID = new CourseIDBB();
		currentGradebook = new GradebookManagerBB();
	}
	
	public void configureBoard(Context currentContext) {
		this.currentContext = currentContext;
		setCurrentUser();
		setCurrentCourseID();
		setGradebookManager();
		setSessionUserID();
		setSessionUserRole();
	}
	
	private void setCurrentUser() {
		currentUser.setCurrentUser(currentContext.getUser());
	}
	
	private void setCurrentCourseID() {
		currentCourseID.setCourse(currentContext.getCourseId());
	}
	
	private void setGradebookManager() {
		currentGradebook.setGradebookManager(currentCourseID);
	}
	
	private void setSessionUserID() {
		sessionUserID = currentUser.getID();
	}
	
	private void setSessionUserRole() {
		sessionUserRole = currentContext.getCourseMembership().getRoleAsString();
	}
}