package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public abstract class AuthorizedTask extends BackgroundTask {

    private static final String LOG_TAG = "AuthorizedTask";

    /**
     * Auth token for logged-in user.
     */
    protected AuthToken authToken;

    protected AuthorizedTask(AuthToken authToken, Handler messageHandler) {
        super(messageHandler);

        this.authToken = authToken;
    }
}
