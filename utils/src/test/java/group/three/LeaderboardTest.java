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
		when(user1.getStudentId()).thenReturn("redshirt1");
		when(user1.getUserName()).thenReturn("username1");
		when(user1.getGivenName()).thenReturn("Name1");
		when(user1.getFamilyName()).thenReturn("Family1");
	}
	
	@Test
	public void testSetUser() {
		currentBoard.setCurrentUser(user1);
		assertEquals("User [username=username1, name= Name1 Family1]", currentBoard.toString());
	}
}