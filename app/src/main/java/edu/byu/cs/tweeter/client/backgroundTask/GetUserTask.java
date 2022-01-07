package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Background task that returns the profile for a specified user.
 */
public class GetUserTask extends AuthorizedTask {

    private static final String LOG_TAG = "GetUserTask";

    public static final String USER_KEY = "user";

    /**
     * Alias (or handle) for user whose profile is being retrieved.
     */
    private String alias;
    /**
     * User whose profile is being retrieved.
     */
    private User selectedUser;

    public GetUserTask(AuthToken authToken, String alias, Handler messageHandler) {
        super(authToken, messageHandler);

        this.alias = alias;
    }

    @Override
    protected boolean runTask() {
        this.selectedUser = getFakeData().findUserByAlias(alias);

        return true;
    }

    @Override
    protected void loadBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, selectedUser);
    }
}
