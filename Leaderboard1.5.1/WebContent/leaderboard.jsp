<!--
	Gamegogy Leaderboard 1.4
    Copyright (C) 2014  David Thornton

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
-->

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
<%@page import="group.three.*"%>
<bbNG:includedPage ctxId="ctx">

	<%
		// get the current user
		User sessionUser = ctx.getUser();
		Id courseID = ctx.getCourseId();		
		String sessionUserRole = ctx.getCourseMembership().getRoleAsString();	
		String sessionUserID = sessionUser.getId().toString();
		
		XMLFactory currentXML = new XMLFactory();
		currentXML.setSessionUser(sessionUser);
		
		try {
			ProcessorBB loadProcessor = new ProcessorBB(courseID);
			currentXML.setXMLInputString(loadProcessor.loadContent(ctx));
			currentXML.setCurrentStudent(sessionUser.getStudentId());
		}
		catch (PersistenceException e) {
		
		}
		
		// use the GradebookManager to get the gradebook data
		GradebookManager gm = GradebookManagerFactory.getInstanceWithoutSecurityCheck();
		BookData bookData = gm.getBookData(new BookDataRequest(courseID));
		List<GradableItem> lgm = gm.getGradebookItems(courseID);
		// it is necessary to execute these two methods to obtain calculated students and extended grade data
		bookData.addParentReferences();
		bookData.runCumulativeGrading();
		// get a list of all the students in the class
		List <CourseMembership> cmlist = CourseMembershipDbLoader.Default.getInstance().loadByCourseIdAndRole(courseID, CourseMembership.Role.STUDENT, null, true);
		Iterator<CourseMembership> i = cmlist.iterator();
		List<Student> students = new ArrayList<Student>();
		
		// instructors will see student names
		boolean canUserSeeScores = false;
		String role = sessionUserRole.trim().toLowerCase();
		//out.print("dave, role is " + role);
		if (role.contains("instructor") || role.contains("faculty") || role.contains("teaching")  ) {
			canUserSeeScores = true;
		}	
		Double scoreToHighlight = -1.0;
		int index = 0;

		//Get number of visible students. This is used to determined height of the leaderboard.		
		String modified = currentXML.getContent(SavedContent.Content.FILEEXISTS);
		int numVisible = 0;
		if(modified.equals("true")){
			numVisible = Integer.parseInt(currentXML.getContent(SavedContent.Content.NUMVISIBLE));
		}
		else{
			numVisible = cmlist.size();
		}
		
		//retrieve grade column to be displayed. Use "Total" column if none has been provided, or if the previously chosen grade column has been deleted.
			String b2_grade_choice = "";
			b2_grade_choice = currentXML.getContent(SavedContent.Content.GRADECHOICE);
			
			String grade_choice = "total";
			for (int j = 0; j < lgm.size(); j++) { //check if grade column has been deleted.
				GradableItem gradeItem = (GradableItem) lgm.get(j);
				if (gradeItem.getTitle().equals(b2_grade_choice)) {
					grade_choice = b2_grade_choice;
				}
			}
		
			while (i.hasNext()) {	
			CourseMembership selectedMember = (CourseMembership) i.next();
			String currentUserID = selectedMember.getUserId().toString();
			
			for (int x = 0; x < lgm.size(); x++){			
				GradableItem gi = (GradableItem) lgm.get(x);					
				GradeWithAttemptScore gwas2 = bookData.get(selectedMember.getId(), gi.getId());
				Double currScore = 0.0;	
				
				if(gwas2 != null && !gwas2.isNullGrade()) {
					currScore = gwas2.getScoreValue();	 
				}						
				if (gi.getTitle().trim().toLowerCase().equalsIgnoreCase(grade_choice)) {
					if (sessionUserID.equals(currentUserID)) {
						scoreToHighlight = currScore;
					}
					/*
					Check each student enrolled in the class. If that student is either not on the hidden list or is the session
					user then add that student to the Leaderboard. Only run this check if the save file has been modified.
					Last edit 3-10-14 by Tim Burch.
					*/
					
					String sessionUserName = sessionUser.getGivenName() + " " + sessionUser.getFamilyName() + ": " + sessionUser.getUserName();
					if(modified.equals("true")){
						String[] hiddenArr = currentXML.getContent(SavedContent.Content.HIDDEN).split(",");
						String studentName = selectedMember.getUser().getGivenName() +" " + selectedMember.getUser().getFamilyName() + ": " + selectedMember.getUser().getUserName();
						for(int l = 0; l < hiddenArr.length; l++){
							if (studentName.equals(sessionUserName)){
								Student currentStudent = new Student();
								currentStudent.setStudentID(selectedMember.getUser().getStudentId());
								currentStudent.setFirstName(selectedMember.getUser().getGivenName());
								currentStudent.setLastName(selectedMember.getUser().getFamilyName());
								currentStudent.setScore(currScore);
								currentStudent.setUserName(selectedMember.getUser().getUserName());
								currentXML.setCurrentStudent(selectedMember.getUser().getStudentId());
								currentStudent.setStudentHighlightColor(request.getParameter("user_color"));
								currentStudent.setStudentGeneralColor(request.getParameter("color"));
								students.add(currentStudent);
								break;
							}
							else if (studentName.equals(hiddenArr[l])) {
								break;
							}
							else if(!studentName.equals(hiddenArr[l]) && l == hiddenArr.length - 1) {
								Student currentStudent = new Student();
								currentStudent.setStudentID(selectedMember.getUser().getStudentId());
								currentStudent.setFirstName(selectedMember.getUser().getGivenName());
								currentStudent.setLastName(selectedMember.getUser().getFamilyName());
								currentStudent.setScore(currScore);
								currentStudent.setUserName(selectedMember.getUser().getUserName());
								currentXML.setCurrentStudent(selectedMember.getUser().getStudentId());
								currentStudent.setStudentHighlightColor(request.getParameter("user_color"));
								currentStudent.setStudentGeneralColor(request.getParameter("color"));
								students.add(currentStudent);
							}
						}
					}
					else {
						Student currentStudent = new Student();
						currentStudent.setStudentID(selectedMember.getUser().getStudentId());
						currentStudent.setFirstName(selectedMember.getUser().getGivenName());
						currentStudent.setLastName(selectedMember.getUser().getFamilyName());
						currentStudent.setScore(currScore);
						currentStudent.setUserName(selectedMember.getUser().getUserName());
						currentXML.setCurrentStudent(selectedMember.getUser().getStudentId());
						currentStudent.setStudentHighlightColor(request.getParameter("user_color"));
						currentStudent.setStudentGeneralColor(request.getParameter("color"));
						students.add(currentStudent);
					}
				}		
			}
			index = index + 1;
		}
		Collections.sort(students);
		Collections.reverse(students);
			
	%>		
	
		<script type="text/javascript">		
			// this is a rather kludgy fix for a bug introduced by Blackboard 9.1 SP 10.  It ensures that javascript files are loaded in the correct order, instead of haphazardly.
			<jsp:include page="js/jqueryNoConflict.js" />
			<jsp:include page="js/highcharts.js" />
			
	        dave(document).ready(function() {
	
				var gamegogyLeaderboardChart;			
				
				var seriesValues = [
	  				<%	
	  				// Load saved color settings
	  				if(canUserSeeScores) {
	  					String user_color = currentXML.getContent(SavedContent.Content.COURSECOLOR);
						
						
	  					if(user_color == "") user_color = "#44aa22";
						
	  				
	  					boolean alreadyHighlighted = false;
	  					for (int x = 0; x < students.size(); x++){
	  						Double score = (Double) students.get(x).getScore();
	  						if (score == scoreToHighlight && !alreadyHighlighted) {
	  							alreadyHighlighted = true;
	  							out.print("{dataLabels: { enabled: true, style: {fontWeight: 'bold'} }, y:  " + score.toString() + ", color: '"+ user_color + "'}");
	  						}
	  						else {
	  							out.print("{y: " + score.toString() + ", color: '" + user_color + "'}");
	  						}
	  						if (x < students.size() -1) { out.print(","); }
	  						else { out.print("];"); }
	  					}
	  				}
	  				else {
	  					String user_color = currentXML.getContent(SavedContent.Content.USERCOLOR);
						
	  					String color = currentXML.getContent(SavedContent.Content.OTHERCOLOR);
						
	  					if(user_color == "") user_color = "#44aa22";
						
	  					if(color == "") color = "#4572A7";
	  				
	  					boolean alreadyHighlighted = false;
	  					for (int x = 0; x < students.size(); x++){
	  						Double score = (Double) students.get(x).getScore();
	  						if (score == scoreToHighlight && !alreadyHighlighted) {
	  							alreadyHighlighted = true;
	  							out.print("{dataLabels: { enabled: true, style: {fontWeight: 'bold'} }, y:  " + score.toString() + ", color: '"+ user_color + "'}");
	  						}
	  						else {
	  							out.print("{y: " + score.toString() + ", color: '" + color + "'}");
	  						}
	  						if (x < students.size() -1) { out.print(","); }
	  						else { out.print("];"); }
	  					}	
	  				}
  					
	  				%>
	  				
	  				var studentNames = [
	 				<%		 					
	 				//Add names to plotbands
						for (int x = 0; x < students.size(); x++){
							String firstName = (String) students.get(x).getFirstName();
							String lastName = (String) students.get(x).getLastName();
							String userName = students.get(x).getUserName();
								  							
							//If the user is an instructor (or other authorized user), all names are displayed
							if(canUserSeeScores){
  							out.print('"' + firstName.substring(0, 1) + ' ' + lastName + '"');   						
							}
							//If the user is a student, just output "You" next to this bar
							else if(sessionUser.getGivenName().equals(firstName) && sessionUser.getFamilyName().equals(lastName)
									&& sessionUser.getUserName().equals(userName)){
								out.print("'You'");
								//out.print('"' + firstName.substring(0, 1) + ' ' + lastName + '"');   						
							}	  							
							//Else, just add a number
							else {
								out.print(String.valueOf(x+1));
							}
							
							if (x < students.size() -1) { 
								out.print(","); 
							}
							else { 
								out.print("];"); 
							}
						}
	 				%>
	 				
	 				gamegogyLeaderboardChart = new Highcharts.Chart({
					chart: {
						renderTo: 'leaderboardBlockChartContainer',
						type: 'bar',
							height: 150 + <%=numVisible%>*30
						<%		// added by dave to remove unnecessary top spacing when no titles have been specified
						//Capture the number of levels as it was saved in the persistence object by leaderboard_save.jsp.
						String s =  "0";
						int filledLevels = (s == "")? 0: Integer.parseInt(s);
						if (filledLevels > 0) {
							out.print(",spacingTop: 95");	
						}						
						%>						 
					},
	                   plotOptions: {
	                       series: {
							pointPadding: 0,
							groupPadding: 0.1,
	                           borderWidth: 0,
	                           borderColor: 'gray',
	                           shadow: false
	                       }
	                   },
					legend: {  enabled: false  },  
					title: {
						text: null
					},						
					xAxis: {		
						rotation: -45,
						categories: studentNames,
						title: {
							text: null
						}
					},
					yAxis: {
						title: {
							text: null
						},						
						gridLineWidth: 0,
						labels: {
							
							enabled: false
						},
						offset: 20,
						plotBands: [
							<%
							//Capture the number of levels as it was saved in the persistence object by leaderboard_save.jsp.
							//String s =  b2Context.getSetting(false,true,"num_filled_levels" + courseID.toExternalString());
							//filledLevels = (s == "")? 0: Integer.parseInt(s);
							//out.print("dave: filled levels is <" + Integer.toString(filledLevels) + ">");
							int levelFrom = 0;
							int levelTo = 0;
							String levelLabel = ""; //ADDED 09/27/2013 HOLDS THE LEVEL LABEL
							
							//For each level where a custom value was provided...
							for(int j = 1; j<=filledLevels; j++){
								//Gather the target value to achieve the current level from the persistence object.
								levelFrom = Integer.parseInt(currentXML.getLevelValue(j));
								
								if(j == filledLevels) {
									levelTo = levelFrom * 2;
								} else {
									levelTo = Integer.parseInt(currentXML.getLevelValue(j + 1));
								}
								//Calculate the correct gradient color using RGB and dividing with respect to filledLevels.
								int gradient = (255/(filledLevels+10))*((filledLevels+10)-j);
								
								//Gets the current level title if there is not title sets it to a default level lable ADDED 09/27/2013
								levelLabel = currentXML.getLevelName(j);
								//levelLabel = xmlFactory.getSetting(); //JARED EDITED VERSION
								//out.print("dave: <" + levelLabel + ">");
								if (levelLabel == "") {
									levelLabel = "Level " +j; //Sets the default level title
								}
								//Output javascript for each highchart plotband.
								out.print("{ color: 'rgb("+gradient+", "+gradient+", "+gradient+")', ");
								out.print("from: "+levelFrom+", ");
								out.print("to: "+levelTo+", ");
								if (j == 1) {
									out.print("label: { text: '',rotation: -35,align: 'center',textAlign: 'left', verticalAlign: 'top', y: -10, style: { color: '#666666'}}}"); //Adds rotation to labels -Jll
								}
								else { 
									out.print("label: { text: '"+ levelLabel +"',rotation: -35,textAlign: 'left',align: 'center', verticalAlign: 'top', y: -10, style: { color: '#666666', fontFamily: 'Verdana, sans-serif'}}}"); //Adds rotation to labels -Jll
								}
								//Add commas after plotband elements until the last element which does not need one.
								if(j<filledLevels) {
									out.print(",");
								}
							}
							%>
						]
					
					},
					tooltip: {
						formatter: function() {
							return this.y;
						}
					},
					
					credits: {
						enabled: false
					},
					series: [{
						name: 'XP',
						data: seriesValues
					}]
				}); //end of chart
			}); // end of ready function									  		
		</script>	
	<div id="leaderboardBlockChartContainer" <%if(numVisible > 10) {%>style="overflow-y:scroll; overflow-x: hidden; min-height: 400px; max-height: 700px; padding-right: 10px;"<% } %>></div>
</bbNG:includedPage>