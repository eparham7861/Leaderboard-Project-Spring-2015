package group.three;

import java.util.*;

public class SavedContent {
	
	public enum Content {COURSE, LEVELINDEX0, LEVELINDEX1, LEVELINDEX2, LEVELINDEX3, LEVELINDEX4, LEVELINDEX5, LEVELINDEX6, LEVELINDEX7, LEVELINDEX8, LEVELINDEX9, LEVEL0, LEVEL1, LEVEL2, LEVEL3, LEVEL4, LEVEL5, LEVEL6, LEVEL7, LEVEL8, LEVEL9, VISIBLE, HIDDEN, MODIFIED, USERCOLOR, OTHERCOLOR, GRADECHOICE};
	private Map<Content, String> contentHolder;
	
	public SavedContent() {
		contentHolder = new LinkedHashMap<Content, String>();
		setupLevels();
	}
	
	private void setupLevels() {
		contentHolder.put(Content.LEVELINDEX0, "");
		contentHolder.put(Content.LEVELINDEX1, "");
		contentHolder.put(Content.LEVELINDEX2, "");
		contentHolder.put(Content.LEVELINDEX3, "");
		contentHolder.put(Content.LEVELINDEX4, "");
		contentHolder.put(Content.LEVELINDEX5, "");
		contentHolder.put(Content.LEVELINDEX6, "");
		contentHolder.put(Content.LEVELINDEX7, "");
		contentHolder.put(Content.LEVELINDEX8, "");
		contentHolder.put(Content.LEVELINDEX9, "");
		contentHolder.put(Content.LEVEL0, "");
		contentHolder.put(Content.LEVEL1, "");
		contentHolder.put(Content.LEVEL2, "");
		contentHolder.put(Content.LEVEL3, "");
		contentHolder.put(Content.LEVEL4, "");
		contentHolder.put(Content.LEVEL5, "");
		contentHolder.put(Content.LEVEL6, "");
		contentHolder.put(Content.LEVEL7, "");
		contentHolder.put(Content.LEVEL8, "");
		contentHolder.put(Content.LEVEL9, "");
	}
	
	public void setContentItem(Content contentName, String item) {
		contentHolder.put(contentName, item);
	}
	
	public String getContentItem(Content contentName) {
		return contentHolder.get(contentName);
	}
}