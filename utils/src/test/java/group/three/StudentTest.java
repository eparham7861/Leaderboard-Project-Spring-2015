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
	@Test
	public void testStudentScoreIsHigher() {
		currentStudent.setScore(33.2);
		assertTrue(currentStudent.isScoreHigher(20.2));
	}
	
	@Test
	public void testStudentScoreIsNotHigher() {
		currentStudent.setScore(33.2);
		assertFalse(currentStudent.isScoreHigher(80.2));
	}
	
	@Test
	public void testStudentScoreIsLower() {
		currentStudent.setScore(80.9);
		assertTrue(currentStudent.isScoreLower(99.9));
	}
	
	@Test
	public void testStudentScoreIsNotLower() {
		currentStudent.setScore(80.9);
		assertFalse(currentStudent.isScoreLower(69.9));
	}
	
	@Test
	public void testStudentScoreIsEqual() {
		currentStudent.setScore(80.9);
		assertTrue(currentStudent.isScoreEqual(80.9));
	}
	
	@Test
	public void testStudentScoreIsNotEqual() {
		currentStudent.setScore(80.9);
		assertFalse(currentStudent.isScoreEqual(69.9));
	}
}