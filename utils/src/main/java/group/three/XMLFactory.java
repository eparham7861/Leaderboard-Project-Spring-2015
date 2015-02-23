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
	private int InstanceID = 0;
	
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
			case "levelPoints":
				if (InstanceID == 0){
					setContent(SavedContent.Content.LEVELINDEX0, content);
				}
				else if (InstanceID == 1){
					setContent(SavedContent.Content.LEVELINDEX1, content);
				}
				else if (InstanceID == 2){
					setContent(SavedContent.Content.LEVELINDEX2, content);
				}
				else if (InstanceID == 3){
					setContent(SavedContent.Content.LEVELINDEX3, content);
				}
				else if (InstanceID == 4){
					setContent(SavedContent.Content.LEVELINDEX4, content);
				}
				else if (InstanceID == 5){
					setContent(SavedContent.Content.LEVELINDEX5, content);
				}
				else if (InstanceID == 6){
					setContent(SavedContent.Content.LEVELINDEX6, content);
				}
				else if (InstanceID == 7){
					setContent(SavedContent.Content.LEVELINDEX7, content);
				}
				else if (InstanceID == 8){
					setContent(SavedContent.Content.LEVELINDEX8, content);
				}
				else if (InstanceID == 9){
					setContent(SavedContent.Content.LEVELINDEX9, content);
				}
				break;
			case "levellabel":
				if (InstanceID == 0){
				setContent(SavedContent.Content.LEVEL0, content);
				}
				if (InstanceID == 1){
				setContent(SavedContent.Content.LEVEL1, content);
				}
				if (InstanceID == 2){
				setContent(SavedContent.Content.LEVEL2, content);
				}
				if (InstanceID == 3){
				setContent(SavedContent.Content.LEVEL3, content);
				}
				if (InstanceID == 4){
				setContent(SavedContent.Content.LEVEL4, content);
				}
				if (InstanceID == 5){
				setContent(SavedContent.Content.LEVEL5, content);
				}
				if (InstanceID == 6){
				setContent(SavedContent.Content.LEVEL6, content);
				}
				if (InstanceID == 7){
				setContent(SavedContent.Content.LEVEL7, content);
				}
				if (InstanceID == 8){
				setContent(SavedContent.Content.LEVEL8, content);
				}
				if (InstanceID == 9){
				setContent(SavedContent.Content.LEVEL9, content);
				}
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
				setContent(SavedContent.Content.GRADECHOICE, content);
				break;
		}
	}
	
	public String convertAllToXML(){
		String visibleCount = getContent(SavedContent.Content.VISIBLE);
		String stringToXML = "<course>";
		
		stringToXML += "<courseID>" + getContent(SavedContent.Content.COURSE) + "</courseID>";
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
		
		for (int i = 0; i<10; i++){
			stringToXML += "<level>";
			stringToXML += "<id>" + i + "</id>";
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
	/*
	public void compareLevelIndexAndLevel(nodeName, content){
		int loop = 10;
		for (int i = 0; i<loop; i++){
			
		}
	}	
	*/
}