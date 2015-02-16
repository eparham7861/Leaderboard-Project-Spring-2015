package group.three;

import org.junit.*;
import static org.junit.Assert.*;

public class SavedContentTest {
	
	private SavedContent currentContent;
	
	@Before
	public void startUp() {
		currentContent = new SavedContent();
	}
	
	@Test
	public void testAddContentData() {
		currentContent.setContentItem(SavedContent.Content.COURSE, "CS491");
		assertEquals("CS491", currentContent.getContentItem(SavedContent.Content.COURSE));
	}
}