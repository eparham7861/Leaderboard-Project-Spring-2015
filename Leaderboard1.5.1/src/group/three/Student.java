package group.three;

public class Student implements Comparable<Student> {
	private double score;
	private String fName, lName, userName, studentID;
	private boolean hidden;
	private String studentHighlightColor, studentGeneralColor;
	private String gradeColumn, timePeriod;
	
	public Student () {
		hidden = false;
		gradeColumn = "";
		timePeriod = "";
	}
	public void setStudentID(String ID){
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
	
	public void setGradeColumn(String gradeColumn) {
		this.gradeColumn = gradeColumn;
	}
	
	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}
	
	public String getStudentID(){
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
	
	public String getGradeColumn() {
		return gradeColumn;
	}
	
	public String getTimePeriod() {
		return timePeriod;
	}
	
	public int compareTo(Student s) {
		if (this.score > s.getScore()) {
			return 1;
		}
		else if (this.score < s.getScore()) {
			return -1;
		}
		else {
			return 0;
		}	    	
    }
}