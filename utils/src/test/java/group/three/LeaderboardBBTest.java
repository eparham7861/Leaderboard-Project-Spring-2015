package group.three;

import org.junit.*;
import static org.junit.Assert.*;
import blackboard.base.InitializationException;
import blackboard.data.user.User;
import blackboard.persist.*;
import javax.servlet.http.HttpServletRequest;
import blackboard.platform.context.ContextManager;
import blackboard.platform.context.Context;
import blackboard.platform.RuntimeBbServiceException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LeaderboardBBTest {
	
	private LeaderboardBB currentLeaderboard;
	private Context currentContext;
	private Id currentCourseID;
	private User currentUser;
	private String defaultPlotbands;
	
	@Before
	public void startUp() {
		currentUser = mock(User.class);
		when(currentUser.getStudentId()).thenReturn("01234567");
		when(currentUser.getUserName()).thenReturn("eparham");
		when(currentUser.getGivenName()).thenReturn("Eric");
		when(currentUser.getFamilyName()).thenReturn("Parham");
		
		currentCourseID = mock(Id.class);
		when(currentCourseID.toExternalString()).thenReturn("CS491");
		when(currentCourseID.toString()).thenReturn("CS491");
		
		currentContext = mock(Context.class);
		when(currentContext.getUser()).thenReturn(currentUser);
		when(currentContext.getCourseId()).thenReturn(currentCourseID);
		currentLeaderboard = new LeaderboardBB(currentContext);
		
		defaultPlotbands = "{ color: 'rgb(228, 228, 228)', from: 0, to: 0, label: { text: '',rotation: -35,align: 'center',textAlign: 'left', verticalAlign: 'top', y: -10, style: { color: '#666666'}}}, { color: 'rgb(216, 216, 216)', from: 0, to: 0, label: { text: 'Level 2',rotation: -35,textAlign: 'left',align: 'center', verticalAlign: 'top', y: -10, style: { color: '#666666', fontFamily: 'Verdana, sans-serif'}}}, { color: 'rgb(204, 204, 204)', from: 0, to: 0, label: { text: 'Level 3',rotation: -35,textAlign: 'left',align: 'center', verticalAlign: 'top', y: -10, style: { color: '#666666', fontFamily: 'Verdana, sans-serif'}}}, { color: 'rgb(192, 192, 192)', from: 0, to: 0, label: { text: 'Level 4',rotation: -35,textAlign: 'left',align: 'center', verticalAlign: 'top', y: -10, style: { color: '#666666', fontFamily: 'Verdana, sans-serif'}}}, { color: 'rgb(180, 180, 180)', from: 0, to: 0, label: { text: 'Level 5',rotation: -35,textAlign: 'left',align: 'center', verticalAlign: 'top', y: -10, style: { color: '#666666', fontFamily: 'Verdana, sans-serif'}}}, { color: 'rgb(168, 168, 168)', from: 0, to: 0, label: { text: 'Level 6',rotation: -35,textAlign: 'left',align: 'center', verticalAlign: 'top', y: -10, style: { color: '#666666', fontFamily: 'Verdana, sans-serif'}}}, { color: 'rgb(156, 156, 156)', from: 0, to: 0, label: { text: 'Level 7',rotation: -35,textAlign: 'left',align: 'center', verticalAlign: 'top', y: -10, style: { color: '#666666', fontFamily: 'Verdana, sans-serif'}}}, { color: 'rgb(144, 144, 144)', from: 0, to: 0, label: { text: 'Level 8',rotation: -35,textAlign: 'left',align: 'center', verticalAlign: 'top', y: -10, style: { color: '#666666', fontFamily: 'Verdana, sans-serif'}}}, { color: 'rgb(132, 132, 132)', from: 0, to: 0, label: { text: 'Level 9',rotation: -35,textAlign: 'left',align: 'center', verticalAlign: 'top', y: -10, style: { color: '#666666', fontFamily: 'Verdana, sans-serif'}}}, { color: 'rgb(120, 120, 120)', from: 0, to: 0, label: { text: 'Level 10',rotation: -35,textAlign: 'left',align: 'center', verticalAlign: 'top', y: -10, style: { color: '#666666', fontFamily: 'Verdana, sans-serif'}}}";
	}
	
	@Test
	public void testSeriesValues() {
		assertEquals("];", currentLeaderboard.getSeriesValues());
	}
	
	@Test
	public void testStudentNames() {
		assertEquals("];", currentLeaderboard.getStudentNames());
	}
	
	@Test
	public void testNumberOfVisibleStudents() {
		assertEquals(0, currentLeaderboard.getNumberOfVisibleStudents());
	}
	
	@Test
	public void testPlotbands() {
		assertEquals(defaultPlotbands, currentLeaderboard.getPlotbands());
	}
}