package group.three;

import org.junit.*;
import static org.junit.Assert.*;
import blackboard.data.user.User;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserBBTest {
	
	private UserBB currentUser;
	private User user1;
	@Before
	public void startUp() {
		currentUser = new UserBB();
		user1 = mock(User.class);
		when(user1.getStudentId()).thenReturn("01234567");
		when(user1.getUserName()).thenReturn("eparham");
		when(user1.getGivenName()).thenReturn("Eric");
		when(user1.getFamilyName()).thenReturn("Parham");
		currentUser.setCurrentUser(user1);
	}
	
	@Test
	public void testGetToString() {
		assertEquals("User [username=eparham, name=Eric Parham]", currentUser.toString());
	}
	
	@Test
	public void testGetStudentID() {
		assertEquals("01234567", currentUser.getStudentID());
	}
	
	@Test
	public void testGetName() {
		assertEquals("Eric Parham", currentUser.getName());
	}
}