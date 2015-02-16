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
		currentXML.setContent(SavedContent.Content.LEVELINDEX, "200");
		assertEquals("200", currentXML.getContent(SavedContent.Content.LEVELINDEX));
	}
	
	@Test
	public void testContentHolderLevel() {
		currentXML.setContent(SavedContent.Content.LEVEL, "Journeyman");
		assertEquals("Journeyman", currentXML.getContent(SavedContent.Content.LEVEL));
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
		currentXML.setContent(SavedContent.Content.LEVELINDEX, "200");
		currentXML.setContent(SavedContent.Content.LEVEL, "Journeyman");
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
		currentXML.setContent(SavedContent.Content.LEVELINDEX, "200");
		currentXML.setContent(SavedContent.Content.LEVEL, "Journeyman");
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
		assertEquals("CS491", currentXML.getContent(SavedContent.Content.COURSE));
	}
	
	private String getXML(int count) {
		String xml = "<course>";
		xml += "<courseID>" + currentXML.getContent(SavedContent.Content.COURSE) + "</courseID>";
		xml += "<setting>" + currentXML.getContent(SavedContent.Content.LEVELINDEX) + "</setting>";
		xml += "<levelLabel>" + currentXML.getContent(SavedContent.Content.LEVEL) + "</levelLabel>";
		xml += "<visibleStudents>" + currentXML.getContent(SavedContent.Content.VISIBLE) + "</visibleStudents>";
		xml += "<hiddenStudents>" + currentXML.getContent(SavedContent.Content.HIDDEN)  + "</hiddenStudents>";
		xml += "<modified>" + currentXML.getContent(SavedContent.Content.MODIFIED) + "</modified>";
		xml += "<userColor>" + currentXML.getContent(SavedContent.Content.USERCOLOR) + "</userColor>";
		xml += "<backgroundColor>" + currentXML.getContent(SavedContent.Content.OTHERCOLOR) + "</backgroundColor>";
		
		for (int i = 0; i < count; i++) {
			xml += "<student>";
			xml += "<id>" + i + "</id>";
			xml += "<gradebookLabel>" + currentXML.getGradebookLabel(i) + "</gradebookLabel>";
			xml += "</student>";
			
		}
		
		xml += "</course>";
		
		return xml;
	}
}