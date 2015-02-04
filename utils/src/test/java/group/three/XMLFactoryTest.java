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