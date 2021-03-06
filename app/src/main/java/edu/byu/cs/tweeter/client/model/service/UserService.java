package edu.byu.cs.tweeter.client.model.service;

import android.os.Message;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService {

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
    public interface LogoutObserver extends ServiceObserver {
        void handleSuccess();
    }

    public void logout(LogoutObserver observer) {
        LogoutTask logoutTask = new LogoutTask(new LogoutHandler(observer));

        BackgroundTaskUtils.executeTask(logoutTask);
    }

    /**
     * Message handler (i.e., observer) for LogoutTask.
     */
    private class LogoutHandler extends BackgroundTaskHandler<LogoutObserver> {

        public LogoutHandler(LogoutObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(Message msg) {
            observer.handleSuccess();
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~ Get User Service ~~~~~~~~~~~~~~~~~~~~~~~~~
    public interface GetUserObserver extends ServiceObserver {
        void handleSuccess(User selectedUser);
    }

    public void getUser(String alias, GetUserObserver observer) {
        GetUserTask getUserTask = new GetUserTask(alias, new GetUserHandler(observer));

        BackgroundTaskUtils.executeTask(getUserTask);
    }

    /**
     * Message handler (i.e., observer) for GetUserTask.
     */
    private class GetUserHandler extends BackgroundTaskHandler<GetUserObserver> {

        public GetUserHandler(GetUserObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(Message msg) {
            User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);

            observer.handleSuccess(user);
        }
    }
}