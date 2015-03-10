package group.three;
// Visible is used here - JS
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
		currentXML.setContent(SavedContent.Content.VISIBLE, "Eric Parham, Jared Starr, Eric Parris, Darren Johnston");
		assertEquals("Eric Parham, Jared Starr, Eric Parris, Darren Johnston", currentXML.getContent(SavedContent.Content.VISIBLE));
	}
	
	@Test
	public void testContentHolderVisibleStudentsCount() {
		currentXML.setContent(SavedContent.Content.VISIBLE, "Eric Parham, Jared Starr, Eric Parris, Darren Johnston");
		assertEquals("4", currentXML.getContent(SavedContent.Content.NUMVISIBLE));
	}
	
	@Test
	public void testContentHolderHiddenStudents() {
		currentXML.setContent(SavedContent.Content.HIDDEN, "Eric Parham, Jared Starr, Eric Parris, Darren Johnston");
		assertEquals("Eric Parham, Jared Starr, Eric Parris, Darren Johnston", currentXML.getContent(SavedContent.Content.HIDDEN));
	}
	
	@Test
	public void testContentHolderFileExists) {
		currentXML.setContent(SavedContent.Content.FILEEXISTS, "true");
		assertEquals("true", currentXML.getContent(SavedContent.Content.FILEEXISTS));
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
		currentXML.setContent(SavedContent.Content.GRADECHOICE, "total");
		assertEquals("total", currentXML.getContent(SavedContent.Content.GRADECHOICE));
	}
	
	@Test
	public void convertToXMLStringOneStudent(){
		currentXML.setContent(SavedContent.Content.COURSE, "CS491");
		currentXML.setContent(SavedContent.Content.LEVELINDEX0, "200");
		currentXML.setContent(SavedContent.Content.LEVEL0, "Journeyman");
		currentXML.setContent(SavedContent.Content.VISIBLE, "Jared Starr");
		currentXML.setContent(SavedContent.Content.HIDDEN, "Eric Parham, Eric Parris, Darren Johnston");
		currentXML.setContent(SavedContent.Content.FILEEXISTS, "true");
		currentXML.setContent(SavedContent.Content.USERCOLOR, "red");
		currentXML.setContent(SavedContent.Content.OTHERCOLOR, "red");
		currentXML.setContent(SavedContent.Content.GRADECHOICE, "total");
		
		assertEquals(getXML(Integer.parseInt(currentXML.getContent(SavedContent.Content.NUMVISIBLE))), currentXML.convertAllToXML());
	}
	
	@Test
	public void testConvertToXMLMultipleStudents() {
		currentXML.setContent(SavedContent.Content.COURSE, "CS491");
		currentXML.setContent(SavedContent.Content.LEVELINDEX0, "200");
		currentXML.setContent(SavedContent.Content.LEVEL0, "Journeyman");
		currentXML.setContent(SavedContent.Content.VISIBLE, "Eric Parham, Jared Starr, Eric Parris, Darren Johnston, Jared Benedict");
		currentXML.setContent(SavedContent.Content.HIDDEN, "Ian Bragg");
		currentXML.setContent(SavedContent.Content.FILEEXISTS, "true");
		currentXML.setContent(SavedContent.Content.USERCOLOR, "red");
		currentXML.setContent(SavedContent.Content.OTHERCOLOR, "red");
		currentXML.setContent(SavedContent.Content.GRADECHOICE, "total");
		
		assertEquals(getXML(Integer.parseInt(currentXML.getContent(SavedContent.Content.NUMVISIBLE))), currentXML.convertAllToXML());
	}
	
	@Test
	public void testGetPreviousXMLContent() {
		String xml = "<course>";
		xml += "<courseID>CS491</courseID>";
		xml += "<visibleStudents>3</visibleStudents>";
		xml += "<hiddenStudents>2</hiddenStudents>";
		xml += "<fileExists>true</fileExists>";
		xml += "<student>";
		xml += "<studentID>1</studentID>";
		xml += "<userColor>red</userColor>";
		xml += "<backgroundColor>red</backgroundColor>";
		xml += "<selectedGradebookColumn>100</selectedGradebookColumn>";
		xml += "</student>";
		xml += "<level>";
		xml += "<levelID>1</levelID>";
		xml += "<levelPoints>100</levelPoints>";
		xml += "<levelLabel>Journeyman</levelLabel>";
		xml += "</level>";
		xml += "<level>";
		xml += "<levelID>2</levelID>";
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
		stringToXML += "<fileExists>" + currentXML.getContent(SavedContent.Content.FILEEXISTS) + "</fileExists>";
		
		stringToXML += "<selectedGradebookColumn>" + currentXML.getContent(SavedContent.Content.GRADECHOICE) + "</selectedGradebookColumn>";
		
		for (int i = 0; i < count; i++) {
			stringToXML += "<student>";
			stringToXML += "<studentID>" + i + "</studentID>";
			stringToXML += "<userColor>" + currentXML.getContent(SavedContent.Content.USERCOLOR) + "</userColor>";
			stringToXML += "<backgroundColor>" + currentXML.getContent(SavedContent.Content.OTHERCOLOR) + "</backgroundColor>";
			stringToXML += "</student>";
		}
		
		for (int i = 0; i<10; i++){
			stringToXML += "<level>";
			stringToXML += "<levelID>" + i + "</levelID>";
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