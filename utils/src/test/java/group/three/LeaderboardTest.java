package group.three;

import org.junit.*;
import static org.junit.Assert.*;
import blackboard.data.user.User;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LeaderboardTest {
	
	private Leaderboard currentBoard;
	private User user1;
	@Before
	public void startUp() {
		currentBoard = new Leaderboard();
		user1 = mock(User.class);
		when(user1.getStudentId()).thenReturn("01234567");
		when(user1.getUserName()).thenReturn("eparham");
		when(user1.getGivenName()).thenReturn("Eric");
		when(user1.getFamilyName()).thenReturn("Parham");
	}
	
	@Test
	public void testSetUser() {
		currentBoard.setCurrentUser(user1);
		assertEquals("User [username=eparham, name=Eric Parham]", currentBoard.toString());
	}
}