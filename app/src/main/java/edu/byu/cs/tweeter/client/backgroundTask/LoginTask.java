package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;

import java.io.IOException;

/**
 * Background task that logs in a user (i.e., starts a session).
 */
public class LoginTask extends AuthenticationTask {

    private static final String LOG_TAG = "LoginTask";

    public LoginTask(String username, String password, Handler messageHandler) {
        super(username, password, messageHandler);
    }

    @Override
    protected boolean runTask() throws IOException {
        this.user = getFakeData().getFirstUser();
        this.authToken = getFakeData().getAuthToken();

        BackgroundTaskUtils.loadImage(user);

        return true;
    }
}
