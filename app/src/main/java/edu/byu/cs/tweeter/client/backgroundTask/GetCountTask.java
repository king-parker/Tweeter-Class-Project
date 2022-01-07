package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class GetCountTask extends  AuthorizedTask {

    private static final String LOG_TAG = "GetCountTask";

    public static final String COUNT_KEY = "count";
    public static final String FOLLOWING_KEY = "forFollowing";

    /**
     * The user whose count is being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    protected User targetUser;
    /**
     * Indicate if the count if for following or for followers.
     */
    protected boolean forFollowing;

    /**
     * The count returned by the server
     */
    protected int count;

    protected GetCountTask(AuthToken authToken, User targetUser, boolean forFollowing, Handler messageHandler) {
        super(authToken, messageHandler);

        this.targetUser = targetUser;
        this.forFollowing = forFollowing;
    }

    @Override
    protected void loadBundle(Bundle msgBundle) {
        msgBundle.putInt(COUNT_KEY, count);
        msgBundle.putBoolean(FOLLOWING_KEY, forFollowing);
    }
}
