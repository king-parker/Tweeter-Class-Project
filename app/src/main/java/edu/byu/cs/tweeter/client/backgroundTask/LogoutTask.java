package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;

/**
 * Background task that logs out a user (i.e., ends a session).
 */
public class LogoutTask extends AuthorizedTask {

    private static final String LOG_TAG = "LogoutTask";
    private static final String URL_PATH = "/user/logout";

    private LogoutRequest request;

    public LogoutTask(Handler messageHandler) {
        super(messageHandler);

        this.request = new LogoutRequest(getCurrUserAuthToken(), getCurrUserAlias());
    }

    @Override
    protected boolean runTask() throws IOException, TweeterRemoteException {
        try {
            LogoutResponse response = getServerFacade().sendRequest(request, URL_PATH, LogoutResponse.class);

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
