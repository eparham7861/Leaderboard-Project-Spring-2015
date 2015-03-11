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
<%@page import="com.spvsoftwareproducts.blackboard.utils.B2Context"%>
<bbNG:includedPage ctxId="ctx">
<%@include file="leaderboard_student.jsp" %>

	<%
		LeaderboardBB currentLeaderboard = new LeaderboardBB(ctx);
		
	%>		
	
		<script type="text/javascript">		
			// this is a rather kludgy fix for a bug introduced by Blackboard 9.1 SP 10.  It ensures that javascript files are loaded in the correct order, instead of haphazardly.
			<jsp:include page="js/jqueryNoConflict.js" />
			<jsp:include page="js/highcharts.js" />
			
	        dave(document).ready(function() {
	
				var gamegogyLeaderboardChart;			
				
				var seriesValues = [
	  				<%	
						out.print (currentLeaderboard.getSeriesValues());
	  					
	  				%>
	  				
	  				var studentNames = [
	 				<%		 
						out.print (currentLeaderboard.getStudentNames());
	 				%>
	 				
	 				gamegogyLeaderboardChart = new Highcharts.Chart({
					chart: {
						renderTo: 'leaderboardBlockChartContainer',
						type: 'bar',
							height: 150 + <%=currentLeaderboard.getNumberOfVisibleStudents()%>*30,
						<%
						out.print(currentLeaderboard.getSpacingTop());					
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
								out.print (currentLeaderboard.getPlotbands());
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
	<div id="leaderboardBlockChartContainer" <%if(currentLeaderboard.getNumberOfVisibleStudents() > 10) {%>style="overflow-y:scroll; overflow-x: hidden; min-height: 400px; max-height: 700px; padding-right: 10px;"<% } %>></div>
</bbNG:includedPage>