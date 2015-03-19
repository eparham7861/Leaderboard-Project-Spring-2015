package group.three;

import blackboard.persist.*;
import blackboard.persist.content.*;
import blackboard.data.content.*;
import blackboard.data.content.avlrule.AvailabilityRule;
import blackboard.data.course.*;
import blackboard.data.gradebook.*;
import blackboard.data.navigation.CourseToc;
import blackboard.persist.content.avlrule.*;
import blackboard.persist.gradebook.impl.OutcomeDefinitionDbLoader;
import blackboard.persist.navigation.CourseTocDbLoader;
import blackboard.platform.context.Context;
import blackboard.base.FormattedText;
import blackboard.data.ValidationException;
import java.util.*;


public class ProcessorBB {
	private Id currentCourseID;
	private Content courseDoc;
	private ContentDbPersister contentPersister;
	private CourseTocDbLoader cTocLoader;
	private ContentDbLoader cntDbLoader;
	private List<CourseToc> tableOfContents;
	private List<Content> tableChildren;
	private String currentXML;
	
	public ProcessorBB(Id currentCourseID) {
		this.currentCourseID = currentCourseID;
	}
	
	public void saveContent(String currentXML) throws PersistenceException {
		this.currentXML = currentXML;
		
		cTocLoader = CourseTocDbLoader.Default.getInstance();
		cntDbLoader = ContentDbLoader.Default.getInstance();

		tableOfContents = cTocLoader.loadByCourseId(currentCourseID);
		tableChildren = new ArrayList<Content>();
		
		for (CourseToc t : tableOfContents) {
			if (t.getTargetType() == CourseToc.Target.CONTENT) {
				tableChildren.addAll(cntDbLoader.loadChildren(t.getContentId(), false, null));
			}
		}
		
		//Write to content file as new or existing
		if (contentExists()){
			saveToExistingContent();
		}
		else {
			saveToNewContent();
		}
		persistToDB();
	}
	
	private boolean contentExists() {
		for (Content item : tableChildren) {
			if (item.getTitle().equalsIgnoreCase("Leaderboard")) {
				return true;
			}
		}
		return false;
	}
	
	private void saveToExistingContent() {
		try {
			for (Content item : tableChildren) {
				if (item.getTitle().equalsIgnoreCase("Leaderboard")) {
					courseDoc = cntDbLoader.loadById(item.getId());
					courseDoc.setBody(formatXML());
				}
			}
		}
		catch (KeyNotFoundException ex) {
			System.out.println(ex);
		}
		catch (PersistenceException ex) {
			System.out.println(ex);
		}
	}
	
	private void saveToNewContent() {
		courseDoc = new Content();
		try {
			for (CourseToc courseTab : tableOfContents) {
				if (courseTab.getLabel().trim().equals("Content")) {
					courseDoc.setTitle("Leaderboard");
					courseDoc.setCourseId(currentCourseID); //done twice??
					courseDoc.setParentId(courseTab.getContentId());
					courseDoc.setBody(formatXML());
					courseDoc.setCourseId(currentCourseID);
					courseDoc.setIsAvailable(false);
					courseDoc.setIsTracked(false);
					courseDoc.setIsDescribed(false);
					courseDoc.setLaunchInNewWindow(false);
					courseDoc.setAllowGuests(false);
					courseDoc.setAllowObservers(false);
					break;
				}
			}
		}
		catch (Exception e) {
			String errorMsg = e.getLocalizedMessage();
		}	
	}
	
	private FormattedText formatXML() {
		return new FormattedText(currentXML, FormattedText.Type.PLAIN_TEXT);
	}
	
	private void persistToDB() {
		try {
			contentPersister =  ContentDbPersister.Default.getInstance();
			contentPersister.persist(courseDoc);
		} catch (ValidationException ex) {
			 String errorMsg = ex.getLocalizedMessage();
		} catch (PersistenceException ex) {
			String errorMsg = ex.getLocalizedMessage();
		}
	}
	
