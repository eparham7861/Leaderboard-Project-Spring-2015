package group.three;
// Visible is used here - JS
import java.util.*;

public class SavedContent {
	
	public enum Content {
							COURSE, VISIBLE, NUMVISIBLE, 
							HIDDEN, FILEEXISTS, USERCOLOR, 
							OTHERCOLOR, GRADECHOICE,
							COURSECOLOR, COLUMNCHOICE, PERIOD,
							LEVELINDEX0, LEVELINDEX1, LEVELINDEX2,
							LEVELINDEX3, LEVELINDEX4, LEVELINDEX5,
							LEVELINDEX6, LEVELINDEX7, LEVELINDEX8,
							LEVELINDEX9,
							LEVEL0, LEVEL1, LEVEL2, LEVEL3, LEVEL4,
							LEVEL5, LEVEL6, LEVEL7, LEVEL8, LEVEL9
						};
	private Map<Content, String> contentHolder;
	
	public SavedContent() {
		contentHolder = new LinkedHashMap<Content, String>();
		setupLevels();
	}
	
	private void setupLevels() {
		contentHolder.put(Content.LEVELINDEX0, "0");
		contentHolder.put(Content.LEVELINDEX1, "0");
		contentHolder.put(Content.LEVELINDEX2, "0");
		contentHolder.put(Content.LEVELINDEX3, "0");
		contentHolder.put(Content.LEVELINDEX4, "0");
		contentHolder.put(Content.LEVELINDEX5, "0");
		contentHolder.put(Content.LEVELINDEX6, "0");
		contentHolder.put(Content.LEVELINDEX7, "0");
		contentHolder.put(Content.LEVELINDEX8, "0");
		contentHolder.put(Content.LEVELINDEX9, "0");
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
		if (contentHolder.containsKey(contentName)) {
			return contentHolder.get(contentName);
		}
		else {
			return "";
		}
	}
	
	public String getContentLevelLabel(int index){
			if (index == 0){
				return contentHolder.get(Content.LEVEL0);
			}
			else if (index == 1){
				return contentHolder.get(Content.LEVEL1);
			}
			else if (index == 2){
				return contentHolder.get(Content.LEVEL2);
			}
			else if (index == 3){
				return contentHolder.get(Content.LEVEL3);
			}
			else if (index == 4){
				return contentHolder.get(Content.LEVEL4);
			}
			else if (index == 5){
				return contentHolder.get(Content.LEVEL5);
			}
			else if (index == 6){
				return contentHolder.get(Content.LEVEL6);
			}
			else if (index == 7){
				return contentHolder.get(Content.LEVEL7);
			}
			else if (index == 8){
				return contentHolder.get(Content.LEVEL8);
			}
			else if (index == 9){
				return contentHolder.get(Content.LEVEL9);
			}
			return "";
	}
	
	public String getContentLevelValue(int index){
			if (index == 0){
				return contentHolder.get(Content.LEVELINDEX0);
			}
			else if (index == 1){
				return contentHolder.get(Content.LEVELINDEX1);
			}
			else if (index == 2){
				return contentHolder.get(Content.LEVELINDEX2);
			}
			else if (index == 3){
				return contentHolder.get(Content.LEVELINDEX3);
			}
			else if (index == 4){
				return contentHolder.get(Content.LEVELINDEX4);
			}
			else if (index == 5){
				return contentHolder.get(Content.LEVELINDEX5);
			}
			else if (index == 6){
				return contentHolder.get(Content.LEVELINDEX6);
			}
			else if (index == 7){
				return contentHolder.get(Content.LEVELINDEX7);
			}
			else if (index == 8){
				return contentHolder.get(Content.LEVELINDEX8);
			}
			else if (index == 9){
				return contentHolder.get(Content.LEVELINDEX9);
			}
			return "";
	}
}