package group.three;

import org.junit.*;
import static org.junit.Assert.*;
import blackboard.platform.gradebook2.*;
import blackboard.platform.gradebook2.impl.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GradebookManagerBBTest {
	private GradebookManagerBB currentGradebook;
	
	@Before
	public void startUp() {
		GradebookManager mockGradebook = mock(GradebookManager.class);
	}
	
	@Test
	public void testStartGradebook() {
		currentGradebook = new GradebookManagerBB();
	}
}