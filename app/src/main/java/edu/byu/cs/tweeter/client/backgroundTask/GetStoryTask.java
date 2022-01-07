package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's story.
 */
public class GetStoryTask extends PagedStatusTask {

    private static final String LOG_TAG = "GetStoryTask";

    public GetStoryTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                        Handler messageHandler) {
        super(authToken, targetUser, limit, lastStatus, messageHandler);
    }

    @Override
    protected boolean runTask() throws IOException {
        Pair<List<Status>, Boolean> pageOfStatus = getFakeData().getPageOfStatus(lastItem, limit);
        this.items = pageOfStatus.getFirst();
        this.hasMorePages = pageOfStatus.getSecond();

        for (Status s : items) {
            BackgroundTaskUtils.loadImage(s.getUser());
        }

        return true;
    }
}