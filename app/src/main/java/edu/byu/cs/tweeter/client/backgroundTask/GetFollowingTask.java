package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;

/**
 * Background task that retrieves a page of other users being followed by a specified user.
 */
public class GetFollowingTask extends PagedUserTask {

    private static final String LOG_TAG = "GetFollowingTask";
    static final String URL_PATH = "/follow/following";

    private FollowingRequest request;

    public GetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastFollowee,
                            Handler messageHandler) {
        super(authToken, targetUser, limit, lastFollowee, messageHandler);

        String targetUserAlias = (targetUser == null) ? null : targetUser.getAlias();
        String lastFolloweeAlias = (lastFollowee == null) ? null : lastFollowee.getAlias();

        this.request = new FollowingRequest(authToken, targetUserAlias, limit, lastFolloweeAlias);
    }

    @Override
    protected boolean runTask() throws IOException, TweeterRemoteException {
        FollowingResponse response = getServerFacade().getFollowees(request, URL_PATH);

        try {
            if (response.isSuccess()) {
                loadImages(response.getFollowees());
                this.items = response.getFollowees();
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
