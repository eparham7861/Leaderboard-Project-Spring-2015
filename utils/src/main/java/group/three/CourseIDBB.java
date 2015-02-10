package group.three;

import blackboard.persist.*;

public class CourseIDBB {
	
	private Id currentCourseID;
	
	public void setCourse(Id currentCourseID) {
		this.currentCourseID = currentCourseID;
	}
	
	public String toExternalString() {
		return currentCourseID.toExternalString();
	}
	
	public String toString() {
		return currentCourseID.toString();
	}
}