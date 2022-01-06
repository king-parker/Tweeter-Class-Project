package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter implements UserService.Observer {

    public interface View {
        void logoutUser();

        void displayErrorMessage(String message);
        void clearErrorMessage();

        void displayInfoMessage(String message);
        void clearInfoMessage();
    }

    private View view;
    private User selectedUser;
    private AuthToken authToken;

    public MainPresenter(View view, AuthToken authToken, User selectedUser) {
        this.view = view;
        this.authToken = authToken;
        this.selectedUser = selectedUser;
    }

    public void logout() {
        view.clearErrorMessage();
        view.clearInfoMessage();

        view.displayInfoMessage("Logging Out...");

        new UserService().logout(authToken, this);
    }

    @Override
    public void handleSuccess(User user, AuthToken authToken) {
        view.clearInfoMessage();
        view.logoutUser();
    }

    @Override
    public void handleFailure(String message) {
        view.displayErrorMessage(message);
    }

    @Override
    public void handleException(Exception ex) {
        view.displayErrorMessage(ex.getMessage());
    }
}
