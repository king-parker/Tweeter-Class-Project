package edu.byu.cs.tweeter.client.model.net;

import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.Request;
import edu.byu.cs.tweeter.model.net.response.Response;

/**
 * Acts as a Facade to the Tweeter server. All network requests to the server should go through
 * this class.
 */
public class ServerFacade {
    private final String LOG_TAG = "SERVER_FACADE";

    // TODO: Set this to the invoke URL of your API. Find it by going to your API in AWS, clicking
    //  on stages in the right-side menu, and clicking on the stage you deployed your API to.
    private static final String SERVER_URL = "https://ujqlurv0y2.execute-api.us-west-2.amazonaws.com/dev";

    private final ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);

    /**
     * Sends the request to the server to be processed, returns the server response.
     *
     * @param request contains all information needed to perform the request.
     * @return the server response.
     */
    public <T extends Response, S extends Request> T sendRequest(S request, String urlPath, Class<T> returnType)
            throws IOException, TweeterRemoteException {
        Log.i(LOG_TAG, String.format("Sending service request: %s", request.getClass().getName()));
        Log.i(LOG_TAG, request.toString());

        T response = clientCommunicator.doPost(urlPath, request, null, returnType);

        Log.i(LOG_TAG, String.format("Received service response: %s", returnType.getName()));
        Log.i(LOG_TAG, response.toString());

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }
}