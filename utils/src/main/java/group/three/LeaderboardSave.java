package group.three;
import blackboard.base.*;
import blackboard.data.course.*;
import blackboard.data.user.*;
import blackboard.persist.*;
import blackboard.persist.course.*;
import blackboard.platform.gradebook2.*;
import blackboard.platform.gradebook2.impl.*;
import java.util.*;
import blackboard.platform.plugin.PlugInUtil;
import blackboard.servlet.data.MultiSelectBean;

if (request.getMethod().toLowerCase.equals("post")){
	public class LeaderboardSave(){
		
		public void saveToPersistanceObject(){
				if(request.getParameter("instructor").equals("true")){
					XMLFactory XMLcontextC = new XMLFactory();
					XMLcontextC.setSaveEmptyValues();
					int numFilledLevels = 10;
					int numOfStudents;
					String setting = "";
					String levelLabel = ""; // added 9/27/2013
					saveCourseID();
					saveGradebookColumnValue();
					String leftVals = (request.getParameter("show_n_hide_left_values")).trim();
					String rightVals = (request.getParameter("show_n_hide_right_values")).trim();
					saveShowHideSettings(leftVals, rightVals);
					numOfStudents = this.getNumberOfStudents(leftVals);
					saveNumberOfStudents(int NumStudents, XMLcontextC);
					saveCourseSettings(XMLcontextC);
				}
				// New persistence object for user-specific settings
				//B2Context b2Context_u = new B2Context(request);
				XMLFactory XMLcontextU = new XMLFactory(); //JARED EDITED VERSION
				setStudentColors(XMLcontextU);
				saveStudentSettings(XMLcontextU);
		}
		
		public void saveCourseID(XMLFactory XMLcontextC){
			String courseID = request.getParameter("courseID");
			//Add number of levels key-pair to the persistance object
			//b2Context_c.setSetting(false, true, "gradebook_column" + courseID, gradeLabel);
			XMLContextC.setSetting();
		}
		
		public void saveGradebookColumnValue(XMLFactory XMLcontextC){
			//Get gradebook column value from user-submitted data and add it to the persistence object.
			String gradeLabel=  b2Context_c.getRequestParameter("gradebook_column", "").trim();
			//String gradeLabel = XMLcontextC.getRequestParameter(); //JARED EDITED VERSION
			//b2Context_c.setSetting(false, true, "gradebook_column" + courseID, gradeLabel);
			XMLcontextC.setSetting();
		}
		
		public void saveShowHideSettings(String leftVals, String rightVals, XMLFactory XMLcontextC){
			//Show/hide settings Last edit 3-9-14 by Tim Burch.
			//b2Context_c.setSetting(false, true, "visibleStudents" + courseID, leftVals);
			XMLcontextC.setSetting();
			//b2Context_c.setSetting(false, true, "hiddenStudents" + courseID, rightVals);
			XMLcontextC.setSetting();
			//b2Context_c.setSetting(false, true, "modified" + courseID, "true");
			XMLcontextC.setSetting();
		}
		
		public int getNumberOfStudents(String leftVals){
			//Parse leftVals string to get number of students (commas)
			// JARED NOTE: Is there a better way to do this?  Counting students by commans seems odd. The matter is of low importance.
			int numCommas = 0;
			if(leftVals.length() > 0){
				char[] studentCharArray = leftVals.toCharArray();
				for(char c : studentCharArray){
					if(c == ','){
						numCommas += 1;
					}
				}
				numCommas += 1;//For example if you have only one student, there wouldn't be a comma.
			}
			return numCommas;
		}
		
		public void saveNumberOfStudents(int NumStudents, XMLFactory XMLcontextC){
			//Number of visible students.
			//b2Context_c.setSetting(false, true, "numVisibleStudents" + courseID, Integer.toString(numCommas));
			XMLcontextC.setSetting();
		}
		
		public void saveCourseSettings(XMLFactory XMLcontextC){
			//b2Context_c.persistSettings(false, true);
			XMLcontextC.persistSettings();
		}
		
		public void setStudentColors(XMLFactory XMLcontextU){
			// Get color value from user-submitted data and add it to the persistence object.
			//b2Context_u.setSetting(true, false, "color", request.getParameter("color"));
			XMLcontextU.setSetting(); //JARED EDITED VERSION
			//b2Context_u.setSetting(true, false, "user_color", request.getParameter("user_color"));
			XMLcontextU.setSetting(); //JARED EDITED VERSION
		}
		
		public void saveStudentSettings(XMLFactory XMLcontextU){
			// Save the settings (USER-SPECIFIC)
			//b2Context_u.persistSettings(true, false);
			XMLcontextU.persistSettings();
		}
	}
}