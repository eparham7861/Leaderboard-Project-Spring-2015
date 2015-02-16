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
	public void convertToXMLStringOneStudent(){
		currentXML.setCourseID("CS491");
		currentXML.setSetting(100);
		currentXML.setLevelLabel("Journeyman");
		currentXML.addGradebookLabel("450");
		currentXML.setVisibleStudents(1);
		currentXML.setHiddenStudentAmount(0);
		currentXML.setModifiedValue("true");
		currentXML.setUserColor("blue");
		currentXML.setBackgroundColor("black");
		
		assertEquals(getXML(1), currentXML.convertAllToXML());
	}
	
	@Test
	public void testConvertToXMLMultipleStudents() {
		currentXML.setCourseID("CS491");
		currentXML.setSetting(100);
		currentXML.setLevelLabel("Journeyman");
		currentXML.setVisibleStudents(5);
		currentXML.setHiddenStudentAmount(0);
		currentXML.setModifiedValue("true");
		currentXML.setUserColor("blue");
		currentXML.setBackgroundColor("black");
		
		for (int i = 0; i < 5; i++){
			currentXML.addGradebookLabel(Integer.toString(i + 450));
		}
		//currentXML.setGradebookLabel("450");
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
	
	@Test
	public void testGetPreviousXMLCourseID() {
		String xml = "<course>";
		xml += "<courseID>CS491</courseID>";
		xml += "<setting>100</setting>";
		xml += "<levelLabel>Journeyman</levelLabel>";
		xml += "<visibleStudents>3</visibleStudents>";
		xml += "<hiddenStudents>2</hiddenStudents>";
		xml += "<modified>true</modified>";
		xml += "<userColor>red</userColor>";
		xml += "<backgroundColor>red</backgroundColor>";
		xml += "<student>";
		xml += "<id>1</id>";
		xml += "<gradebookLabel>100</gradebookLabel>";
		xml += "</student>";
		xml += "</course>";
		
		currentXML.setXMLInputString(xml);
		assertEquals("CS491", currentXML.getCourseID());
	}
	
	private String getXML(int count) {
		String xml = "<course>";
		xml += "<courseID>" + currentXML.getCourseID() + "</courseID>";
		xml += "<setting>" + currentXML.getSetting() + "</setting>";
		xml += "<levelLabel>" + currentXML.getLevelLabel() + "</levelLabel>";
		xml += "<visibleStudents>" + currentXML.getVisibleStudents() + "</visibleStudents>";
		xml += "<hiddenStudents>" + currentXML.getHiddenStudentAmount()  + "</hiddenStudents>";
		xml += "<modified>" + currentXML.getModifiedValue() + "</modified>";
		xml += "<userColor>" + currentXML.getUserColor() + "</userColor>";
		xml += "<backgroundColor>" + currentXML.getBackgroundColor() + "</backgroundColor>";
		
		for (int i = 0; i < count; i++) {
			xml += "<student>";
			xml += "<id>" + i + "</id>";
			//xml += "<gradebookLabel>" + currentXML.getGradebookLabel() + "</gradebookLabel>";
			xml += "<gradebookLabel>" + currentXML.getGradebookLabel(i) + "</gradebookLabel>";
			xml += "</student>";
			
		}
		
		xml += "</course>";
		
		return xml;
	}
}