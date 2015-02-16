package group.three;

import blackboard.platform.context.Context;
import blackboard.data.user.User;
import blackboard.persist.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class LeaderboardBB {
	private UserBB currentUser;
	private String sessionUserRole, sessionUserID;
	private CourseIDBB currentCourseID;
	private Context currentContext;
	private GradebookManagerBB currentGradebook;
	private CourseMembershipBB currentCourseMembership;
	private List<Student> students;
	private double scoreToHighlight;
	private int index, numVisible, gradeChoice;
	private String modified;
	private boolean canSeeScores;
	private XMLFactory currentXML;
	
	public LeaderboardBB(Context currentContext) {
		this.currentContext = currentContext;
		
		sessionUserRole = "";
		sessionUserID = "";
		scoreToHighlight = -1.0;
		index = 0;
		
		currentUser = new UserBB();
		currentCourseID = new CourseIDBB();
		currentGradebook = new GradebookManagerBB();
		currentCourseMembership = new CourseMembershipBB();
		students = new ArrayList<Student>();
		currentXML = new XMLFactory();
		canSeeScores = false;
		
		setCurrentUser();
		setCurrentCourseID();
		setGradebookManager();
		setSessionUserID();
		setSessionUserRole();
		setCourseMemberships();
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
		sessionUserRole = sessionUserRole.trim().toLowerCase();
	}
	
	private void setCourseMemberships() {
		currentCourseMembership.setCourseMemberships(currentCourseID);
	}
	
	private void isInstructor() {
		if (sessionUserRole.contains("instructor") || sessionUserRole.contains("faculty") || sessionUserRole.contains("teaching")) {
			canSeeScores = true;
		}
		else {
			canSeeScores = false;
		}
	}
}