	public String loadContent(Context courseContext) throws PersistenceException {
		String contentXML = " ";
		
		cTocLoader = CourseTocDbLoader.Default.getInstance();
		cntDbLoader = ContentDbLoader.Default.getInstance();

		tableOfContents = cTocLoader.loadByCourseId(courseContext.getCourseId());
		
		tableChildren = new ArrayList<Content>();
		
		for (CourseToc courseTab : tableOfContents) {
			if (courseTab.getTargetType() == CourseToc.Target.CONTENT) {
				tableChildren.addAll(cntDbLoader.loadChildren(courseTab.getContentId(), false, null));
			}
		}
		
		for (Content item : tableChildren) {
			if (item.getTitle().equalsIgnoreCase("Leaderboard")) {
				if (item.getBody().getText() != null) {
					contentXML += item.getBody().getText();
				}
			}
		}
		
		if (contentXML.equals(" ")){
			contentXML = generateDefaultXML(courseContext);
		}
		
		return contentXML;
	}
	
	private String generateDefaultXML(Context courseContext) {
		String defaultXML = "";
		
		CourseIDBB currentCourseID = new CourseIDBB();
		CourseMembershipBB currentCourseMembership = new CourseMembershipBB();
		
		currentCourseID.setCourse(courseContext.getCourseId());
		currentCourseMembership.setCourseMemberships(currentCourseID);
		
		Iterator<CourseMembership> memberships = currentCourseMembership.getIterator();
		
		defaultXML += "<course>";
		defaultXML += "<courseID>" + currentCourseID.toString() + "</courseID>";
		defaultXML += "<courseColor>blue</courseColor>";
		defaultXML += "<visibleStudents>";
		
		
		while (memberships.hasNext()) {
			CourseMembership selectedMember = memberships.next();
			
			if (memberships.hasNext()) {
				defaultXML += selectedMember.getUser().getStudentId() + ", ";
			}
			else {
				defaultXML += selectedMember.getUser().getStudentId();
			}
		}
		
		defaultXML += "</visibleStudents>";
		defaultXML += "<hiddenStudents></hiddenStudents>";
		defaultXML += "<fileExists>true</fileExists>";
		defaultXML += "<selectedGradebookColumn>Total: </selectedGradebookColumn>";
		
		memberships = currentCourseMembership.getIterator();
		while (memberships.hasNext()) {
			CourseMembership selectedMember = memberships.next();
			
			defaultXML += "<student>";
			defaultXML += "<studentID>" + selectedMember.getUser().getStudentId() + "</studentID>";
			defaultXML += "<userColor>blue</userColor>";
			defaultXML += "<otherColor>blue</otherColor>";
			defaultXML += "<studentColumnChoice></studentColumnChoice>";
			defaultXML += "<studentTimePeriod></studentTimePeriod>";
			defaultXML += "</student>";
			
		}
		
		for (int i = 0; i < 10; i++) {
			defaultXML += "<level>";
			defaultXML += "<levelID>" + i + "</levelID>";
			if (i == 0){
				defaultXML += "<levelPoints>0</levelPoints>";
				defaultXML += "<levelLabel>Novice</levelLabel>";
			}
			else if (i == 1){
				defaultXML += "<levelPoints>100</levelPoints>";
				defaultXML += "<levelLabel>Apprentice</levelLabel>";
			}
			else if (i == 2){
				defaultXML += "<levelPoints>200</levelPoints>";
				defaultXML += "<levelLabel>Journeyman</levelLabel>";
			}
			else if (i == 3){
				defaultXML += "<levelPoints>300</levelPoints>";
				defaultXML += "<levelLabel>Grand Master</levelLabel>";
			}
			else if (i == 4){
				defaultXML += "<levelPoints>400</levelPoints>";
				defaultXML += "<levelLabel>Done</levelLabel>";
			}
			else if (i == 5){
				defaultXML += "<levelPoints></levelPoints>";
				defaultXML += "<levelLabel></levelLabel>";
			}
			else if (i == 6){
				defaultXML += "<levelPoints></levelPoints>";
				defaultXML += "<levelLabel></levelLabel>";
			}
			else if (i == 7){
				defaultXML += "<levelPoints></levelPoints>";
				defaultXML += "<levelLabel></levelLabel>";
			}
			else if (i == 8){
				defaultXML += "<levelPoints></levelPoints>";
				defaultXML += "<levelLabel></levelLabel>";
			}
			else if (i == 9){
				defaultXML += "<levelPoints></levelPoints>";
				defaultXML += "<levelLabel></levelLabel>";
			}
			defaultXML += "</level>";
		}
		defaultXML += "</course>";
		
		return defaultXML;
	}
}