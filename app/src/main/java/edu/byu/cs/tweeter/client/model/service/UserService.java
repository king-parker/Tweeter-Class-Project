package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService {

    public interface Observer {
        void handleSuccess(User user, AuthToken authToken);
        void handleFailure(String message);
        void handleException(Exception ex);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~ Register Service ~~~~~~~~~~~~~~~~~~~~~~~~~
    public interface RegisterObserver extends ServiceObserver {
        void handleSuccess(User registeredUser, AuthToken authToken);
    }

    public void register (String firstName, String lastName, String alias, String password,
                          String imageBytesBase64, RegisterObserver observer) {
        RegisterTask registerTask = new RegisterTask(firstName, lastName, alias, password,
                imageBytesBase64, new RegisterHandler(observer));

        BackgroundTaskUtils.executeTask(registerTask);
    }

    /**
     * Message handler (i.e., observer) for RegisterTask
     */
    private class RegisterHandler extends BackgroundTaskHandler<RegisterObserver> {

        public RegisterHandler(RegisterObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(Message msg) {
            User registeredUser = (User) msg.getData().getSerializable(RegisterTask.USER_KEY);
            AuthToken authToken = (AuthToken) msg.getData().getSerializable(RegisterTask.AUTH_TOKEN_KEY);

            // Cache user session information
            Cache.getInstance().setCurrUser(registeredUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);

            observer.handleSuccess(registeredUser, authToken);
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~ Login Service ~~~~~~~~~~~~~~~~~~~~~~~~~
    public interface LoginObserver extends ServiceObserver {
        void handleSuccess(User loggedInUser, AuthToken authToken);
    }

    public void login(String alias, String password, LoginObserver observer) {
        // Run a LoginTask to login the user
        LoginTask loginTask = new LoginTask(alias, password, new LoginHandler(observer));

        BackgroundTaskUtils.executeTask(loginTask);
    }

    /**
     * Message handler (i.e., observer) for LoginTask
     */
    private class LoginHandler extends BackgroundTaskHandler<LoginObserver> {

//        private Observer observer;

        public LoginHandler(LoginObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(Message msg) {
            User loggedInUser = (User) msg.getData().getSerializable(LoginTask.USER_KEY);
            AuthToken authToken = (AuthToken) msg.getData().getSerializable(LoginTask.AUTH_TOKEN_KEY);

            // Cache user session information
            Cache.getInstance().setCurrUser(loggedInUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);

            observer.handleSuccess(loggedInUser, authToken);
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~ Logout Service ~~~~~~~~~~~~~~~~~~~~~~~~~
    public void logout(AuthToken authToken, Observer observer) {
        LogoutTask logoutTask = new LogoutTask(authToken, new LogoutHandler(observer));

        BackgroundTaskUtils.executeTask(logoutTask);
    }

    /**
     * Message handler (i.e., observer) for LogoutTask.
     */
    private class LogoutHandler extends Handler {

        private Observer observer;

        public LogoutHandler(Observer observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(LogoutTask.SUCCESS_KEY);
            if (success) {
                observer.handleSuccess(null, null);
            } else if (msg.getData().containsKey(LogoutTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(LogoutTask.MESSAGE_KEY);

                observer.handleFailure(message);
            } else if (msg.getData().containsKey(LogoutTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(LogoutTask.EXCEPTION_KEY);

                observer.handleException(ex);
            }
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~ Get User Service ~~~~~~~~~~~~~~~~~~~~~~~~~
    public void getUser(AuthToken authToken, String alias, Observer observer) {
        GetUserTask getUserTask = new GetUserTask(authToken, alias, new GetUserHandler(observer));

        BackgroundTaskUtils.executeTask(getUserTask);
    }

    /**
     * Message handler (i.e., observer) for GetUserTask.
     */
    private class GetUserHandler extends Handler {

        private Observer observer;

        public GetUserHandler(Observer observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetUserTask.SUCCESS_KEY);
            if (success) {
                User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);

                observer.handleSuccess(user, null);
            } else if (msg.getData().containsKey(GetUserTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetUserTask.MESSAGE_KEY);

                observer.handleFailure(message);
            } else if (msg.getData().containsKey(GetUserTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetUserTask.EXCEPTION_KEY);

                observer.handleException(ex);
            }
        }
    }
}
