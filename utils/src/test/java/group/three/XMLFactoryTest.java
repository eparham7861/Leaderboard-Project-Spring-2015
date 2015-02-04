package group.three;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class XMLFactoryTest {
	
	private XMLFactory currentXML;
	
	@Before
	public void startUp() {
		currentXML = new XMLFactory();
	}
	
	@Test
	public void testSetCourseID() {
		currentXML.setCourseID("CS491");
		assertEquals("CS491", currentXML.getCourseID());
	}
	
	@Test
	public void testSettingNumber(){
		int settingNumber = 100;
		currentXML.setSetting(settingNumber);
		assertEquals(settingNumber, currentXML.getSetting());
	}
	
	@Test
	public void testLevelLabel(){
		String levelLabel = "Journeyman";
		currentXML.setLevelLabel(levelLabel);
		assertEquals(levelLabel, currentXML.getLevelLabel());
	}
	
	@Test
	public void testGradebookLabel(){
		String gradeLabel = "450";
		currentXML.setGradebookLabel(gradeLabel);
		assertEquals(gradeLabel, currentXML.getGradebookLabel());
	}
	
	@Test
	public void testSetVisibleStudents() {
		currentXML.setVisibleStudents(5);
		assertEquals(5, currentXML.getVisibleStudents());
	}
	
	@Test
	public void convertToXMLString(){
		currentXML.setCourseID("CS491");
		currentXML.setSetting(100);
		currentXML.setLevelLabel("Journeyman");
		currentXML.setGradebookLabel("450");
		currentXML.setVisibleStudents(1);
		assertEquals(getXML(1), currentXML.convertAllToXML());
	}
	
	@Test
	public void testConvertToXMLMultipleStudents() {
		currentXML.setCourseID("CS491");
		currentXML.setSetting(100);
		currentXML.setLevelLabel("Journeyman");
		currentXML.setGradebookLabel("450");
		currentXML.setVisibleStudents(5);
		assertEquals(getXML(5), currentXML.convertAllToXML());
	}
	
	@Test
	public void testSetHiddenStudents(){
		int hiddenStudentNum = 5;
		currentXML.setHiddenStudentAmount(hiddenStudentNum);
		assertEquals(hiddenStudentNum, currentXML.getHiddenStudentAmount());
	}
	
	@Test
	public void testModifiedValue(){
	//We believe the modified variable is to determine if there is any change in the # of visible students
		String modifyString = "true";
		currentXML.setModifiedValue(modifyString);
		assertEquals(modifyString, currentXML.getModifiedValue());
	}
	
	@Test
	public void testSetColorBackground(){
		String graphBackgroundColor = "blue";
		currentXML.setBackgroundColor(graphBackgroundColor);
		assertEquals(graphBackgroundColor, currentXML.getBackgroundColor());
	}
	
	@Test
	public void testSetUserColor(){
		String userColor = "red";
		currentXML.setUserColor(userColor);
		assertEquals(userColor, currentXML.getUserColor());
	}
	
	private String getXML(int count) {
		String xml = "<Leaderboard><courseID>" + currentXML.getCourseID() + "</courseID>";
		xml += "<setting>" + currentXML.getSetting() + "</setting>";
		xml += "<levelLabel>" + currentXML.getLevelLabel() + "</levelLabel>";
		
		for (int i = 0; i < count; i++) {
			xml += "<student><id>" + i + "</id>";
			xml += "<gradebookLabel>" + currentXML.getGradebookLabel() + "</gradebookLabel></student>";
			
		}
		
		xml +="</Leaderboard>";
		
		return xml;
	}
}