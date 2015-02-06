package group.three;

import org.junit.*;
import static org.junit.Assert.*;
import blackboard.data.user.User;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LeaderboardUserTest {
	
	private LeaderboardUser currentUser;
	private User user1;
	@Before
	public void startUp() {
		currentUser = new LeaderboardUser();
		user1 = mock(User.class);
		when(user1.getStudentId()).thenReturn("01234567");
		when(user1.getUserName()).thenReturn("eparham");
		when(user1.getGivenName()).thenReturn("Eric");
		when(user1.getFamilyName()).thenReturn("Parham");
	}
	
	@Test
	public void testSetCurrentUser() {
		currentUser.setCurrentUser(user1);
		assertEquals("User [username=eparham, name=Eric Parham]", currentUser.toString());
	}
}