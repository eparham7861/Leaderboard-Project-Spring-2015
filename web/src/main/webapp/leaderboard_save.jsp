<!-- .setSetting is used a lot here.  We'll have to discuss what function should be called
from XMLFactory since we have a different function for each instead of having an overloaded method
like blackboard's setSetting function call. - Jared -->



<%@page import="blackboard.base.*"%>
<%@page import="blackboard.data.course.*"%> 				<!-- for reading data -->
<%@page import="blackboard.data.user.*"%> 					<!-- for reading data -->
<%@page import="blackboard.persist.*"%> 					<!-- for writing data -->
<%@page import="blackboard.persist.course.*"%> 				<!-- for writing data -->
<%@page import="blackboard.platform.gradebook2.*"%>
<%@page import="blackboard.platform.gradebook2.impl.*"%>
<%@page import="java.util.*"%> 								<!-- for utilities -->
<%@page import="blackboard.platform.plugin.PlugInUtil"%>	<!-- for utilities -->
<%@ taglib uri="/bbData" prefix="bbData"%> 					<!-- for tags -->
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<%@page import="blackboard.servlet.data.MultiSelectBean"%>
<%@page import="blackboard.data.user.User"%>
<%@page import="group.three.*"%>
<bbNG:modulePage type="personalize" ctxId="ctx">

<%
String s = "<script>history.go(-2);</script>";

