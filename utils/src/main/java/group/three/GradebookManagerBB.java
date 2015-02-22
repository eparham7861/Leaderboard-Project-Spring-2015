package group.three;

import blackboard.platform.gradebook2.*;
import blackboard.platform.gradebook2.impl.*;
import blackboard.persist.*;
import blackboard.platform.security.authentication.BbSecurityException;
import java.util.*;

public class GradebookManagerBB {

	private GradebookManager currentGradebookManager;
	private BookData currentBookData;
	private List<GradableItem> gradeItems;
	private String gradebookChoice;
	
	public void setGradebookManager(CourseIDBB currentCourseID) {
		try {
			currentGradebookManager = GradebookManagerFactory.getInstanceWithoutSecurityCheck();
			currentBookData = currentGradebookManager.getBookData(new BookDataRequest(currentCourseID.getCourseID()));
			
			gradeItems = currentGradebookManager.getGradebookItems(currentCourseID.getCourseID());
			
			currentBookData.addParentReferences();
			currentBookData.runCumulativeGrading();
		}
		catch (BbSecurityException e) {
		}	
	}
	
	public void setGradebookColumn(String gradebookChoice) {
		for (GradableItem item : gradeItems) {
			if (item.getTitle().equals(gradebookChoice)) {
				this.gradebookChoice = gradebookChoice;
			}
		}
		this.gradebookChoice = "total";
	}
	
	public String getGradebookColumn() {
		return gradebookChoice;
	}
}