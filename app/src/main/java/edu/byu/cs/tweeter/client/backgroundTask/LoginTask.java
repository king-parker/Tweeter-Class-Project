package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;

/**
 * Background task that logs in a user (i.e., starts a session).
 */
public class LoginTask extends AuthenticationTask {

    private static final String LOG_TAG = "LoginTask";
    private static final String URL_PATH = "/user/login";

    private LoginRequest request;

    public LoginTask(String username, String password, Handler messageHandler) {
        super(username, password, messageHandler);

        this.request = new LoginRequest(username, password);
    }

    @Override
    protected boolean runTask() throws IOException, TweeterRemoteException {
        try {
            LoginResponse response = getServerFacade().sendRequest(request, URL_PATH, LoginResponse.class);

            if (response.isSuccess()) {
                this.user = response.getUser();
                this.authToken = response.getAuthToken();

                BackgroundTaskUtils.loadImage(user);
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
