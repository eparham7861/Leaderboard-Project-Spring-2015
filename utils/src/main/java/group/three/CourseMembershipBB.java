package group.three;

import blackboard.data.course.*;
import blackboard.data.course.CourseMembership.Role;
import java.util.*;
import blackboard.base.*;
import blackboard.data.course.*;
import blackboard.persist.course.*;
import blackboard.persist.*;


public class CourseMembershipBB {

	private List<CourseMembership> currentCourseMemberships;
	private Iterator<CourseMembership> courseMemberships;
	
	public void setCourseMemberships(CourseIDBB currentCourseID) {
		try {
			currentCourseMemberships = CourseMembershipDbLoader.Default.getInstance().loadByCourseIdAndRole(currentCourseID.getCourseID(), CourseMembership.Role.STUDENT, null, true);
			courseMemberships = currentCourseMemberships.iterator();
		}
		catch(PersistenceException e) {
		
		}
	}
	
	public List<CourseMembership> getCourseMemberships() {
		return currentCourseMemberships;
	}
	
	public Iterator<CourseMembership> getIterator() {
		return courseMemberships;
	}
}