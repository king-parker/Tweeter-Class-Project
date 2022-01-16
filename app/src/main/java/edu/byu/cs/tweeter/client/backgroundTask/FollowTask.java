package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;

/**
 * Background task that establishes a following relationship between two users.
 */
public class FollowTask extends AuthorizedTask {

    private static final String LOG_TAG = "FollowTask";
    static final String URL_PATH = "/follow/follow";
    
    private FollowRequest request;

    public FollowTask(User followee, Handler messageHandler) {
        super(messageHandler);

        this.request = new FollowRequest(getCurrUserAuthToken(), getCurrUserAlias(), followee.getAlias());
    }

    @Override
    protected boolean runTask() throws IOException, TweeterRemoteException {
        try {
            FollowResponse response = getServerFacade().sendRequest(request, URL_PATH, FollowResponse.class);

            if (response.isSuccess()) {
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
