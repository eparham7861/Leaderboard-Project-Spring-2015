package group.three;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class StudentTest {
	
	private Student currentStudent;
	@Before
	public void startUp() {
		currentStudent = new Student();
	}
	
	@Test
	public void testAddStudentID(){
		String id = "1111";
		currentStudent.setStudentID(id);
		assertEquals(id, currentStudent.getStudentID());
	}
	
	@Test
	public void testAddFirstName() {
		currentStudent.setFirstName("Eric");
		assertEquals("Eric", currentStudent.getFirstName());
	}
	
	@Test
	public void testAddLastName() {
		currentStudent.setLastName("Parham");
		assertEquals("Parham", currentStudent.getLastName());
	}
	
	@Test
	public void testAddScore() {
		currentStudent.setScore(88.9);
		assertEquals(88.9, currentStudent.getScore(), 0.0);
	}
	
	@Test
	public void testAddUserName() {
		currentStudent.setUserName("eparham");
		assertEquals("eparham", currentStudent.getUserName());
	}
	
	@Test
	public void testSetHidden() {
		currentStudent.setHidden();
		assertTrue(currentStudent.isHidden());
	}
	@Test
	public void testSetStudentHighlightColor(){
		currentStudent.setStudentHighlightColor("Red");
		assertEquals("Red", currentStudent.getStudentHighlightColor());
	}
	
	@Test
	public void testSetStudentGeneralColor(){
		currentStudent.setStudentGeneralColor("Blue");
		assertEquals("Blue", currentStudent.getStudentGeneralColor());
	}
}