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
	public void testContentHolderCourse() {
		currentXML.setContent(SavedContent.Content.COURSE, "CS491");
		assertEquals("CS491", currentXML.getContent(SavedContent.Content.COURSE));
	}
	
	@Test
	public void testContentHolderLevelIndex() {
		currentXML.setContent(SavedContent.Content.LEVELINDEX0, "200");
		assertEquals("200", currentXML.getContent(SavedContent.Content.LEVELINDEX0));
	}
	
	@Test
	public void testContentHolderLevel() {
		currentXML.setContent(SavedContent.Content.LEVEL0, "Journeyman");
		assertEquals("Journeyman", currentXML.getContent(SavedContent.Content.LEVEL0));
	}
	
	@Test
	public void testContentHolderVisibleStudents() {
		currentXML.setContent(SavedContent.Content.VISIBLE, "5");
		assertEquals("5", currentXML.getContent(SavedContent.Content.VISIBLE));
	}
	
	@Test
	public void testContentHolderHiddenStudents() {
		currentXML.setContent(SavedContent.Content.HIDDEN, "1");
		assertEquals("1", currentXML.getContent(SavedContent.Content.HIDDEN));
	}
	
	@Test
	public void testContentHolderModified() {
		currentXML.setContent(SavedContent.Content.MODIFIED, "true");
		assertEquals("true", currentXML.getContent(SavedContent.Content.MODIFIED));
	}
	
	@Test
	public void testContentHolderUserColor() {
		currentXML.setContent(SavedContent.Content.USERCOLOR, "red");
		assertEquals("red", currentXML.getContent(SavedContent.Content.USERCOLOR));
	}
	
	@Test
	public void testContentHolderOtherColor() {
		currentXML.setContent(SavedContent.Content.OTHERCOLOR, "red");
		assertEquals("red", currentXML.getContent(SavedContent.Content.OTHERCOLOR));
	}
	
	@Test
	public void testGradebookLabel(){
		String gradeLabel = "450";
		currentXML.addGradebookLabel(gradeLabel);
		assertEquals(gradeLabel, currentXML.getGradebookLabel(0));
	}
	
	@Test
	public void convertToXMLStringOneStudent(){
		currentXML.setContent(SavedContent.Content.COURSE, "CS491");
		currentXML.setContent(SavedContent.Content.LEVELINDEX0, "200");
		currentXML.setContent(SavedContent.Content.LEVEL0, "Journeyman");
		currentXML.setContent(SavedContent.Content.VISIBLE, "1");
		currentXML.setContent(SavedContent.Content.HIDDEN, "1");
		currentXML.setContent(SavedContent.Content.MODIFIED, "true");
		currentXML.setContent(SavedContent.Content.USERCOLOR, "red");
		currentXML.setContent(SavedContent.Content.OTHERCOLOR, "red");
		currentXML.addGradebookLabel("450");
		
		assertEquals(getXML(1), currentXML.convertAllToXML());
	}
	
	@Test
	public void testConvertToXMLMultipleStudents() {
		currentXML.setContent(SavedContent.Content.COURSE, "CS491");
		currentXML.setContent(SavedContent.Content.LEVELINDEX0, "200");
		currentXML.setContent(SavedContent.Content.LEVEL0, "Journeyman");
		currentXML.setContent(SavedContent.Content.VISIBLE, "5");
		currentXML.setContent(SavedContent.Content.HIDDEN, "1");
		currentXML.setContent(SavedContent.Content.MODIFIED, "true");
		currentXML.setContent(SavedContent.Content.USERCOLOR, "red");
		currentXML.setContent(SavedContent.Content.OTHERCOLOR, "red");
		
		for (int i = 0; i < 5; i++){
			currentXML.addGradebookLabel(Integer.toString(i + 450));
		}
		
		assertEquals(getXML(5), currentXML.convertAllToXML());
	}
	
	@Test
	public void testGetPreviousXMLContent() {
		String xml = "<course>";
		xml += "<courseID>CS491</courseID>";
		//xml += "<levelPoints>100</levelPoints>";
		//xml += "<levelLabel>Journeyman</levelLabel>";
		xml += "<visibleStudents>3</visibleStudents>";
		xml += "<hiddenStudents>2</hiddenStudents>";
		xml += "<modified>true</modified>";
		xml += "<userColor>red</userColor>";
		xml += "<backgroundColor>red</backgroundColor>";
		xml += "<student>";
		xml += "<id>1</id>";
		xml += "<gradebookLabel>100</gradebookLabel>";
		xml += "</student>";
		xml += "<level>";
		xml += "<id>1</id>";
		xml += "<levelPoints>100</levelPoints>";
		xml += "<levelLabel>Journeyman</levelLabel>";
		xml += "</level>";
		xml += "<level>";
		xml += "<id>2</id>";
		xml += "<levelPoints>300</levelPoints>";
		xml += "<levelLabel>Boss</levelLabel>";
		xml+= "</level>";
		xml += "</course>";
		
		currentXML.setXMLInputString(xml);
		assertEquals("CS491", currentXML.getContent(SavedContent.Content.COURSE));
	}
	
	private String getXML(int count) {
		String stringToXML = "<course>";
		stringToXML += "<courseID>" + currentXML.getContent(SavedContent.Content.COURSE) + "</courseID>";
		stringToXML += "<visibleStudents>" + currentXML.getContent(SavedContent.Content.VISIBLE) + "</visibleStudents>";
		stringToXML += "<hiddenStudents>" + currentXML.getContent(SavedContent.Content.HIDDEN)  + "</hiddenStudents>";
		stringToXML += "<modified>" + currentXML.getContent(SavedContent.Content.MODIFIED) + "</modified>";
		stringToXML += "<userColor>" + currentXML.getContent(SavedContent.Content.USERCOLOR) + "</userColor>";
		stringToXML += "<backgroundColor>" + currentXML.getContent(SavedContent.Content.OTHERCOLOR) + "</backgroundColor>";
		
		for (int i = 0; i < Integer.parseInt(currentXML.getContent(SavedContent.Content.VISIBLE)); i++) {
			stringToXML += "<student>";
			stringToXML += "<id>" + i + "</id>";
			stringToXML += "<gradebookLabel>" + currentXML.getContent(SavedContent.Content.GRADECHOICE) + "</gradebookLabel>";
			stringToXML += "</student>";
		}
		
		for (int i = 0; i<10; i++){
			stringToXML += "<level>";
			stringToXML += "<id>" + i + "</id>";
			if (i == 0){
				stringToXML += "<levelPoints>" + currentXML.getContent(SavedContent.Content.LEVELINDEX0) + "</levelPoints>";
				stringToXML += "<levelLabel>" + currentXML.getContent(SavedContent.Content.LEVEL0) + "</levelLabel>";
			}
			else if (i == 1){
				stringToXML += "<levelPoints>" + currentXML.getContent(SavedContent.Content.LEVELINDEX1) + "</levelPoints>";
				stringToXML += "<levelLabel>" + currentXML.getContent(SavedContent.Content.LEVEL1) + "</levelLabel>";
			}
			else if (i == 2){
				stringToXML += "<levelPoints>" + currentXML.getContent(SavedContent.Content.LEVELINDEX2) + "</levelPoints>";
				stringToXML += "<levelLabel>" + currentXML.getContent(SavedContent.Content.LEVEL2) + "</levelLabel>";
			}
			else if (i == 3){
				stringToXML += "<levelPoints>" + currentXML.getContent(SavedContent.Content.LEVELINDEX3) + "</levelPoints>";
				stringToXML += "<levelLabel>" + currentXML.getContent(SavedContent.Content.LEVEL3) + "</levelLabel>";
			}
			else if (i == 4){
				stringToXML += "<levelPoints>" + currentXML.getContent(SavedContent.Content.LEVELINDEX4) + "</levelPoints>";
				stringToXML += "<levelLabel>" + currentXML.getContent(SavedContent.Content.LEVEL4) + "</levelLabel>";
			}
			else if (i == 5){
				stringToXML += "<levelPoints>" + currentXML.getContent(SavedContent.Content.LEVELINDEX5) + "</levelPoints>";
				stringToXML += "<levelLabel>" + currentXML.getContent(SavedContent.Content.LEVEL5) + "</levelLabel>";
			}
			else if (i == 6){
				stringToXML += "<levelPoints>" + currentXML.getContent(SavedContent.Content.LEVELINDEX6) + "</levelPoints>";
				stringToXML += "<levelLabel>" + currentXML.getContent(SavedContent.Content.LEVEL6) + "</levelLabel>";
			}
			else if (i == 7){
				stringToXML += "<levelPoints>" + currentXML.getContent(SavedContent.Content.LEVELINDEX7) + "</levelPoints>";
				stringToXML += "<levelLabel>" + currentXML.getContent(SavedContent.Content.LEVEL7) + "</levelLabel>";
			}
			else if (i == 8){
				stringToXML += "<levelPoints>" + currentXML.getContent(SavedContent.Content.LEVELINDEX8) + "</levelPoints>";
				stringToXML += "<levelLabel>" + currentXML.getContent(SavedContent.Content.LEVEL8) + "</levelLabel>";
			}
			else if (i == 9){
				stringToXML += "<levelPoints>" + currentXML.getContent(SavedContent.Content.LEVELINDEX9) + "</levelPoints>";
				stringToXML += "<levelLabel>" + currentXML.getContent(SavedContent.Content.LEVEL9) + "</levelLabel>";
			}
			stringToXML += "</level>";
		}
		
		stringToXML += "</course>";
		
		return stringToXML;
	}
}