package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedUserTask extends PagedTask<User> {

    private static final String LOG_TAG = "PagedUserTask";

    protected PagedUserTask(AuthToken authToken, User targetUser, int limit, User lastUser, Handler messageHandler) {
        super(authToken, targetUser, limit, lastUser, messageHandler);
    }

    // This method is public so it can be accessed by test cases
    public void loadImages(List<User> followees) throws IOException {
        for (User u : followees) {
            BackgroundTaskUtils.loadImage(u);
        }
    }
}
