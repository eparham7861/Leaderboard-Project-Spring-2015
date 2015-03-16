package group.three;


// Visible is used in this file - JS

import blackboard.base.*;
import blackboard.data.course.*;
import blackboard.data.user.*;
import blackboard.persist.*;
import blackboard.persist.course.*;
import blackboard.platform.gradebook2.*;
import blackboard.platform.gradebook2.impl.*;
import java.util.*;
import blackboard.platform.plugin.PlugInUtil;
import blackboard.servlet.data.MultiSelectBean;
import blackboard.platform.security.authentication.BbSecurityException;
import blackboard.platform.context.Context;



public class Leaderboard_Config{

	/*Variables*/
	private XMLFactory xmlFactory;
	private String color_value, user_color_value;
	private Id courseID;
	private String[] level_values, level_labels, gradeList, hiddenArr, visibleArr;
	private String jsConfigFormPath, sessionUserRole, fileExists;
	private String visibleList, hiddenList, prev_grade_choice, prev_grade_string;
	private List<MultiSelectBean> leftList, rightList;
	private List<Student> students;
	private MultiSelectBean leftBean, rightBean;
	private List<CourseMembership> cmlist;
	private List<GradableItem> lgm;
	private GradebookManager gm;
	private BookData bookData;
	private boolean isUserAnInstructor;
	private final int NUM_LABELS = 10;
	private Context ctx;
	private UserBB sessionUser;
	private CourseIDBB currentCourseID;
	private CourseMembershipBB currentCourseMembership;
	private GradebookManagerBB currentGradebook;
	private String gradeChoice;

	public Leaderboard_Config(Context context){
		try{
			xmlFactory = new XMLFactory();
			level_values = new String[NUM_LABELS];
			level_labels = new String[NUM_LABELS];
			prev_grade_choice = "Total";
			prev_grade_string = "";
			isUserAnInstructor = false;
			leftList = new ArrayList<MultiSelectBean>(); //Used for UI
			rightList = new ArrayList<MultiSelectBean>(); //Used for UI
			gm = GradebookManagerFactory.getInstanceWithoutSecurityCheck();
			bookData = gm.getBookData(new BookDataRequest(courseID));
			lgm = gm.getGradebookItems(courseID);
			ctx = context;
			courseID = ctx.getCourseId();
			cmlist = CourseMembershipDbLoader.Default.getInstance().loadByCourseIdAndRole(courseID, CourseMembership.Role.STUDENT, null, true);
			jsConfigFormPath = PlugInUtil.getUri("dt", "leaderboardblock11", "js/config_form.js");
			sessionUser = new UserBB();
			currentCourseID = new CourseIDBB();
			currentGradebook = new GradebookManagerBB();
			currentCourseMembership = new CourseMembershipBB();
			students = new ArrayList<Student>();
			gradeChoice = "";
			setCurrentUser();
			loadContent();
			setCurrentCourseID();
			setGradebookManager();
			setCourseMemberships();
			studentSetup();
		}
		catch(KeyNotFoundException e){}
		catch(BbSecurityException e){}
		catch(PersistenceException e){}
	}
	
	private void setCurrentUser() {
		sessionUser.setCurrentUser(ctx.getUser());
	}
	
	private void loadContent() {
		try {
			ProcessorBB loadProcessor = new ProcessorBB(currentCourseID.getCourseID());
			xmlFactory.setXMLInputString(loadProcessor.loadContent(ctx));
			xmlFactory.setCurrentStudent(sessionUser.getStudentID());
		}
		catch (PersistenceException e) {
		
		}
		
	}
	
	private void setCurrentCourseID() {
		currentCourseID.setCourse(ctx.getCourseId());
	}
	
	private void setGradebookManager() {
		currentGradebook.setGradebookManager(currentCourseID);
		currentGradebook.setGradebookColumn(xmlFactory.getContent(SavedContent.Content.GRADECHOICE));
		gradeChoice = currentGradebook.getGradebookColumn();
	}
	
