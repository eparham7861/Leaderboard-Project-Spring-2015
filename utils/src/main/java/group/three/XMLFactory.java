package group.three;

public class XMLFactory {
	private String courseID;
	private int settingNumber, visibleStudents, hiddenStudents;
	private String levelLabel;
	private String gradebookLabel;
	private String isModified;
	private String backgroundColor, userColor;
	
	public XMLFactory(){
		courseID = "";
		settingNumber = 0;
		visibleStudents = 1;
		hiddenStudents = 0;
		levelLabel = "";
		gradebookLabel = "";
		isModified = "";
		backgroundColor = "null";
		userColor = "null";
	}
	
	public void setCourseID(String id){
		this.courseID = id;
	}
	
	public void setSetting(int settingNum){
		this.settingNumber = settingNum;
	}
	
	public void setLevelLabel(String label){
		this.levelLabel = label;
	}
	
	public void setGradebookLabel(String gradeLabel){
		this.gradebookLabel = gradeLabel;
	}
	
	public void setVisibleStudents(int visibleStudents) {
		this.visibleStudents = visibleStudents;
	}
	
	public void setHiddenStudentAmount(int amount){
		this.hiddenStudents = amount;
	}
	
	public void setModifiedValue(String modifyBooleanString){
		this.isModified = modifyBooleanString;
	}
	
	public void setBackgroundColor(String colValue){
		this.backgroundColor = colValue;
	}
	
	public void setUserColor(String userColorValue){
		this.userColor = userColorValue;
	}
	
	public String getCourseID(){
		return this.courseID;
	}
	
	public int getSetting(){
		return this.settingNumber;
	}
	
	public String getLevelLabel(){
		return this.levelLabel;
	}
	
	public int getVisibleStudents() {
		return this.visibleStudents;
	}
	
	public int getHiddenStudentAmount(){
		return this.hiddenStudents;
	}
	
	public String getGradebookLabel(){
		return this.gradebookLabel;
	}
	
	public String getModifiedValue(){
		return this.isModified;
	}
	
	public String getBackgroundColor(){
		return this.backgroundColor;
	}
	
	public String getUserColor(){
		return this.userColor;
	}
	
	public String convertAllToXML(){
		String stringToXML = "<Leaderboard>";
		stringToXML += "<courseID>" + courseID + "</courseID>";
		stringToXML += "<setting>" + Integer.toString(settingNumber) + "</setting>";
		stringToXML += "<levelLabel>" + levelLabel + "</levelLabel>";
		
		for (int i = 0; i < visibleStudents; i++) {
			stringToXML += "<student><id>" + i + "</id>";
			stringToXML += "<gradebookLabel>" + gradebookLabel + "</gradebookLabel></student>";
		}
		
		stringToXML += "</Leaderboard>";
		
		
		
		return stringToXML;
	}
}