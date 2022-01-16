package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;

public abstract class AuthorizedTask extends BackgroundTask {

    private static final String LOG_TAG = "AuthorizedTask";

    protected AuthorizedTask(Handler messageHandler) {
        super(messageHandler);
    }

    public AuthToken getCurrUserAuthToken() {
        return Cache.getInstance().getCurrUserAuthToken();
    }

    public String getCurrUserAlias() {
        return Cache.getInstance().getCurrUser().getAlias();
    }
}
