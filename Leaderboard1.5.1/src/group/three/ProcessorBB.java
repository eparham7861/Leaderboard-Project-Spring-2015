package group.three;
import blackboard.persist.*;
import blackboard.persist.content.*;
import blackboard.data.content.*;
import blackboard.data.course.*;
import blackboard.data.navigation.CourseToc;
import blackboard.data.user.User;
import blackboard.persist.course.CourseMembershipDbLoader;
import blackboard.persist.navigation.CourseTocDbLoader;
import blackboard.persist.user.UserDbLoader;
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
					courseDoc.setIsAvailable(true);
					courseDoc.setIsTracked(false);
					courseDoc.setIsDescribed(false);
					courseDoc.setLaunchInNewWindow(false);
					courseDoc.setAllowGuests(true);
					courseDoc.setAllowObservers(true);
					break;
				}
			}
		}
		catch (Exception e) {
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
		} catch (PersistenceException ex) {
		}
	}
	
	public String loadContent(Context courseContext) throws PersistenceException {
		String contentXML = "";
		
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
		
		if (contentXML.equals("")){
			contentXML = generateDefaultXML(courseContext);
		}
		
		return contentXML;
	}
	
	private String generateDefaultXML(Context courseContext) {
		String defaultXML = "";
		List <CourseMembership> currentCourseMembership;
		try {
			currentCourseMembership = CourseMembershipDbLoader.Default.getInstance().loadByCourseIdAndRole(currentCourseID, CourseMembership.Role.STUDENT, null, true);
		
			Iterator<CourseMembership> memberships = currentCourseMembership.iterator();
		
			defaultXML += "<course>";
			defaultXML += "<courseID>" + currentCourseID.toString() + "</courseID>";
			defaultXML += "<courseColor>#4572A7</courseColor>";
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
			
			memberships = currentCourseMembership.iterator();
		
			while (memberships.hasNext()) {
				CourseMembership selectedMember = memberships.next();
				defaultXML += "<student>";
				defaultXML += "<studentID>" + selectedMember.getUser().getStudentId() + "</studentID>";
				defaultXML += "<userColor>#4572A7</userColor>";
				defaultXML += "<otherColor>#4572A7</otherColor>";
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
					defaultXML += "<levelPoints>500</levelPoints>";
					defaultXML += "<levelLabel>Over Acheiver</levelLabel>";
				}
				else if (i == 6){
					defaultXML += "<levelPoints>600</levelPoints>";
					defaultXML += "<levelLabel>Papa G</levelLabel>";
				}
				else if (i == 7){
					defaultXML += "<levelPoints>700</levelPoints>";
					defaultXML += "<levelLabel>Travis</levelLabel>";
				}
				else if (i == 8){
					defaultXML += "<levelPoints>800</levelPoints>";
					defaultXML += "<levelLabel>Dr. Thornton</levelLabel>";
				}
				else if (i == 9){
					defaultXML += "<levelPoints>1000000</levelPoints>";
					defaultXML += "<levelLabel>Eric Lee</levelLabel>";
				}
				defaultXML += "</level>";
			}
			
			defaultXML += "</course>";
		}
		catch (PersistenceException e) {
			
		}
		return defaultXML;
	}
}