	private void setCourseMemberships() {
		currentCourseMembership.setCourseMemberships(currentCourseID);
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
				String sessionUserName = sessionUser.getName() + ": " + sessionUser.getUserName();
				
				if (fileExists()) {
					String[] hiddenArr = xmlFactory.getContent(SavedContent.Content.HIDDEN).split(",");
					String studentName = selectedMember.getUser().getGivenName() + " " + selectedMember.getUser().getFamilyName() + ": " + selectedMember.getUser().getUserName();
					for (int i = 0; i < hiddenArr.length; i++) {
						if (studentName.equals(sessionUserName)){
							Student currentStudent = new Student();
							currentStudent.setStudentID(selectedMember.getUser().getStudentId());
							currentStudent.setFirstName(selectedMember.getUser().getGivenName());
							currentStudent.setLastName(selectedMember.getUser().getFamilyName());
							currentStudent.setScore(currentScore);
							currentStudent.setUserName(selectedMember.getUser().getUserName());
							xmlFactory.setCurrentStudent(selectedMember.getUser().getStudentId());
							currentStudent.setStudentHighlightColor(xmlFactory.getContent(SavedContent.Content.USERCOLOR));
							currentStudent.setStudentGeneralColor(xmlFactory.getContent(SavedContent.Content.OTHERCOLOR));
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
							xmlFactory.setCurrentStudent(selectedMember.getUser().getStudentId());
							currentStudent.setStudentHighlightColor(xmlFactory.getContent(SavedContent.Content.USERCOLOR));
							currentStudent.setStudentGeneralColor(xmlFactory.getContent(SavedContent.Content.OTHERCOLOR));
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
					xmlFactory.setCurrentStudent(selectedMember.getUser().getStudentId());
					currentStudent.setStudentHighlightColor(xmlFactory.getContent(SavedContent.Content.USERCOLOR));
					currentStudent.setStudentGeneralColor(xmlFactory.getContent(SavedContent.Content.OTHERCOLOR));
					students.add(currentStudent);
				}
			}
			Collections.sort(students);
			Collections.reverse(students);
		}
	}
	
	private boolean fileExists() {
		return Boolean.parseBoolean(xmlFactory.getContent(SavedContent.Content.FILEEXISTS));
	}
	
	public List<Student> getStudentList() {
		return students;
	}
	
	/*Main Functions*/
	public void loadPreviousColorValues(){
		/*Load color_value and user_color_value from XML instead of b2Context*/
		// Grab previously saved color value
		this.color_value = xmlFactory.getContent(SavedContent.Content.OTHERCOLOR);
		this.user_color_value = xmlFactory.getContent(SavedContent.Content.USERCOLOR);
	}
	public void loadPreviousLevelInformation(){
		/*Load level_values and level_labels from XML instead of b2Context*/
		// Grab previously saved level values and labels
		for(int i = 0; i < NUM_LABELS; i++){
			this.level_values[i] = xmlFactory.getLevelValue(i);
			this.level_labels[i] = xmlFactory.getLevelName(i);
		}
	}
	public boolean ensureUserIsInstructor(){
		this.sessionUserRole = ctx.getCourseMembership().getRoleAsString();
		//this.isUserAnInstructor = false;
		if (sessionUserRole.trim().toLowerCase().equals("instructor")){
			return true;
		}
		return false;
	}
	
	public String establishLevelLabel(int i){
		String levelLabel;
		levelLabel = level_labels[i-1];
		
		if(i == 2 && levelLabel.equals("")) {
			levelLabel = "Apprentice";
		}
		if(i == 3 && levelLabel.equals("")) {
			levelLabel = "Journeyman";
		}
		if(i == 4 && levelLabel.equals("")) {
			levelLabel = "Master Craftsman";
		}
		if(i == 5 && levelLabel.equals("")) {
			levelLabel = "Grand Master";
		}
		return levelLabel;
	}
	
	public String establishLevelValue(int i){
		String levelPoints;
		levelPoints = level_values[i-1];
		
		if(i == 2 && levelPoints.equals("")) {
			levelPoints = "100";
		}
		if(i == 3 && levelPoints.equals("")) {
			levelPoints = "300";
		}
		if(i == 4 && levelPoints.equals("")) {
			levelPoints = "700";
		}
		if(i == 5 && levelPoints.equals("")) {
			levelPoints = "1000";
		}
		return levelPoints;
	}
	
