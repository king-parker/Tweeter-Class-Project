package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedStatusTask extends PagedTask<Status> {

    private static final String LOG_TAG = "PagedStatusTask";

    public PagedStatusTask(User targetUser, int limit, Status lastStatus, Handler messageHandler) {
        super(targetUser, limit, lastStatus, messageHandler);
    }

    // This method is public so it can be accessed by test cases
    @Override
    public void loadImages(List<Status> statuses) throws IOException {
        if (statuses != null) {
            for (Status s : statuses) {
                BackgroundTaskUtils.loadImage(s.getUser());
            }
        }
    }
}
