package group.three;

import blackboard.base.InitializationException;
import blackboard.data.user.User;
import blackboard.persist.*;
import javax.servlet.http.HttpServletRequest;
import blackboard.platform.context.ContextManager;
import blackboard.platform.context.Context;
import blackboard.platform.RuntimeBbServiceException;
import blackboard.platform.gradebook2.*;
import blackboard.platform.gradebook2.impl.*;
import blackboard.platform.plugin.PlugInUtil;
import java.util.*;

public class LeaderboardBB {
	private UserBB sessionUser;
	private CourseIDBB currentCourseID;
	private Context currentContext;
	private GradebookManagerBB currentGradebook;
	private CourseMembershipBB currentCourseMembership;
	private List<Student> students;
	private XMLFactory currentXML;
	private String sessionUserRole, sessionUserID, modified, gradeChoice;
	private double scoreToHighlight;
	private int index, numVisible;
	private boolean canSeeScores;
	
	public LeaderboardBB(Context currentContext) {
		try {
			this.currentContext = currentContext;
			
			sessionUserRole = "";
			sessionUserID = "";
			scoreToHighlight = -1.0;
			index = 0;
			
			sessionUser = new UserBB();
			currentCourseID = new CourseIDBB();
			currentGradebook = new GradebookManagerBB();
			currentCourseMembership = new CourseMembershipBB();
			students = new ArrayList<Student>();
			currentXML = new XMLFactory();
			canSeeScores = false;
			
			loadContent();
			setCurrentUser();
			setCurrentCourseID();
			setGradebookManager();
			setSessionUserID();
			setSessionUserRole();
			setCourseMemberships();
			setNumberOfVisibleStudents();
		}
		catch (RuntimeBbServiceException e) {
		
		}
	}
	
	private void loadContent() {
		try {
			ProcessorBB loadProcessor = new ProcessorBB(currentCourseID.getCourseID());
			currentXML.setXMLInputString(loadProcessor.loadContent(currentContext));
		}
		catch (PersistenceException e) {
		
		}
		
	}
	
	private void setCurrentUser() {
		sessionUser.setCurrentUser(currentContext.getUser());
	}
	
	private void setCurrentCourseID() {
		currentCourseID.setCourse(currentContext.getCourseId());
	}
	
	private void setGradebookManager() {
		currentGradebook.setGradebookManager(currentCourseID);
		currentGradebook.setGradebookColumn(currentXML.getContent(SavedContent.Content.GRADECHOICE));
		gradeChoice = currentGradebook.getGradebookColumn();
	}
	
	private void setSessionUserID() {
		sessionUserID = sessionUser.getID();
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
	
	private void setNumberOfVisibleStudents() {
		if (isModified()) {
			numVisible = Integer.parseInt(currentXML.getContent(SavedContent.Content.VISIBLE));
		}
		else {
			numVisible = currentCourseMembership.getCourseMemberships().size();
		}
	}
	
	private boolean isModified() {
		return Boolean.parseBoolean(currentXML.getContent(SavedContent.Content.MODIFIED));
	}
	
	private void studentSetup() {
		Iterator<CourseMembership> memberships = currentCourseMembership.getIterator();
		while (memberships.hasNext()) {
			CourseMembership selectedMember = memberships.next();
			String currentUserID = selectedMember.getUserId().toString();
			
			for (int x = 0; x < currentGradebook.getGradebookSize(); x++) {
				GradeWithAttempScore attemptedScore = currentGradebook.getGradebookAttemptedScore(selectedMember.getId(), x);
				
				double currentScore = 0.0;
				if (attemptedScore != null && !attemptedScore.IsNullGrade()) {
					currentScore = attemptedScore.getScoreValue();
				}
				if (currentGradebook.getGradebookItem(x).getTitle().trim().toLowerCase().equalsIgnoreCase(currentGradebook.getGradebookColumn())) {
					if (sessionUserID.equals(currentUserID)) {
						scoreToHighlight = currentScore;
					}
				}
			}
		}
		
	}
}