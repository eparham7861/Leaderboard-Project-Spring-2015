package group.three;

public class Student {
	private double score;
	private String fName, lName, userName;
	private boolean hidden;
	private int studentID;
	private String studentHighlightColor;
	private String studentGeneralColor;
	
	
	
	public Student () {
		hidden = false;
	}
	public void setStudentID(int ID){
		this.studentID = ID;
	}

	public void setFirstName(String fName) {
		this.fName = fName;
	}
	
	public void setLastName(String lName) {
		this.lName = lName;
	}
	
	public void setScore(double score) {
		this.score = score;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void setHidden() {
		hidden = true;
	}
	public void setStudentHighlightColor(String Color){
		this.studentHighlightColor = Color;
	}
	public void setStudentGeneralColor(String Color){
		this.studentGeneralColor = Color;
	}
	
	public int getStudentID(){
		return studentID;
	}
	
	public String getFirstName() {
		return fName;
	}
	
	public String getLastName() {
		return lName;
	}
	
	public double getScore() {
		return score;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public boolean isHidden() {
		return hidden;
	}
	public String getStudentHighlightColor(){
		return studentHighlightColor;
	}
	public String getStudentGeneralColor(){
		return studentGeneralColor;
	}
	
	public boolean isScoreHigher(Double score) {
		if (this.score > score) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean isScoreLower(Double score) {
		if (this.score < score) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean isScoreEqual(Double score) {
		if (this.score == score) {
			return true;
		}
		else {
			return false;
		}
	}
}