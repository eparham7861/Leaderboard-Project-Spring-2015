package group.three;

import java.util.*;

public class SavedContent {
	
	public enum Content {COURSE, LEVELINDEX0, LEVELINDEX1, LEVELINDEX2, LEVELINDEX3, LEVELINDEX4, LEVELINDEX5, LEVELINDEX6, LEVELINDEX7, LEVELINDEX8, LEVELINDEX9, LEVEL0, LEVEL1, LEVEL2, LEVEL3, LEVEL4, LEVEL5, LEVEL6, LEVEL7, LEVEL8, LEVEL9, VISIBLE, HIDDEN, MODIFIED, USERCOLOR, OTHERCOLOR, GRADECHOICE};
	private Map<Content, String> contentHolder;
	
	public SavedContent() {
		contentHolder = new LinkedHashMap<Content, String>();
	}
	
	public void setContentItem(Content contentName, String item) {
		contentHolder.put(contentName, item);
	}
	
	public String getContentItem(Content contentName) {
		return contentHolder.get(contentName);
	}
}