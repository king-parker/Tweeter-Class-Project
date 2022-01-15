package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;

/**
 * Background task that returns the profile for a specified user.
 */
public class GetUserTask extends AuthorizedTask {

    private static final String LOG_TAG = "GetUserTask";
    private static final String URL_PATH = "/user/get";

    public static final String USER_KEY = "user";

    private GetUserRequest request;

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
        Cache cache = Cache.getInstance();
        this.request = new GetUserRequest(cache.getCurrUserAuthToken(), cache.getCurrUser().getAlias(), alias);
    }

    @Override
    protected boolean runTask() throws IOException, TweeterRemoteException {
        try {
            GetUserResponse response = getServerFacade().sendRequest(request, URL_PATH, GetUserResponse.class);

            if (response.isSuccess()) {
                this.selectedUser = response.getUser();

                BackgroundTaskUtils.loadImage(selectedUser);
                return true;
            } else {
                this.errorMessage = response.getMessage();
                return false;
            }
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    protected void loadBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, selectedUser);
    }
}
