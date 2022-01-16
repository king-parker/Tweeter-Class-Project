package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;

/**
 * Background task that queries how many followers a user has.
 */
public class GetFollowersCountTask extends GetCountTask {

    private static final String LOG_TAG = "GetFollowersCountTask";
    static final String URL_PATH = "/follow/followers/count";
    
    private FollowersCountRequest request;

    public GetFollowersCountTask(User targetUser, Handler messageHandler) {
        super(targetUser, false, messageHandler);

        this.request = new FollowersCountRequest(getCurrUserAuthToken(), getCurrUserAlias(), targetUser.getAlias());
    }

    @Override
    protected boolean runTask() throws IOException, TweeterRemoteException {
        try {
            FollowersCountResponse response = getServerFacade().sendRequest(request, URL_PATH, FollowersCountResponse.class);

            if (response.isSuccess()) {
                this.count = response.getCount();
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
}
