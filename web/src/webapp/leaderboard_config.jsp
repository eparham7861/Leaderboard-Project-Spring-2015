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
<%@ taglib prefix="bbUI" uri="/bbUI" %>
<%@page import="blackboard.servlet.data.MultiSelectBean"%>


<%@page import="com.spvsoftwareproducts.blackboard.utils.B2Context"%>
<bbNG:modulePage type="personalize" ctxId="ctx">
<%
	Leaderboard_Config leaderboardConfig = new Leaderboard_Config(ctx);
	
	// Grab previously saved color value
	leaderboardConfig.loadPreviousColorValues();
	
	// Grab previously saved level values and labels
	leaderboardConfig.loadPreviousLevelInformation();
%>

<%@include file="leaderboard_student.jsp" %>

<bbNG:pageHeader>
	<bbNG:pageTitleBar title="Leaderboard Configuration"></bbNG:pageTitleBar>
</bbNG:pageHeader>

<!-- Body Content: Plotbands & Color Picker -->
<bbNG:form action="leaderboard_save.jsp" method="post" name="plotband_config_form" id="plotband_config_form" onSubmit="return validateForm()">
	<bbNG:dataCollection>
		
			<%	
			//Changed to the below ensure is intructor
			%>
			
			<!-- Instructor flag submitted to save page - MAY BE UNSAFE -->
			<input type="hidden" name="instructor" value="<%= leaderboardConfig.ensureUserIsInstructor() %>" />
			
			<!-- Plotbands Configuration Form -->
			<% if (leaderboardConfig.ensureUserIsInstructor()){ %>
				<!-- Color Picker -->
				<bbNG:step title="Primary Bar Color">
					<bbNG:dataElement>
						<bbNG:elementInstructions text="Select a general plotband color."/>
						<bbNG:colorPicker name="color" initialColor="<%= leaderboardConfig.getColorValue() %>"/>
					</bbNG:dataElement>
				</bbNG:step>
			
				<bbNG:step title="Plotbands Points">
					<bbNG:dataElement>
						<bbNG:elementInstructions text="Set point requirements for each level. Everyone starts at Level 1. Note: Higher levels are not shown on the leaderboard until at least one student reaches that level." />
						<table>
							<!-- Fill up table with 10 levels.  Includes label & input field -->
							<tr>
								<th></th>
								<th style="text-align:center;"> Points Required</th>
								<th style="text-align:center;">Title</th>
							</tr>
							
							<% 
							//Establish levels for students based on XP
							//Currently, students start at an unnamed Level 1 and have a lelvel cap of 10.
							//We may want to change this where they start at 0 and can go to as many levels as the teacher allows.

							for (int i = 2; i<= 10; i++){
							%>
								<tr id="Level_<%= i %>">
									<td>Level <%= i %> </td>
									<input type="hidden" name="courseID" value="<%= leaderboardConfig.getCourseID().toExternalString() %>" /> <!--Have to use toExternalString() to get the courseID Key ;Used to pass the CourseID to leaderboard_save.jsp   -->
									<td><input type="text" name="Level_<%= i %>_Points" size="12" value="<%=leaderboardConfig.establishLevelValue(i)%>" onkeyup="checkForm()"/></td>
									<td><input type="text" name="Level_<%= i %>_Labels" size="18" value="<%=leaderboardConfig.establishLevelLabel(i)%> " /></td>
								</tr>
								
							<% } %>
						</table>
						<!-- Javascript Form Logic //-->
						<script type="text/javascript" src="<%= leaderboardConfig.getJSConfigFormpath() %>"></script>
					</bbNG:dataElement>
				</bbNG:step>
				
				<!-- Show/Hide Student Selection-->	
				<bbNG:step title="Show/Hide Students">
				<% 
				/*
				Started by Zack White.
				Last edit 3-9-14 by Tim Burch.
				*/
				
				//Create show/hide UI
				leaderboardConfig.createUserInterface();
				//Logic to determine if the default or a saved show/hide list is used
				if(leaderboardConfig.getModified().equals("true")){//modified.equals("true")){//A save file already exists.
					//Blackboard only saves and loads information in strings
					//Each list is saved as one string of names with the following format:
					//"firstName lastName, firstName lastName, etc."
					leaderboardConfig.createVisibleStudentList();
					leaderboardConfig.createHiddenStudentList();
					/*
					If the cmlist (entire course roster) is larger than both the hidden and visible lists, then
					a student must be missing from the lists. So this checks if any new student has been added
					to the course roster since Leaderboard has been uploaded.
					*/
					leaderboardConfig.checkRosterLargerThanCurrentLists();
					
					}// end of check for newly added student
				//}// end of if a save file already exists
				else{//If there isn't a config file saved, set default with everyone visible since lists haven't been created yet.
					leaderboardConfig.setDefaultEveryoneVisible();
				}
				

				%>
				
					<bbUI:multiSelect widgetName="show_n_hide" leftCollection="<%=leftList%>" rightCollection="<%=rightList%>" formName="container_form" leftTitle = "Visible Students" rightTitle = "Hidden Students"/>
				</bbNG:step>
				
				<!-- Grade Column Chooser -->
				<%
					//Create a string array for the levels and point values from the config file
					leaderboardConfig.getGradebookData();
				%>
				<bbNG:step title="Choose Grade Column">
					 <bbNG:dataElement>
					 	<bbNG:elementInstructions text=" Choose Grade column to be used." />
				        <bbNG:selectElement name="gradebook_column"  multiple= "false" >
				        	
				   				<bbNG:selectOptionElement value="<%= leaderboardConfig.getPrevGradeChoice() %>" optionLabel="<%= leaderboardConfig.getPrevGradeString() %>" />
				        	<% for(int i = 0; i < lgm.size(); i++) { 
				        		String gradeItem = gradeList[i]; 
				        		if(!gradeItem.equals(leaderboardConfig.getPrevGradeChoice())) {%>
				        		 <bbNG:selectOptionElement value="<%= gradeItem %>" optionLabel="<%= gradeItem %>"/>
				        	<% } 
				        	} %>
				        </bbNG:selectElement>
				     </bbNG:dataElement>
				</bbNG:step>
				
			<% } else { %>
				<!-- Color Picker -->
				<bbNG:step title="Everyone else's color">
					<bbNG:dataElement>
						<bbNG:colorPicker name="color" initialColor="<%= leaderboardConfig.getColorValue() %>" helpText="Select a general plotband color."/>
					</bbNG:dataElement>
				</bbNG:step>
				<bbNG:step title="Your bar's color">
					<bbNG:dataElement>
						<bbNG:colorPicker name="user_color" initialColor="<%= leaderboardConfig.getUserColorValue() %>" helpText="Choose a color for your own bar."/>
					</bbNG:dataElement>
				</bbNG:step>
			<% } %>
		<bbNG:stepSubmit />
	</bbNG:dataCollection>
</bbNG:form>
</bbNG:modulePage>
