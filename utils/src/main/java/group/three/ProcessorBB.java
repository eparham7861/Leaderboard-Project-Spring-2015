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
			contentXML = "empty";
		}
		
		return contentXML;
	}
}