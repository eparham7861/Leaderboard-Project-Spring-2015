package group.three;

import org.junit.*;
import static org.junit.Assert.*;
import blackboard.platform.context.ContextManager;
import blackboard.platform.context.Context;
import blackboard.persist.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProcessorBBTest {
	private ProcessorBB currentProcessor;
	private Id currentCourseID;
	private String currentXML;
	
	@Before
	public void startUp() {
		currentCourseID = mock(Id.class);
		when(currentCourseID.toExternalString()).thenReturn("CS491");
		when(currentCourseID.toString()).thenReturn("CS491");
		
		currentXML = "<course>";
		currentXML += "<courseID>CS491</courseID>";
		currentXML += "<setting>100</setting>";
		currentXML += "<levelLabel>Journeyman</levelLabel>";
		currentXML += "<visibleStudents>3</visibleStudents>";
		currentXML += "<hiddenStudents>2</hiddenStudents>";
		currentXML += "<modified>true</modified>";
		currentXML += "<userColor>red</userColor>";
		currentXML += "<backgroundColor>red</backgroundColor>";
		currentXML += "<student>";
		currentXML += "<id>1</id>";
		currentXML += "<gradebookLabel>100</gradebookLabel>";
		currentXML += "</student>";
		currentXML += "</course>";
		
		currentProcessor = new ProcessorBB(currentCourseID);
	}
}