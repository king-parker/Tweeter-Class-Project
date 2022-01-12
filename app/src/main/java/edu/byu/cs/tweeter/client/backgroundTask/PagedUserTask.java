package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedUserTask extends PagedTask<User> {

    private static final String LOG_TAG = "PagedUserTask";

    protected PagedUserTask(AuthToken authToken, User targetUser, int limit, User lastUser, Handler messageHandler) {
        super(authToken, targetUser, limit, lastUser, messageHandler);
    }
}
