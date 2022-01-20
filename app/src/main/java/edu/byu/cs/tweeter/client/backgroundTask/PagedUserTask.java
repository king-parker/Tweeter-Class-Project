package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedUserTask extends PagedTask<User> {

    private static final String LOG_TAG = "PagedUserTask";

    protected PagedUserTask(User targetUser, int limit, User lastUser, Handler messageHandler) {
        super(targetUser, limit, lastUser, messageHandler);
    }

    // This method is public so it can be accessed by test cases
    @Override
    public void loadImages(List<User> users) throws IOException {
        if (users != null) {
            for (User u : users) {
                BackgroundTaskUtils.loadImage(u);
            }
        }
    }
}
