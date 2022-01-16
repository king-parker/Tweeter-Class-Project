package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.IsFollowingRequest;
import edu.byu.cs.tweeter.model.net.response.IsFollowingResponse;

/**
 * Background task that determines if one user is following another.
 */
public class IsFollowerTask extends AuthorizedTask {
    private static final String LOG_TAG = "IsFollowerTask";
    static final String URL_PATH = "/follow/isfollower";

    public static final String IS_FOLLOWER_KEY = "is-follower";
    
    private IsFollowingRequest request;
    
    private boolean isFollower;

    public IsFollowerTask(User follower, User followee, Handler messageHandler) {
        super(messageHandler);

        this.request = new IsFollowingRequest(getCurrUserAuthToken(), getCurrUserAlias(), follower.getAlias(), followee.getAlias());
    }

    @Override
    protected boolean runTask() throws IOException, TweeterRemoteException {
        try {
            IsFollowingResponse response = getServerFacade().sendRequest(request, URL_PATH, IsFollowingResponse.class);

            if (response.isSuccess()) {
                this.isFollower = response.getIsFollower();
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
        msgBundle.putBoolean(IS_FOLLOWER_KEY, isFollower);
    }
}
