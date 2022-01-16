package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;

/**
 * Background task that posts a new status sent by a user.
 */
public class PostStatusTask extends AuthorizedTask {

    private static final String LOG_TAG = "PostStatusTask";
    static final String URL_PATH = "/status/post";
    
    private PostStatusRequest request;

    public PostStatusTask(Status status, Handler messageHandler) {
        super(messageHandler);

        this.request = new PostStatusRequest(getCurrUserAuthToken(), getCurrUserAlias(), status);
    }

    @Override
    protected boolean runTask() throws IOException, TweeterRemoteException {
        try {
            PostStatusResponse response = getServerFacade().sendRequest(request, URL_PATH, PostStatusResponse.class);

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
