package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;

/**
 * Background task that retrieves a page of statuses from a user's feed.
 */
public class GetFeedTask extends PagedStatusTask {

    private static final String LOG_TAG = "GetFeedTask";
    static final String URL_PATH = "/status/feed";

    private FeedRequest request;

    public GetFeedTask(User targetUser, int limit, Status lastStatus,
                       Handler messageHandler) {
        super(targetUser, limit, lastStatus, messageHandler);

        String targetUserAlias = (targetUser == null) ? null : targetUser.getAlias();

        this.request = new FeedRequest(getCurrUserAuthToken(), getCurrUserAlias(), targetUserAlias, limit, lastStatus);
    }

    @Override
    protected boolean runTask() throws IOException, TweeterRemoteException {
        try {
            FeedResponse response = getServerFacade().sendRequest(request, URL_PATH, FeedResponse.class);


            if (response.isSuccess()) {
                loadImages(response.getFeed());
                this.items = response.getFeed();
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