if (request.getMethod().equalsIgnoreCase("POST")) {
	XMLFactory currentXML;
	UserBB sessionUser;
	CourseIDBB currentCourseID;
	Context currentContext;
	GradebookManagerBB currentGradebook;
	CourseMembershipBB currentCourseMembership;
	List<Student> students;
	String gradeChoice;
	boolean fileExists;
	
	currentXML = new XMLFactory();
	sessionUser.setCurrentUser(ctx.getUser()); 
	
	try {
		ProcessorBB loadProcessor = new ProcessorBB(currentCourseID.getCourseID());
		currentXML.setXMLInputString(loadProcessor.loadContent(ctx));
		currentXML.setCurrentStudent(sessionUser.getStudentID());
	}
	catch (PersistenceException e) {
	
	}
	
	fileExists = Boolean.parseBoolean(currentXML.getContent(SavedContent.Content.FILEEXISTS));
	
	if(request.getParameter("instructor").equals("true")) {
		int numOfLevels = 10;
		String levelIndex, levelLabel;
		levelIndex = "";
		levelLabel = "";
		
		currentXML.setContent(SavedContent.Content.GRADECHOICE, request.getParameter("gradebook_column"));
		
		for (int i = 1; i <= 10; i++) {
			levelIndex = (i == 1)? "0":request.getParameter("Level_" + i + "_Points");
			levelLabel = (i == 1)? "0":request.getParameter("Level_" + i + "_Labels");
			if (i == 1) {
				currentXML.setContent(SavedContent.Content.LEVELINDEX0, levelIndex);
				currentXML.setContent(SavedContent.Content.LEVEL0, levelLabel);
			}
			else if(i == 2) {
				currentXML.setContent(SavedContent.Content.LEVELINDEX1, levelIndex);
				currentXML.setContent(SavedContent.Content.LEVEL1, levelLabel);
			}
			else if(i == 3) {
				currentXML.setContent(SavedContent.Content.LEVELINDEX2, levelIndex);
				currentXML.setContent(SavedContent.Content.LEVEL2, levelLabel);
			}
			else if(i == 4) {
				currentXML.setContent(SavedContent.Content.LEVELINDEX3, levelIndex);
				currentXML.setContent(SavedContent.Content.LEVEL3, levelLabel);
			}
			else if(i == 5) {
				currentXML.setContent(SavedContent.Content.LEVELINDEX4, levelIndex);
				currentXML.setContent(SavedContent.Content.LEVEL4, levelLabel);
			}
			else if(i == 6) {
				currentXML.setContent(SavedContent.Content.LEVELINDEX5, levelIndex);
				currentXML.setContent(SavedContent.Content.LEVEL5, levelLabel);
			}
			else if(i == 7) {
				currentXML.setContent(SavedContent.Content.LEVELINDEX6, levelIndex);
				currentXML.setContent(SavedContent.Content.LEVEL6, levelLabel);
			}
			else if(i == 8) {
				currentXML.setContent(SavedContent.Content.LEVELINDEX7, levelIndex);
				currentXML.setContent(SavedContent.Content.LEVEL7, levelLabel);
			}
			else if(i == 9) {
				currentXML.setContent(SavedContent.Content.LEVELINDEX8, levelIndex);
				currentXML.setContent(SavedContent.Content.LEVEL8, levelLabel);
			}
			else {
				currentXML.setContent(SavedContent.Content.LEVELINDEX9, levelIndex);
				currentXML.setContent(SavedContent.Content.LEVEL9, levelLabel);
			}
		}
		String leftVals = (request.getParameter("show_n_hide_left_values")).trim();
		String rightVals = (request.getParameter("show_n_hide_right_values")).trim();
		
		currentXML.setContent(SavedContent.Content.VISIBLE, leftVals);
		currentXML.setContent(SavedContent.Content.HIDDEN, rightVals);
		
		String[] visibleStudent = leftVals.split(",");
		
		currentXML.setContent(SavedContent.Content.NUMVISIBLE, visibleStudent.length);
	}
	
	currentCourseID.setCourse(ctx.getCourseId());
	
	currentGradebook.setGradebookManager(currentCourseID);
	currentGradebook.setGradebookColumn(currentXML.getContent(SavedContent.Content.GRADECHOICE));
	gradeChoice = currentGradebook.getGradebookColumn();
	
	currentCourseMembership.setCourseMemberships(currentCourseID);
	
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
			
			String sessionUserName = sessionUser.getName() + ": " + sessionUser.getUserName();
			if (fileExists()) {
				String[] hiddenArr = currentXML.getContent(SavedContent.Content.HIDDEN).split(",");
				String studentName = selectedMember.getUser().getGivenName() + " " + selectedMember.getUser().getFamilyName() + ": " + selectedMember.getUser().getUserName();
				for (int i = 0; i < hiddenArr.length; i++) {
					if (studentName.equals(sessionUserName)){
						Student currentStudent = new Student();
						currentStudent.setStudentID(selectedMember.getUser().getStudentId());
						currentStudent.setFirstName(selectedMember.getUser().getGivenName());
						currentStudent.setLastName(selectedMember.getUser().getFamilyName());
						currentStudent.setScore(currentScore);
						currentStudent.setUserName(selectedMember.getUser().getUserName());
						currentXML.setCurrentStudent(selectedMember.getUser().getStudentId());
						currentStudent.setStudentHighlightColor(request.getParameter("user_color"));
						currentStudent.setStudentGeneralColor(request.getParameter("color"));
						students.add(currentStudent);
						break;
					}
					else if (studentName.equals(hiddenArr[i])) {
						break;
					}
					else if(!studentName.equals(hiddenArr[i]) && i == hiddenArr.length - 1) {
						Student currentStudent = new Student();
						currentStudent.setStudentID(selectedMember.getUser().getStudentId());
						currentStudent.setFirstName(selectedMember.getUser().getGivenName());
						currentStudent.setLastName(selectedMember.getUser().getFamilyName());
						currentStudent.setScore(currentScore);
						currentStudent.setUserName(selectedMember.getUser().getUserName());
						currentXML.setCurrentStudent(selectedMember.getUser().getStudentId());
						currentStudent.setStudentHighlightColor(request.getParameter("user_color"));
						currentStudent.setStudentGeneralColor(request.getParameter("color"));
						students.add(currentStudent);
					}
				}
			}
			else {
				Student currentStudent = new Student();
				currentStudent.setStudentID(selectedMember.getUser().getStudentId());
				currentStudent.setFirstName(selectedMember.getUser().getGivenName());
				currentStudent.setLastName(selectedMember.getUser().getFamilyName());
				currentStudent.setScore(currentScore);
				currentStudent.setUserName(selectedMember.getUser().getUserName());
				currentXML.setCurrentStudent(selectedMember.getUser().getStudentId());
				currentStudent.setStudentHighlightColor(request.getParameter("user_color"));
				currentStudent.setStudentGeneralColor(request.getParameter("color"));
				students.add(currentStudent);
			}
		}
	}
	Collections.sort(students);
	Collections.reverse(students);
	
	currentXML.setStudentList(students);
	
	
	try {
		ProcessorBB saveProcessor = new ProcessorBB(currentCourseID.getCourseID());
		saveProcessor.saveContent(currentXML.convertAllToXML());
	}
	catch (PersistenceException e) {
	
	}
}

%>

<%=s %>