package group.three;

import java.util.ArrayList;
import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class XMLFactory {
	private String courseID;
	private int settingNumber, visibleStudents, hiddenStudents;
	private String levelLabel;
	private String gradebookLabel;
	private String isModified;
	private String backgroundColor, userColor;
	private ArrayList<String> studentScoreLabels;
	private String XMLInputString;
	
	public XMLFactory(){
		courseID = "";
		settingNumber = 0;
		visibleStudents = 1;
		hiddenStudents = 0;
		levelLabel = "";
		gradebookLabel = "";
		isModified = "";
		backgroundColor = "null";
		userColor = "null";
		studentScoreLabels = new ArrayList<String>();
		XMLInputString = "";
	}
	
	public void setCourseID(String id){
		this.courseID = id;
	}
	
	public void setSetting(int settingNum){
		this.settingNumber = settingNum;
	}
	
	public void setLevelLabel(String label){
		this.levelLabel = label;
	}
	
	public void setGradebookLabel(String gradeLabel){
		this.gradebookLabel = gradeLabel;
	}
	
	/*Note: We are testing this version of setGradebookLabel*/
	public void setGradebookLabel(int index, String gradeLabel){
		studentScoreLabels.set(index, gradeLabel);
	}
	
	public void addGradebookLabel(String gradeLabel){
		studentScoreLabels.add(gradeLabel);
	}
	
	public void removeGradebookLabel(String gradeLabel){
		studentScoreLabels.remove(gradeLabel);
	}
	
	public String getGradebookLabel(int index){
		return studentScoreLabels.get(index);
	}
	/*End area of the functions being currently tested*/
	
	public void setVisibleStudents(int visibleStudents) {
		this.visibleStudents = visibleStudents;
	}
	
	public void setHiddenStudentAmount(int amount){
		this.hiddenStudents = amount;
	}
	
	public void setModifiedValue(String modifyBooleanString){
		this.isModified = modifyBooleanString;
	}
	
	public void setBackgroundColor(String colValue){
		this.backgroundColor = colValue;
	}
	
	public void setUserColor(String userColorValue){
		this.userColor = userColorValue;
	}
	
	public void setXMLInputString(String xml){
		this.XMLInputString = xml;
		constructXML();
	}
	
	private void constructXML() {
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
	
	private void getDataFromXML(NodeList currentSavedContent) 
	{
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
	
	private void getItemFromContentNode(NodeList currentItem) 
	{
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
		switch(nodeName) {
			case "courseid":
				setCourseID(content);
				break;
			case "settings":
				setSetting(Integer.parseInt(content));
				break;
			case "levellabel":
				setLevelLabel(content);
				break;
			case "visiblestudents":
				setVisibleStudents(Integer.parseInt(content));
				break;
			case "hiddenstudents":
				setHiddenStudentAmount(Integer.parseInt(content));
				break;
			case "modified":
				setModifiedValue(content);
				break;
			case "usercolor":
				setUserColor(content);
				break;
			case "backgroundcolor":
				setBackgroundColor(content);
				break;
			case "gradebooklabel":
				addGradebookLabel(content);
				break;
		}
	}
	
	public String getCourseID(){
		return this.courseID;
	}
	
	public int getSetting(){
		return this.settingNumber;
	}
	
	public String getLevelLabel(){
		return this.levelLabel;
	}
	
	public int getVisibleStudents() {
		return this.visibleStudents;
	}
	
	public int getHiddenStudentAmount(){
		return this.hiddenStudents;
	}
	
	public String getGradebookLabel(){
		return this.gradebookLabel;
	}
	
	public String getModifiedValue(){
		return this.isModified;
	}
	
	public String getBackgroundColor(){
		return this.backgroundColor;
	}
	
	public String getUserColor(){
		return this.userColor;
	}
	
	public String convertAllToXML(){
		String stringToXML = "<course>";
		stringToXML += "<courseID>" + courseID + "</courseID>";
		stringToXML += "<setting>" + Integer.toString(settingNumber) + "</setting>";
		stringToXML += "<levelLabel>" + levelLabel + "</levelLabel>";
		stringToXML += "<visibleStudents>" + visibleStudents + "</visibleStudents>";
		stringToXML += "<hiddenStudents>" + hiddenStudents  + "</hiddenStudents>";
		stringToXML += "<modified>" + isModified + "</modified>";
		stringToXML += "<userColor>" + userColor + "</userColor>";
		stringToXML += "<backgroundColor>" + backgroundColor + "</backgroundColor>";
		
		for (int i = 0; i < visibleStudents; i++) {
			stringToXML += "<student>";
			stringToXML += "<id>" + i + "</id>";
			//stringToXML += "<gradebookLabel>" + gradebookLabel + "</gradebookLabel>";
			stringToXML += "<gradebookLabel>" + studentScoreLabels.get(i) + "</gradebookLabel>";
			stringToXML += "</student>";
		}
		
		stringToXML += "</course>";
		
		return stringToXML;
	}
}