	public void createUserInterface(){
		try{
			this.leftList = new ArrayList<MultiSelectBean>();
			this.rightList = new ArrayList<MultiSelectBean>();
			this.fileExists = xmlFactory.getContent(SavedContent.Content.FILEEXISTS);//b2Context_sh.getSetting(false, true, "modified" +  courseID.toExternalString());
			this.cmlist = CourseMembershipDbLoader.Default.getInstance().loadByCourseIdAndRole(courseID, CourseMembership.Role.STUDENT, null, true);
		}
		catch(KeyNotFoundException e){}
		catch(PersistenceException e){}
	}
	public void createVisibleStudentList(){
		//Logic to determine if the default or a saved show/hide list is used
		//if(this.modified.equals("true")){//A save file already exists.
			//Blackboard only saves and loads information in strings
			//Each list is saved as one string of names with the following format:
			//"firstName lastName, firstName lastName, etc."
			/*B2CONTEXT*/
			this.visibleList = xmlFactory.getContent(SavedContent.Content.VISIBLE);//b2Context_sh.getSetting(false, true, "visibleStudents" +  courseID.toExternalString());
			this.visibleArr = visibleList.split(",");
			if(!(visibleList.trim().equals(" ")) && !(visibleList.trim().isEmpty()) && visibleList != null){
				for(int i = 0; i < visibleArr.length; i++){//Add any saved visible to left side.
					this.leftBean = new MultiSelectBean();
					this.leftBean.setValue(visibleArr[i]);
					this.leftBean.setLabel(visibleArr[i]);
					this.leftList.add(leftBean);
				}
			}
		//}
	}
	public void createHiddenStudentList(){
		/*B2CONTEXT*/
		this.hiddenList = xmlFactory.getContent(SavedContent.Content.HIDDEN); //b2Context_sh.getSetting(false, true, "hiddenStudents" +  courseID.toExternalString());
		this.hiddenArr = hiddenList.split(",");
		if(!(hiddenList.trim().equals(" ")) && !(hiddenList.trim().isEmpty()) && hiddenList != null){
			for(int i = 0; i < hiddenArr.length; i++){//Add any saved hidden to right side.
				this.rightBean = new MultiSelectBean();
				this.rightBean.setValue(hiddenArr[i]);
				this.rightBean.setLabel(hiddenArr[i]);
				this.rightList.add(rightBean);
			}
		}
	}
	public void checkRosterLargerThanCurrentLists(){
		/*
		If the cmlist (entire course roster) is larger than both the hidden and visible lists, then
		a student must be missing from the lists. So this checks if any new student has been added
		to the course roster since Leaderboard has been uploaded.
		*/
		//if(cmlist.size() > (visibleList.length() + hiddenList.length())){
		for(int i = 0; i < cmlist.size(); i++){//Check entire roster.
			User student = cmlist.get(i).getUser();
			String stuName = student.getGivenName() + " " + student.getFamilyName() + ": " + student.getUserName();
			boolean found = false;
			for(int j = 0; j < this.visibleArr.length; j++){//Check visible list
				if(stuName.equals(this.visibleArr[j])){
					found = true;
					break;
				}
			}
			if(found == false){
				for(int j = 0; j < this.hiddenArr.length; j++){//Check hidden list
					if(stuName.equals(this.hiddenArr[j])){
						found = true;
						break;
					}
				}
			}
			if(found == false){//If the name wasn't found on either list, add to visible.
				//MultiSelectBean leftBean = new MultiSelectBean();
				this.leftBean = new MultiSelectBean();
				this.leftBean.setValue(stuName);
				this.leftBean.setLabel(stuName);
				this.leftList.add(leftBean);
			}
		}
	}
	public void setDefaultEveryoneVisible(){
		for(int i = 0; i < this.cmlist.size(); i ++){
			this.leftBean = new MultiSelectBean();
			User student = this.cmlist.get(i).getUser();
			this.leftBean.setValue(student.getGivenName() + " " + student.getFamilyName() + ": " + student.getUserName());
			this.leftBean.setLabel(student.getGivenName() + " " + student.getFamilyName() + ": " + student.getUserName());
			this.leftList.add(leftBean);
		}
	}
	public void getGradebookData(){
		try{
			//Use the GradebookManager to get the gradebook data
			this.gm = GradebookManagerFactory.getInstanceWithoutSecurityCheck();
			this.bookData = gm.getBookData(new BookDataRequest(courseID));
			this.lgm = gm.getGradebookItems(courseID);
			//It is necessary to execute these two methods to obtain calculated students and extended grade data
			bookData.addParentReferences();
			bookData.runCumulativeGrading();
			
			//Create list of grade columns, so the instructor can select the grade to set for the widget if it is not the overall grade
			String[] gradeList = new String[lgm.size()];
			for (int i = 0; i < lgm.size(); i++) {
				GradableItem gi = (GradableItem) lgm.get(i);
				gradeList[i] = gi.getTitle();
			}
			
			//Load previous grade column choice. Sets default column as "Total". 
			this.prev_grade_choice = "Total";
			this.prev_grade_string = "";
			/*B2Context!*/
			//B2Context b2Context_grade = new B2Context(request);
			//prev_grade_choice = b2Context_grade.getSetting(false,true,"gradebook_column" + courseID.toExternalString());
			this.prev_grade_choice = xmlFactory.getContent(SavedContent.Content.GRADECHOICE);
			if(this.prev_grade_choice == "") this.prev_grade_choice = "Total";
			this.prev_grade_string = this.prev_grade_choice + " - (Chosen)"; //selected option on dropdown list 
		}
		catch(BbSecurityException e){}
	}
	
