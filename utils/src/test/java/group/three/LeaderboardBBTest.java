package group.three;

import org.junit.*;
import static org.junit.Assert.*;
import blackboard.data.user.User;
import blackboard.persist.*;
import blackboard.platform.context.Context;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LeaderboardBBTest {
	
	private LeaderboardBB currentLeaderboard;
	private Context currentContext;
	private Id currentCourseID;
	private User currentUser;
	
	@Before
	public void startUp() {
		currentLeaderboard = new LeaderboardBB();
		
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
		
		currentLeaderboard.configureBoard(currentContext);
	}
}