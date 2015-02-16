package group.three;

import blackboard.base.*;
import blackboard.data.course.*;
import blackboard.data.user.*;
import blackboard.persist.*;
import blackboard.persist.course.*;
import blackboard.platform.gradebook2.*;
import blackboard.platform.gradebook2.impl.*;
import java.util.*;
import blackboard.platform.plugin.PlugInUtil;
/*
<%@ taglib uri="/bbData" prefix="bbData"%> 					<!-- for tags -->
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<%@ taglib prefix="bbUI" uri="/bbUI" %>
*/
import blackboard.servlet.data.MultiSelectBean;
import blackboard.platform.security.authentication.BbSecurityException;
import blackboard.platform.context.Context;



public class Leaderboard_Config{

	/*Variables*/
	private String color_value;
	private String user_color_value;
	private Id courseID;
	private String [] level_values;
	private String [] level_labels;
	private String[] gradeList;
	private String jsConfigFormPath;
	private String sessionUserRole;
	private String modified;
	private String visibleList;
	private String hiddenList;
	private String prev_grade_choice;
	//private String[] hiddenArr;
	//private String[] visibleArr;
	private List<MultiSelectBean> leftList;
	private List<MultiSelectBean> rightList;
	private List<CourseMembership> cmlist;
	private List<GradableItem> lgm;
	private GradebookManager gm;
	private BookData bookData;
	private boolean isUserAnInstructor;
	private final int NUM_LABELS = 10;
	private Context ctx;
	//String jsConfigFormPath = PlugInUtil.getUri("dt", "leaderboardblock11", "js/config_form.js");

	public Leaderboard_Config(Context context) throws KeyNotFoundException, PersistenceException, BbSecurityException{
		color_value = "";
		user_color_value = "";
		level_values = new String[NUM_LABELS];
		level_labels = new String[NUM_LABELS];
		sessionUserRole = "";
		modified = "";
		visibleList = "";
		prev_grade_choice = "Total";
		isUserAnInstructor = false;
		leftList = new ArrayList<MultiSelectBean>(); //Used for UI
		rightList = new ArrayList<MultiSelectBean>(); //Used for UI
		gm = GradebookManagerFactory.getInstanceWithoutSecurityCheck();
		bookData = gm.getBookData(new BookDataRequest(courseID));
		lgm = gm.getGradebookItems(courseID);
		ctx = context;
		courseID = ctx.getCourseId();
		cmlist = CourseMembershipDbLoader.Default.getInstance().loadByCourseIdAndRole(courseID, CourseMembership.Role.STUDENT, null, true);
		jsConfigFormPath = ""; //Needs changing to ctx
	}
	
	/*Main Functions*/
	public void loadPreviousColorValues(){
		/*Load color_value and user_color_value from XML instead
		of b2Context*/
	}
	public void loadPreviousLevelInformation(){
		/*Load level_values and level_labels from XML instead of 
		b2Context*/
	}
	public void loadVisibleStudentInformation(){
		/*Need to get visibleList from XML instead of b2Context*/
	}
	public void loadHiddenStudentInformation(){
		/*Need to get visibleList from XML instead of b2Context*/
	}
	public void ensureUserIsInstructor(){
		//this.sessionUserRole = ctx.getCourseMembership().getRoleAsString();
		this.isUserAnInstructor = false;
		if (sessionUserRole.trim().toLowerCase().equals("instructor")){
			isUserAnInstructor = true;
		}
	}
	public void establishStudentLevels(){
		for(int i = 2; i <= 10; i++) { 
			//Sets default level titles
			String levelLabel;
			String levelPoints;
			levelLabel = level_labels[i-1];
			levelPoints = level_values[i-1];
			//Sets some default values if none is set
			if(i == 2 && levelLabel.equals("") && levelPoints.equals("")) {
				levelLabel = "Apprentice";
				levelPoints = "100";
			}
			if(i == 3 && levelLabel.equals("") && levelPoints.equals("")) {
				levelLabel = "Journeyman";
				levelPoints = "300";
			}
			if(i == 4 && levelLabel.equals("") && levelPoints.equals("")) {
				levelLabel = "Master Craftsman";
				levelPoints = "700";
			}
			if(i == 5 && levelLabel.equals("") && levelPoints.equals("")) {
				levelLabel = "Grand Master";
				levelPoints = "1000";
			}
		}
	}
	public void updateCourseRoster(){
	
	}
	public void initializeConfigFile(){
	
	}
	public void updateLevelInfoFromConfig(){
		
	}
	public void obtainCalculatedStudentsGradeData(){
		
	}
	public void createGradeColumnList(){
		
	}
	public void loadPreviousGradeColumnChoice(){
		
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
	public void setModified(String str){
		this.modified = str;
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
	public String getModified(){
		return this.modified;
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
	public static void debug(){
		
	}
	public static void main(String[] args){
		debug();
	}
}