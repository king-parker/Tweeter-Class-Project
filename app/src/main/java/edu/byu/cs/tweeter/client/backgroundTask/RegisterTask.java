package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;

/**
 * Background task that creates a new user account and logs in the new user (i.e., starts a session).
 */
public class RegisterTask extends AuthenticationTask {

    private static final String LOG_TAG = "RegisterTask";
    private static final String URL_PATH = "/user/register";

    private RegisterRequest request;

    /**
     * The user's first name.
     */
    private String firstName;
    /**
     * The user's last name.
     */
    private String lastName;
    /**
     * The base-64 encoded bytes of the user's profile image.
     */
    private String image;

    public RegisterTask(String firstName, String lastName, String username, String password,
                        String image, Handler messageHandler) {
        super(username, password, messageHandler);

        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;

        this.request = new RegisterRequest(firstName, lastName, username, password, image);
    }

    @Override
    protected boolean runTask() throws IOException, TweeterRemoteException {
        try {
            RegisterResponse response = getServerFacade().sendRequest(request, URL_PATH, RegisterResponse.class);

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
