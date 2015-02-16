package group.three;

import org.junit.*;
import static org.junit.Assert.*;
import blackboard.data.course.CourseMembership;
import blackboard.persist.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CourseMembershipBBTest {

	private CourseIDBB currentCourseID;
	private CourseMembershipBB currentCourseMembership;
	private Id id;
	
	
	@Before
	public void startUp() {
	
		currentCourseID = new CourseIDBB();
		id = mock(Id.class);
		when(id.toExternalString()).thenReturn("CS491");
		when(id.toString()).thenReturn("CS491");
		
		currentCourseID.setCourse(id);
		
		currentCourseMembership = new CourseMembershipBB();
		currentCourseMembership.setCourseMemberships(currentCourseID);
	}
}