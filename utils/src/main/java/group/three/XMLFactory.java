package group.three;

import java.util.ArrayList;
import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class XMLFactory {
	private String XMLInputString;
	private ArrayList<String> studentScoreLabels;
	private SavedContent contentHolder;
	
	public XMLFactory(){
		XMLInputString = "";
		studentScoreLabels = new ArrayList<String>();
		contentHolder = new SavedContent();
	}
	
	public void setContent(SavedContent.Content contentName, String item) {
		contentHolder.setContentItem(contentName, item);
	}
	
	public String getContent(SavedContent.Content contentName) {
		return contentHolder.getContentItem(contentName);
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
				setContent(SavedContent.Content.COURSE, content);
				break;
			case "settings":
				setContent(SavedContent.Content.LEVELINDEX, content);
				break;
			case "levellabel":
				setContent(SavedContent.Content.LEVEL, content);
				break;
			case "visiblestudents":
				setContent(SavedContent.Content.VISIBLE, content);
				break;
			case "hiddenstudents":
				setContent(SavedContent.Content.HIDDEN, content);
				break;
			case "modified":
				setContent(SavedContent.Content.MODIFIED, content);
				break;
			case "usercolor":
				setContent(SavedContent.Content.USERCOLOR, content);
				break;
			case "backgroundcolor":
				setContent(SavedContent.Content.OTHERCOLOR, content);
				break;
			case "gradebooklabel":
				addGradebookLabel(content);
				break;
		}
	}
	
	public String convertAllToXML(){
		String visibleCount = getContent(SavedContent.Content.VISIBLE);
		String stringToXML = "<course>";
		
		stringToXML += "<courseID>" + getContent(SavedContent.Content.COURSE) + "</courseID>";
		stringToXML += "<setting>" + getContent(SavedContent.Content.LEVELINDEX) + "</setting>";
		stringToXML += "<levelLabel>" + getContent(SavedContent.Content.LEVEL) + "</levelLabel>";
		stringToXML += "<visibleStudents>" + visibleCount + "</visibleStudents>";
		stringToXML += "<hiddenStudents>" + getContent(SavedContent.Content.HIDDEN)  + "</hiddenStudents>";
		stringToXML += "<modified>" + getContent(SavedContent.Content.MODIFIED) + "</modified>";
		stringToXML += "<userColor>" + getContent(SavedContent.Content.USERCOLOR) + "</userColor>";
		stringToXML += "<backgroundColor>" + getContent(SavedContent.Content.OTHERCOLOR) + "</backgroundColor>";
		
		for (int i = 0; i < Integer.parseInt(visibleCount); i++) {
			stringToXML += "<student>";
			stringToXML += "<id>" + i + "</id>";
			stringToXML += "<gradebookLabel>" + studentScoreLabels.get(i) + "</gradebookLabel>";
			stringToXML += "</student>";
		}
		
		stringToXML += "</course>";
		
		return stringToXML;
	}
}