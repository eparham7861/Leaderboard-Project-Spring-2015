package group.three;

import org.junit.*;
import static org.junit.Assert.*;
import blackboard.persist.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CourseIDBBTest {
	
	private CourseIDBB currentCourseID;
	private Id currentID;
	@Before
	public void startUp() {
		currentCourseID = new CourseIDBB();
		currentID = mock(Id.class);
		when(currentID.toExternalString()).thenReturn("CS491");
		when(currentID.toString()).thenReturn("CS491");
		currentCourseID.setCourse(currentID);
	}
	
	@Test
	public void testGetCourseToExternalString() {
		assertEquals("CS491", currentCourseID.toExternalString());
	}
	
	@Test
	public void testGetCourseToString() {
		assertEquals("CS491", currentCourseID.toString());
	}
}