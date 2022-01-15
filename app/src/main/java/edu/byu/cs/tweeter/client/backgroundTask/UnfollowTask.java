package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;

/**
 * Background task that removes a following relationship between two users.
 */
public class UnfollowTask extends AuthorizedTask {
    private static final String LOG_TAG = "UnfollowTask";
    static final String URL_PATH = "/follow/unfollow";
    
    private UnfollowRequest request;

    public UnfollowTask(User followee, Handler messageHandler) {
        super(messageHandler);

        this.request = new UnfollowRequest(getCurrUserAuthToken(), getCurrUserAlias(), followee.getAlias());
    }

    @Override
    protected boolean runTask() throws IOException, TweeterRemoteException {
        try {
            UnfollowResponse response = getServerFacade().sendRequest(request, URL_PATH, UnfollowResponse.class);

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
