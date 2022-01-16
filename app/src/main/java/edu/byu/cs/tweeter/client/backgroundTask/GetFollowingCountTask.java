package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;

/**
 * Background task that queries how many other users a specified user is following.
 */
public class GetFollowingCountTask extends GetCountTask {

    private static final String LOG_TAG = "GetFollowingCountTask";
    static final String URL_PATH = "/follow/following/count";
    
    private FollowingCountRequest request;

    public GetFollowingCountTask(User targetUser, Handler messageHandler) {
        super(targetUser, true, messageHandler);

        this.request = new FollowingCountRequest(getCurrUserAuthToken(), getCurrUserAlias(), targetUser.getAlias());
    }

    @Override
    protected boolean runTask() throws IOException, TweeterRemoteException {
        try {
            FollowingCountResponse response = getServerFacade().sendRequest(request, URL_PATH, FollowingCountResponse.class);

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
