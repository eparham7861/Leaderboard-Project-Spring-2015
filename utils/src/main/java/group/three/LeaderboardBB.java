package group.three;

import blackboard.base.*;
import blackboard.data.user.User;
import blackboard.persist.*;
import javax.servlet.http.HttpServletRequest;
import blackboard.data.course.*;
import blackboard.persist.*;
import blackboard.persist.course.*;
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
			studentSetup();
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
				GradeWithAttemptScore attemptedScore = currentGradebook.getGradebookAttemptedScore(selectedMember, x);
				
				double currentScore = 0.0;
				if (attemptedScore != null) {
					currentScore = attemptedScore.getScoreValue();
				}
				if (currentGradebook.getGradebookItem(x).getTitle().trim().toLowerCase().equalsIgnoreCase(currentGradebook.getGradebookColumn())) {
					if (sessionUserID.equals(currentUserID)) {
						scoreToHighlight = currentScore;
					}
				}
				String sessionUserName = sessionUser.getName() + ": " + sessionUser.getUserName();
				if (isModified()) {
					String[] hiddenArr = currentXML.getContent(SavedContent.Content.HIDDEN).split(",");
					String studentName = selectedMember.getUser().getGivenName() + " " + selectedMember.getUser().getFamilyName() + ": " + selectedMember.getUser().getUserName();
					for (int i = 0; i < hiddenArr.length; i++) {
						if (studentName.equals(sessionUserName)){
							Student currentStudent = new Student();
							currentStudent.setFirstName(selectedMember.getUser().getGivenName());
							currentStudent.setLastName(selectedMember.getUser().getFamilyName());
							currentStudent.setScore(currentScore);
							currentStudent.setUserName(selectedMember.getUser().getUserName());
							students.add(currentStudent);
							break;
						}
						else if (studentName.equals(hiddenArr[i])) {
							break;
						}
						else if(!studentName.equals(hiddenArr[i]) && i == hiddenArr.length - 1) {
							Student currentStudent = new Student();
							currentStudent.setFirstName(selectedMember.getUser().getGivenName());
							currentStudent.setLastName(selectedMember.getUser().getFamilyName());
							currentStudent.setScore(currentScore);
							currentStudent.setUserName(selectedMember.getUser().getUserName());
							students.add(currentStudent);
						}
					}
				}
				else {
					Student currentStudent = new Student();
					currentStudent.setFirstName(selectedMember.getUser().getGivenName());
					currentStudent.setLastName(selectedMember.getUser().getFamilyName());
					currentStudent.setScore(currentScore);
					currentStudent.setUserName(selectedMember.getUser().getUserName());
					students.add(currentStudent);
				}
			}
		}
		Collections.sort(students);
		Collections.reverse(students);
	}
	
	public String getSeriesValues() {
		boolean isHighlighted = false;
		String seriesValues = "";
		for (int x = 0; x < students.size(); x++) {
			double score = students.get(x).getScore();
			if (score == scoreToHighlight && !isHighlighted) {
				isHighlighted = true;
				seriesValues += "{dataLabels: { enabled: true, style: {fontWeight: 'bold'} }, y:  " + score + ", color: '"+ currentXML.getContent(SavedContent.Content.USERCOLOR) + "'}";
			}
			else {
				seriesValues += "{y: " + score + ", color: '" + currentXML.getContent(SavedContent.Content.OTHERCOLOR) + "'}";
			}
			if (x < students.size() - 1) {
				seriesValues += ",";
			}
		}
		seriesValues += "];";
		
		return seriesValues;
	}
	
	public String getStudentNames() {
		String studentNames = "";
		for (int x = 0; x < students.size(); x++) {
			String firstName = students.get(x).getFirstName();
			String lastName = students.get(x).getLastName();
			String userName = students.get(x).getUserName();
			
			if (canSeeScores) {
				studentNames = '"' + firstName.substring(0, 1) + ' ' + lastName + '"';
			}
			else if (sessionUser.getName().equals(firstName + " " + lastName) && sessionUser.getUserName().equals(userName)) {
				studentNames += "'You'";
			}
			else {
				studentNames += (x + 1);
			}
			
			if (x < students.size() - 1) {
				studentNames += ",";
			}
		}
		studentNames += "];";
		
		return studentNames;
	}
	
	public int getNumberOfVisibleStudents() {
		int amount;
		if (currentXML.getContent(SavedContent.Content.NUMVISIBLE).equals("")){
			amount = 0;
		}
		else {
			amount = Integer.parseInt(currentXML.getContent(SavedContent.Content.NUMVISIBLE));
		}
		return amount;
	}
	
	public String getSpacingTop() {
		return "spacingTop:95";
	}
	
	public String getPlotbands() {
		int levelFrom = 0;
		int levelTo = 0;
		String levelLabel = "";
		String plotBands = "";
		
		for (int i = 1; i <= 10; i++) {
			levelFrom = getLevelPoints(i);
			if (i == 10) {
				levelTo = levelFrom * 2;
			}
			else {
				levelTo = getLevelPoints(i + 1);
			}
			
			int gradient = (255 / (20)) * ((20) - i);
			
			levelLabel = getLevelLabel(i);
			
			if (levelLabel.equals("")) {
				levelLabel = "Level " + i;
			}
			
			plotBands += "{ color: 'rgb(" + gradient + ", " + gradient + ", " + gradient + ")', ";
			plotBands += "from: " + levelFrom + ", ";
			plotBands += "to: " + levelTo + ", ";
			
			if (i == 1) {
				plotBands += "label: { text: '',rotation: -35,align: 'center',textAlign: 'left', verticalAlign: 'top', y: -10, style: { color: '#666666'}}}";
			}
			else {
				plotBands += "label: { text: '"+ levelLabel +"',rotation: -35,textAlign: 'left',align: 'center', verticalAlign: 'top', y: -10, style: { color: '#666666', fontFamily: 'Verdana, sans-serif'}}}";
			}
			
			if (i < 10) {
				plotBands += ", ";
			}
		}
		
		return plotBands;
	}
	
	private int getLevelPoints(int index) {
		int levelPoints = 0;
		switch(index) {
			case 1:
				levelPoints = Integer.parseInt(currentXML.getContent(SavedContent.Content.LEVELINDEX0));
			case 2:
				levelPoints = Integer.parseInt(currentXML.getContent(SavedContent.Content.LEVELINDEX1));
			case 3:
				levelPoints = Integer.parseInt(currentXML.getContent(SavedContent.Content.LEVELINDEX2));
			case 4:
				levelPoints = Integer.parseInt(currentXML.getContent(SavedContent.Content.LEVELINDEX3));
			case 5:
				levelPoints = Integer.parseInt(currentXML.getContent(SavedContent.Content.LEVELINDEX4));
			case 6:
				levelPoints = Integer.parseInt(currentXML.getContent(SavedContent.Content.LEVELINDEX5));
			case 7:
				levelPoints = Integer.parseInt(currentXML.getContent(SavedContent.Content.LEVELINDEX6));
			case 8:
				levelPoints = Integer.parseInt(currentXML.getContent(SavedContent.Content.LEVELINDEX7));
			case 9:
				levelPoints = Integer.parseInt(currentXML.getContent(SavedContent.Content.LEVELINDEX8));
			case 10:
				levelPoints = Integer.parseInt(currentXML.getContent(SavedContent.Content.LEVELINDEX9));
		}
		return levelPoints;
	}
	
	private String getLevelLabel(int index) {
		String level = "";
		switch(index) {
			case 1:
				level = currentXML.getContent(SavedContent.Content.LEVEL0);
			case 2:
				level = currentXML.getContent(SavedContent.Content.LEVEL1);
			case 3:
				level = currentXML.getContent(SavedContent.Content.LEVEL2);
			case 4:
				level = currentXML.getContent(SavedContent.Content.LEVEL3);
			case 5:
				level = currentXML.getContent(SavedContent.Content.LEVEL4);
			case 6:
				level = currentXML.getContent(SavedContent.Content.LEVEL5);
			case 7:
				level = currentXML.getContent(SavedContent.Content.LEVEL6);
			case 8:
				level = currentXML.getContent(SavedContent.Content.LEVEL7);
			case 9:
				level = currentXML.getContent(SavedContent.Content.LEVEL8);
			case 10:
				level = currentXML.getContent(SavedContent.Content.LEVEL9);
		}
		return level;
	}
}