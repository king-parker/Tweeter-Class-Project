package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;

/**
 * Background task that retrieves a page of statuses from a user's story.
 */
public class GetStoryTask extends PagedStatusTask {

    private static final String LOG_TAG = "GetStoryTask";
    static final String URL_PATH = "/status/story";

    private StoryRequest request;

    public GetStoryTask(User targetUser, int limit, Status lastStatus,
                        Handler messageHandler) {
        super(targetUser, limit, lastStatus, messageHandler);

        String targetUserAlias = (targetUser == null) ? null : targetUser.getAlias();

        this.request = new StoryRequest(getCurrUserAuthToken(), getCurrUserAlias(), targetUserAlias, limit, lastStatus);
    }

    @Override
    protected boolean runTask() throws IOException, TweeterRemoteException {
        try {
            StoryResponse response = getServerFacade().sendRequest(request, URL_PATH, StoryResponse.class);


            if (response.isSuccess()) {
                loadImages(response.getStory());
                this.items = response.getStory();
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