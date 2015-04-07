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
<%@page import="com.spvsoftwareproducts.blackboard.utils.B2Context"%>
<%@page import="blackboard.servlet.data.MultiSelectBean"%>
<%@page import="blackboard.platform.context.*"%>
<%@page import="group.three.*"%>
<%@page import="blackboard.data.user.User"%>

<bbNG:includedPage ctxId="ctx">

<%
String s = "<script>history.go(-2);</script>";
XMLFactory currentXML = new XMLFactory();
User sessionUser;
Id currentCourseID = (Id)application.getAttribute("theCourseID");
ArrayList<Student> students = new ArrayList<Student>();
String gradeChoice;
boolean fileExists;

sessionUser = ctx.getUser(); 
currentXML.setSessionUser(sessionUser);
boolean isInstructor = Boolean.parseBoolean(request.getParameter("instructor"));
if (request.getMethod().equalsIgnoreCase("POST")) {
	
	
	
	try {
		ProcessorBB loadProcessor = new ProcessorBB(currentCourseID);
		currentXML.setXMLInputString(loadProcessor.loadContent(ctx));
		currentXML.setCurrentStudent(sessionUser.getStudentId());
	}
	catch (PersistenceException e) {
	
	}
	
	fileExists = Boolean.parseBoolean(currentXML.getContent(SavedContent.Content.FILEEXISTS));
	
	if(isInstructor) {
		
		currentXML.setContent(SavedContent.Content.COURSECOLOR, request.getParameter("color"));
		
		int numFilledLevels = 10;
		String setting = "";
		String courseID = request.getParameter("courseID"); //Gets courseID that was passed from leaderboard_config.jsp -JJL
		// Get level values from user-submitted data and add it to the persistence object.
		
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

		//Show/hide settings Last edit 3-9-14 by Tim Burch.
		String leftVals = (request.getParameter("show_n_hide_left_values")).trim();
		String rightVals = (request.getParameter("show_n_hide_right_values")).trim();
		currentXML.setContent(SavedContent.Content.VISIBLE, leftVals);
		currentXML.setContent(SavedContent.Content.HIDDEN, rightVals);
		
		String[] visibleStudent = leftVals.split(",");
		
		currentXML.setContent(SavedContent.Content.NUMVISIBLE, Integer.toString(visibleStudent.length));
	}
	GradebookManager currentGradebook = GradebookManagerFactory.getInstanceWithoutSecurityCheck();
	BookData bookData = currentGradebook.getBookData(new BookDataRequest(currentCourseID));
	List<GradableItem> gradeItems = currentGradebook.getGradebookItems(currentCourseID);
	
	bookData.addParentReferences();
	bookData.runCumulativeGrading();
	
	List <CourseMembership> currentCourseMembership = CourseMembershipDbLoader.Default.getInstance().loadByCourseIdAndRole(currentCourseID, CourseMembership.Role.STUDENT, null, true);
	Iterator<CourseMembership> memberships = currentCourseMembership.iterator();
	
	String b2_grade_choice = "";
	
	b2_grade_choice = currentXML.getContent(SavedContent.Content.GRADECHOICE);
	
	String grade_choice = "total";
	
	for (int j = 0; j < gradeItems.size(); j++) {
		GradableItem gradeItem = (GradableItem) gradeItems.get(j);
		if (gradeItem.getTitle().equals(b2_grade_choice)) {
			grade_choice = b2_grade_choice;
		}
	}
	

	while (memberships.hasNext()) {
		CourseMembership selectedMember = memberships.next();
		String currentUserID = selectedMember.getUserId().toString();
		
		for (int x = 0; x < gradeItems.size(); x++) {
			GradableItem gi = (GradableItem) gradeItems.get(x);					
			GradeWithAttemptScore attemptedScore = bookData.get(selectedMember.getId(), gi.getId());;
			
			double currentScore = 0.0;
			if (attemptedScore != null) {
				//currentScore = attemptedScore.getScoreValue();
			}
			
			String sessionUserName = sessionUser.getGivenName() + " " + sessionUser.getFamilyName() + ": " + sessionUser.getUserName();
			if (fileExists) {
				String[] hiddenArr = currentXML.getContent(SavedContent.Content.HIDDEN).split(",");
				String studentName = selectedMember.getUser().getGivenName() + " " + selectedMember.getUser().getFamilyName() + ": " + selectedMember.getUser().getUserName();
				for (int i = 0; i < hiddenArr.length; i++) {
					String userColor, otherColor;
					
					if (studentName.equals(sessionUserName)){
						if (isInstructor) {
							userColor = currentXML.getContent(SavedContent.Content.USERCOLOR);
							otherColor = currentXML.getContent(SavedContent.Content.OTHERCOLOR);
						}
						else {
							userColor = request.getParameter("user_color");
							otherColor = request.getParameter("color");
						}
						Student currentStudent = new Student();
						currentStudent.setStudentID(selectedMember.getUser().getStudentId());
						currentStudent.setFirstName(selectedMember.getUser().getGivenName());
						currentStudent.setLastName(selectedMember.getUser().getFamilyName());
						currentStudent.setScore(currentScore);
						currentStudent.setUserName(selectedMember.getUser().getUserName());
						currentXML.setCurrentStudent(selectedMember.getUser().getStudentId());
						currentStudent.setStudentHighlightColor(userColor);
						currentStudent.setStudentGeneralColor(otherColor);
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
						currentStudent.setStudentHighlightColor(currentXML.getContent(SavedContent.Content.USERCOLOR));
						currentStudent.setStudentGeneralColor(currentXML.getContent(SavedContent.Content.OTHERCOLOR));
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
				currentStudent.setStudentHighlightColor(currentXML.getContent(SavedContent.Content.USERCOLOR));
				currentStudent.setStudentGeneralColor(currentXML.getContent(SavedContent.Content.OTHERCOLOR));
				students.add(currentStudent);
			}
		}
	}
	Collections.sort(students);
	Collections.reverse(students);
	
	currentXML.setStudentList(students);
	
	try {
		ProcessorBB saveProcessor = new ProcessorBB(currentCourseID);
		saveProcessor.saveContent(currentXML.convertAllToXML());
	}
	catch (PersistenceException e) {
	
	}
}

// May need error checking logic here (gaps in level fields, overlapping values, etc.)


%>

<%=s %>
</bbNG:includedPage>