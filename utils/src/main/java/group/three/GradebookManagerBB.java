package group.three;

import blackboard.platform.gradebook2.*;
import blackboard.platform.gradebook2.impl.*;
import blackboard.persist.*;
import blackboard.data.course.*;
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
			gradebookChoice = "";
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
		if (this.gradebookChoice.equals("")) {
			this.gradebookChoice = "total";
		}
		
	}
	
	public String getGradebookColumn() {
		return gradebookChoice;
	}
	
	public int getGradebookSize() {
		return gradeItems.size();
	}
	
	public GradeWithAttemptScore getGradebookAttemptedScore(CourseMembership currentMembership, int choice) {
		return currentBookData.get(currentMembership.getId(), gradeItems.get(choice).getId());
	}
	
	public GradableItem getGradebookItem(int choice) {
		return gradeItems.get(choice);
	}
}