	/*Setters*/
	public void setColorValue(String str){
		this.color_value = str;
	}
	public void setUserColorValue(String str){
		this.user_color_value = str;
	}
	public void setLevelValue(int index, String str){
		this.level_values[index] = str;
	}
	public void setLevelLabel(int index, String str){
		this.level_labels[index] = str;
	}
	public void setIsUserAnInstructor(boolean bool){
		this.isUserAnInstructor = bool;
	}
	public void setJSConfigFormPath(String path){
		this.jsConfigFormPath = path;
	}
	public void setCourseID(Id course){
		this.courseID = course;
	}
	public void setSessionUserRole(String str){
		this.sessionUserRole = str;
	}
	public void setFileExists(String str){
		this.fileExists = str;
	}
	public void addLeftListValue(MultiSelectBean beanValue){
		this.leftList.add(beanValue);
	}
	public void setLeftListValue(int index, MultiSelectBean beanValue){
		this.leftList.set(index, beanValue);
	}
	public void addRightListValue(MultiSelectBean beanValue){
		this.rightList.add(beanValue);
	}
	public void setRightListValue(int index, MultiSelectBean beanValue){
		this.rightList.set(index, beanValue);
	}
	public void addCourseMembershipListValue(CourseMembership courseValue){
		this.cmlist.add(courseValue);
	}
	public void setCourseMembershipListValue(int index, CourseMembership cmValue){
		this.cmlist.set(index, cmValue);
	}
	
	/*Getters*/
	public Id getCourseID(){
		return this.courseID;
	}
	public String getColorValue(){
		return this.color_value;
	}
	public String getUserColorValue(){
		return this.user_color_value;
	}
	public String getLevelValue(int index){
		return this.level_values[index];
	}
	public String getLevelLabel(int index){
		return this.level_labels[index];
	}
	public boolean getIsUserAnInstructor(){
		return this.isUserAnInstructor;
	}
	public String getJSConfigFormPath(){
		return this.jsConfigFormPath;
	}
	public String getSessionUserRole(){
		return this.sessionUserRole;
	}
	public String getFileExists(){
		return this.fileExists;
	}
	public String getPrevGradeChoice(){
		return this.prev_grade_choice;
	}
	public String getPrevGradeString(){
		return this.prev_grade_string;
	}
	public MultiSelectBean getLeftListValue(int index){
		return this.leftList.get(index);
	}
	public List<MultiSelectBean> getLeftList(){
		return this.leftList;
	}
	public MultiSelectBean getRightListValue(int index){
		return this.rightList.get(index);
	}
	public List<MultiSelectBean> getRightList(){
		return this.rightList;
	}
	public CourseMembership getCourseMembershipListValue(int index){
		return this.cmlist.get(index);
	}
	public List<CourseMembership> getCourseMembershipList(){
		return this.cmlist;
	}
}