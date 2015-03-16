package group.three;
// Visible is used here - JS

import java.util.ArrayList;
import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class XMLFactory {
	private String XMLInputString, studentID;
	private List<Student> students;
	private SavedContent contentHolder;
	private int instanceID;
	private boolean isStudent;
	
	public XMLFactory(){
		XMLInputString = "";
		studentID = "";
		students = new ArrayList<Student>();
		contentHolder = new SavedContent();
		isStudent = false;
	}
	
	public void setContent(SavedContent.Content contentName, String item) {
		contentHolder.setContentItem(contentName, item);
		setNumberOfVisibleStudents(contentName);
	}
	
	public String getContent(SavedContent.Content contentName) {
		return contentHolder.getContentItem(contentName);
	}
	
	public void setStudentList(ArrayList<Student> students) {
		this.students = students;
	}
	
	public String getLevelName(int index){
		return contentHolder.getContentLevelLabel(index);
	}
	
	public String getLevelValue(int index){
		return contentHolder.getContentLevelValue(index);
	}
	
	private void setNumberOfVisibleStudents(SavedContent.Content contentName) {
		if (contentName == SavedContent.Content.VISIBLE) {
			String visibleStudents = contentHolder.getContentItem(contentName);
			String[] students = visibleStudents.split(",");
			contentHolder.setContentItem(SavedContent.Content.NUMVISIBLE, Integer.toString(students.length));
		}
	}
	
	public void setXMLInputString(String xml){
		this.XMLInputString = xml;
	}
	
	public void setCurrentStudent(String studentID) {
		this.studentID = studentID;
		constructXML();
	}
	
	private void constructXML() {
		/*
			This process builds a document which takes the input source of XML
			the document will be of extension XML.
			The exceptions should only be caught during catastrophic failure.
		*/
		try{
		
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(XMLInputString)));
			NodeList nodeList = document.getDocumentElement().getChildNodes();
		
			getDataFromXML(nodeList);
			
		}catch(ParserConfigurationException e) {
		}catch(SAXException e) {
		}catch(IOException e) {
		}
	}
	
	private void getDataFromXML(NodeList currentSavedContent) {
		/*
			Traverse through parent nodes and get their content.
			getItemFromContentNode is a path to add children of these
			parent nodes if they exist.
		*/
		for (int i = 0; i < currentSavedContent.getLength(); i++) 
		{
			Node node = currentSavedContent.item(i);

			if (node instanceof Element) 
			{
				String nodeName = node.getNodeName().toLowerCase();
				String content = node.getLastChild().getTextContent().trim();
				
				assignContent(nodeName, content);
				
				NodeList childNodes = node.getChildNodes();
				
				getItemFromContentNode(childNodes);
				
				
			}
		}	
	}
	
	private void getItemFromContentNode(NodeList currentItem) {
		for (int i = 0; i < currentItem.getLength(); i++) 
		{
			Node cNode = currentItem.item(i);

			if (cNode instanceof Element) 
			{
				String nodeName = cNode.getNodeName().toLowerCase();
				String content = cNode.getLastChild().getTextContent().trim();
				
				assignContent(nodeName, content);
			}
		}
	}
	
	private void assignContent(String nodeName, String content) {
		/*
			This is a hard-coded way of handling content.
		*/
		switch(nodeName) {
			case "courseid":
				setContent(SavedContent.Content.COURSE, content);
				break;
			case "levelid":
				instanceID = Integer.parseInt(content);
			case "levelPoints":
				if (instanceID == 0){
					setContent(SavedContent.Content.LEVELINDEX0, content);
				}
				else if (instanceID == 1){
					setContent(SavedContent.Content.LEVELINDEX1, content);
				}
				else if (instanceID == 2){
					setContent(SavedContent.Content.LEVELINDEX2, content);
				}
				else if (instanceID == 3){
					setContent(SavedContent.Content.LEVELINDEX3, content);
				}
				else if (instanceID == 4){
					setContent(SavedContent.Content.LEVELINDEX4, content);
				}
				else if (instanceID == 5){
					setContent(SavedContent.Content.LEVELINDEX5, content);
				}
				else if (instanceID == 6){
					setContent(SavedContent.Content.LEVELINDEX6, content);
				}
				else if (instanceID == 7){
					setContent(SavedContent.Content.LEVELINDEX7, content);
				}
				else if (instanceID == 8){
					setContent(SavedContent.Content.LEVELINDEX8, content);
				}
				else if (instanceID == 9){
					setContent(SavedContent.Content.LEVELINDEX9, content);
				}
				break;
			case "levellabel":
				if (instanceID == 0){
				setContent(SavedContent.Content.LEVEL0, content);
				}
				if (instanceID == 1){
				setContent(SavedContent.Content.LEVEL1, content);
				}
				if (instanceID == 2){
				setContent(SavedContent.Content.LEVEL2, content);
				}
				if (instanceID == 3){
				setContent(SavedContent.Content.LEVEL3, content);
				}
				if (instanceID == 4){
				setContent(SavedContent.Content.LEVEL4, content);
				}
				if (instanceID == 5){
				setContent(SavedContent.Content.LEVEL5, content);
				}
				if (instanceID == 6){
				setContent(SavedContent.Content.LEVEL6, content);
				}
				if (instanceID == 7){
				setContent(SavedContent.Content.LEVEL7, content);
				}
				if (instanceID == 8){
				setContent(SavedContent.Content.LEVEL8, content);
				}
				if (instanceID == 9){
				setContent(SavedContent.Content.LEVEL9, content);
				}
				break;
			case "visiblestudents":
				setContent(SavedContent.Content.VISIBLE, content);
				break;
			case "hiddenstudents":
				setContent(SavedContent.Content.HIDDEN, content);
				break;
			case "fileExists":
				setContent(SavedContent.Content.FILEEXISTS, content);
				break;
			case "studentid":
				checkCurrentStudent(content);
			case "usercolor":
				if (isStudent) {
					setContent(SavedContent.Content.USERCOLOR, content);
				}
				break;
			case "othercolor":
				if (isStudent) {
					setContent(SavedContent.Content.OTHERCOLOR, content);
				}
				break;
			case "selectedGradebookColumn":
				setContent(SavedContent.Content.GRADECHOICE, content);
				break;
		}
	}
	
	private void checkCurrentStudent(String studentID) {
		if (this.studentID == studentID) {
			isStudent = true;
		}
		else {
			isStudent = false;
		}
	}
	
	public String convertAllToXML(){
		String visibleStudents = getContent(SavedContent.Content.VISIBLE);
		String stringToXML = "<course>";
		int visibleStudentCount = getStudentCount(visibleStudents);
		
		stringToXML += "<courseID>" + getContent(SavedContent.Content.COURSE) + "</courseID>";
		stringToXML += "<courseColor>" + getContent(SavedContent.Content.COURSECOLOR) + "</courseColor>";
		stringToXML += "<visibleStudents>" + visibleStudents  + "</visibleStudents>";
		stringToXML += "<hiddenStudents>" + getContent(SavedContent.Content.HIDDEN)  + "</hiddenStudents>";
		stringToXML += "<fileExists>" + getContent(SavedContent.Content.FILEEXISTS) + "</fileExists>";
		stringToXML += "<selectedGradebookColumn>" + getContent(SavedContent.Content.GRADECHOICE) + "</selectedGradebookColumn>";
		
		for (int i = 0; i < visibleStudentCount; i++) {
			//this is a dirty temporary fix for our current tests
			//the else statement should be the only statement
			if (students.size() == 0) {
				stringToXML += "<student>";
				stringToXML += "<studentID>" + i + "</studentID>";
			}
			else {
				String studentID = students.get(i).getStudentID();
				setCurrentStudent(studentID);
				
				stringToXML += "<student>";
				stringToXML += "<studentID>" + studentID + "</studentID>";
			}
			stringToXML += "<userColor>" + getContent(SavedContent.Content.USERCOLOR) + "</userColor>";
			stringToXML += "<otherColor>" + getContent(SavedContent.Content.OTHERCOLOR) + "</otherColor>";
			stringToXML += "<studentColumnChoice>" + getContent(SavedContent.Content.COLUMNCHOICE) + "</studentColumnChoice>";
			stringToXML += "<studentTimePeriod>" + getContent(SavedContent.Content.PERIOD) + "</studentTimePeriod>";
			stringToXML += "</student>";
		}
		
		for (int i = 0; i<10; i++){
			stringToXML += "<level>";
			stringToXML += "<levelID>" + i + "</levelID>";
			if (i == 0){
				stringToXML += "<levelPoints>" + getContent(SavedContent.Content.LEVELINDEX0) + "</levelPoints>";
				stringToXML += "<levelLabel>" + getContent(SavedContent.Content.LEVEL0) + "</levelLabel>";
			}
			else if (i == 1){
				stringToXML += "<levelPoints>" + getContent(SavedContent.Content.LEVELINDEX1) + "</levelPoints>";
				stringToXML += "<levelLabel>" + getContent(SavedContent.Content.LEVEL1) + "</levelLabel>";
			}
			else if (i == 2){
				stringToXML += "<levelPoints>" + getContent(SavedContent.Content.LEVELINDEX2) + "</levelPoints>";
				stringToXML += "<levelLabel>" + getContent(SavedContent.Content.LEVEL2) + "</levelLabel>";
			}
			else if (i == 3){
				stringToXML += "<levelPoints>" + getContent(SavedContent.Content.LEVELINDEX3) + "</levelPoints>";
				stringToXML += "<levelLabel>" + getContent(SavedContent.Content.LEVEL3) + "</levelLabel>";
			}
			else if (i == 4){
				stringToXML += "<levelPoints>" + getContent(SavedContent.Content.LEVELINDEX4) + "</levelPoints>";
				stringToXML += "<levelLabel>" + getContent(SavedContent.Content.LEVEL4) + "</levelLabel>";
			}
			else if (i == 5){
				stringToXML += "<levelPoints>" + getContent(SavedContent.Content.LEVELINDEX5) + "</levelPoints>";
				stringToXML += "<levelLabel>" + getContent(SavedContent.Content.LEVEL5) + "</levelLabel>";
			}
			else if (i == 6){
				stringToXML += "<levelPoints>" + getContent(SavedContent.Content.LEVELINDEX6) + "</levelPoints>";
				stringToXML += "<levelLabel>" + getContent(SavedContent.Content.LEVEL6) + "</levelLabel>";
			}
			else if (i == 7){
				stringToXML += "<levelPoints>" + getContent(SavedContent.Content.LEVELINDEX7) + "</levelPoints>";
				stringToXML += "<levelLabel>" + getContent(SavedContent.Content.LEVEL7) + "</levelLabel>";
			}
			else if (i == 8){
				stringToXML += "<levelPoints>" + getContent(SavedContent.Content.LEVELINDEX8) + "</levelPoints>";
				stringToXML += "<levelLabel>" + getContent(SavedContent.Content.LEVEL8) + "</levelLabel>";
			}
			else if (i == 9){
				stringToXML += "<levelPoints>" + getContent(SavedContent.Content.LEVELINDEX9) + "</levelPoints>";
				stringToXML += "<levelLabel>" + getContent(SavedContent.Content.LEVEL9) + "</levelLabel>";
			}
			stringToXML += "</level>";
		}
		
		stringToXML += "</course>";
		
		return stringToXML;
	}
	
	private int getStudentCount(String visibleStudents) {
		String[] students = visibleStudents.split(",");
		
		return students.length;
	}
}