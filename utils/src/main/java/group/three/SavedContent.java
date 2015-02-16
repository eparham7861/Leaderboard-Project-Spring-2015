package group.three;

import java.util.*;

public class SavedContent {
	
	public enum Content {COURSE, LEVELINDEX, LEVEL, VISIBLE, HIDDEN, MODIFIED, USERCOLOR, OTHERCOLOR};
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