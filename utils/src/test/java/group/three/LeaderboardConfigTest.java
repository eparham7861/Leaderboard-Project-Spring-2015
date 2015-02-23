package group.three;

import org.junit.*;
import static org.junit.Assert.*;
import blackboard.base.InitializationException;
//import blackboard.data.user.User;
//import blackboard.persist.*;
import javax.servlet.http.HttpServletRequest;
import blackboard.platform.context.ContextManager;
import blackboard.platform.context.Context;
import blackboard.platform.RuntimeBbServiceException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import blackboard.base.*;
import blackboard.data.course.*;
import blackboard.data.user.*;
import blackboard.persist.*;
import blackboard.persist.course.*;
import blackboard.platform.gradebook2.*;
import blackboard.platform.gradebook2.impl.*;
import java.util.*;
import blackboard.platform.plugin.PlugInUtil;
import blackboard.servlet.data.MultiSelectBean;
import blackboard.platform.security.authentication.BbSecurityException;
import blackboard.platform.context.Context;

public class LeaderboardConfigTest{
		private Leaderboard_Config leaderboardConfig;
		private Context context;
		private Id currentCourseId;
		
		@Before
		public void startUp() {
				
				currentCourseId = mock(Id.class);
				when(currentCourseId.toExternalString()).thenReturn("CS491");
				when(currentCourseId.toString()).thenReturn("CS491");
				
				context = mock(Context.class);
				when(context.getCourseId()).thenReturn(currentCourseId);
				
				leaderboardConfig = new Leaderboard_Config(context);
		}
		
}