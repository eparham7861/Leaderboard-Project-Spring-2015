package group.three;

public class XMLFactory {
	private String courseID;
	private int settingNumber, visibleStudents;
	private String levelLabel;
	private String gradebookLabel;
	
	public XMLFactory(){
		courseID = "";
		settingNumber = 0;
		visibleStudents = 1;
		levelLabel = "";
		gradebookLabel = "";
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
		return visibleStudents;
	}
	
	public String getGradebookLabel(){
		return this.gradebookLabel;
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