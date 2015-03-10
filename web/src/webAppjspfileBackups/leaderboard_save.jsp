<!-- .setSetting is used a lot here.  We'll have to discuss what function should be called
from XMLFactory since we have a different function for each instead of having an overloaded method
like blackboard's setSetting function call. - Jared -->



<%@page import="blackboard.base.*"%>
<%@page import="blackboard.data.course.*"%> 				<!-- for reading data -->
<%@page import="blackboard.data.user.*"%> 					<!-- for reading data -->
<%@page import="blackboard.persist.*"%> 					<!-- for writing data -->
<%@page import="blackboard.persist.course.*"%> 				<!-- for writing data -->
<%@page import="blackboard.platform.gradebook2.*"%>
<%@page import="blackboard.platform.gradebook2.impl.*"%>
<%@page import="java.util.*"%> 								<!-- for utilities -->
<%@page import="blackboard.platform.plugin.PlugInUtil"%>	<!-- for utilities -->
<%@ taglib uri="/bbData" prefix="bbData"%> 					<!-- for tags -->
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<%@page import="com.spvsoftwareproducts.blackboard.utils.B2Context"%>
<%@page import="blackboard.servlet.data.MultiSelectBean"%>

<%
String s = "<script>history.go(-2);</script>";

if (request.getMethod().equalsIgnoreCase("POST")) {
	/*
	JARED NOTE:  The following section might be useless now that we're not using B2Context.
	Look into removing it entirely.  Eric also believes that this part may be cut.
	*/
	if(request.getParameter("instructor").equals("true")) {
		// Create a new persistence object for course settings.  Don't save empty fields.
		B2Context b2Context_c = new B2Context(request);
		//XMLFactory XMLcontextC = new XMLFactory(); //JARED EDITED VERSION
		b2Context_c.setSaveEmptyValues(false);
		//XMLcontextC.setSaveEmptyValues(); //JARED EDITED VERSION
		
		int numFilledLevels = 10;
		String setting = "";
		String levelLabel =""; //ADDED 09/27/2013
		String courseID = request.getParameter("courseID"); //Gets courseID that was passed from leaderboard_config.jsp -JJL
		// Get level values from user-submitted data and add it to the persistence object.
		
		/*
		JARED NOTE:  The whole following section is due for editing and possibly removal.
		The function was to automatically count how many settings there were
		and make the list display only that many.  However, it doesn't work
		and we might as well cut it and keep it hardcoded like it is now.
		For now, I'm going to comment the whole thing out.
		
		for(int i = 1; i <= 10; i++) {
			setting = (i == 1)? "0":request.getParameter("Level_" + i + "_Points");
			levelLabel = (i == 1)? "0":request.getParameter("Level_" + i + "_Labels"); //ADDED 09/27/2013
			
			b2Context_c.setSetting(false, true, "Level_" + i + "_Points" + courseID, setting);
			//XMLcontextC.setSetting(); //JARED EDITED VERSION
			b2Context_c.setSetting(false, true, "Level_" + i + "_Labels" + courseID, levelLabel); //ADDED 09/27/2013
			//XMLcontextC.setSetting(); //JARED EDITED VERSION
			//Count the number of levels by subtracting empty strings from total available levels.
			if(setting == ""){
				numFilledLevels--;
			}
		}
		/*
		
		//Add number of levels key-pair to the persistence object.
		b2Context_c.setSetting(false,true,"num_filled_levels" + courseID,Integer.toString(numFilledLevels)); //Added courseID to num_filled to fix couse overide bug -JJL
		//XMLcontextC.setSetting(); //JARED EDITED VERSION - This part is setting how many levels there are.  It was supposed to count down, but it doesn't work.
		
		//Get gradebook column value from user-submitted data and add it to the persistence object.
		String gradeLabel=  b2Context_c.getRequestParameter("gradebook_column", "").trim();
		//String gradeLabel = XMLcontextC.getRequestParameter(); //JARED EDITED VERSION
		b2Context_c.setSetting(false, true, "gradebook_column" + courseID, gradeLabel);
		//XMLcontextC.setSetting(); //JARED EDITED VERSION

		//Show/hide settings Last edit 3-9-14 by Tim Burch.
		String leftVals = (request.getParameter("show_n_hide_left_values")).trim();
		String rightVals = (request.getParameter("show_n_hide_right_values")).trim();
		b2Context_c.setSetting(false, true, "visibleStudents" + courseID, leftVals);
		//XMLcontextC.setSetting(); //JARED EDITED VERSION
		b2Context_c.setSetting(false, true, "hiddenStudents" + courseID, rightVals);
		//XMLcontextC.setSetting(); //JARED EDITED VERSION
		b2Context_c.setSetting(false, true, "modified" + courseID, "true");
		//XMLcontextC.setSetting();
		
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
		
		//Number of visible students.
		b2Context_c.setSetting(false, true, "numVisibleStudents" + courseID, Integer.toString(numCommas));
		//XMLcontextC.setSetting(); //JARED EDITED VERSION
		
		// Save course settings
		b2Context_c.persistSettings(false, true);
		//XMLcontextC.persistSettings(); //JARED EDITED VERSION
	}
	
	// New persistence object for user-specific settings
	B2Context b2Context_u = new B2Context(request);
	//XMLFactory XMLcontextU = new XMLFactory(); //JARED EDITED VERSION
	
	// Get color value from user-submitted data and add it to the persistence object.
	b2Context_u.setSetting(true, false, "color", request.getParameter("color"));
	//XMLcontextU.setSetting(); //JARED EDITED VERSION
	b2Context_u.setSetting(true, false, "user_color", request.getParameter("user_color"));
	//XMLcontextU.setSetting(); //JARED EDITED VERSION
	
	
	// Save the settings (USER-SPECIFIC)
	b2Context_u.persistSettings(true, false);
	//XMLcontextU.persistSettings();
}

// May need error checking logic here (gaps in level fields, overlapping values, etc.)


%>

<%=s %>