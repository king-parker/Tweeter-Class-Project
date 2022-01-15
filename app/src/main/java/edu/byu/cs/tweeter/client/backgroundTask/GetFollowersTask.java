package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedUserTask {

    private static final String LOG_TAG = "GetFollowersTask";
    static final String URL_PATH = "/follow/followers";
    
    private FollowerRequest request;

    public GetFollowersTask(User targetUser, int limit, User lastFollower,
                            Handler messageHandler) {
        super(targetUser, limit, lastFollower, messageHandler);

        String targetUserAlias = (targetUser == null) ? null : targetUser.getAlias();
        String lastFollowerAlias = (lastFollower == null) ? null : lastFollower.getAlias();

        this.request = new FollowerRequest(getCurrUserAuthToken(), getCurrUserAlias(), targetUserAlias, limit, lastFollowerAlias);
    }

    @Override
    protected boolean runTask() throws IOException, TweeterRemoteException {
        try {
            FollowerResponse response = getServerFacade().sendRequest(request, URL_PATH, FollowerResponse.class);


            if (response.isSuccess()) {
                loadImages(response.getFollowers());
                this.items = response.getFollowers();
                this.hasMorePages = response.getHasMorePages();

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
