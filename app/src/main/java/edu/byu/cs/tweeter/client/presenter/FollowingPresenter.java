package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter implements FollowService.Observer, UserService.Observer {

    public interface View {
        void addItems(List<User> followees);
        void setLoading(boolean value);
        void navigateToUser(User user);

        void displayErrorMessage(String message);
        void clearErrorMessage();

        void displayInfoMessage(String message);
        void clearInfoMessage();
    }

    private static final int PAGE_SIZE = 10;

    private View view;
    private AuthToken authToken;
    private User targetUser;
    private User lastFollowee;
    private boolean hasMorePages = true;
    private boolean isLoading = false;

    public FollowingPresenter(View view, AuthToken authToken, User targetUser) {
        this.view = view;
        this.authToken = authToken;
        this.targetUser = targetUser;
    }

    public void getUser(String alias) {
        new UserService().getUser(authToken, alias, this);
    }

    public void loadMoreItems() {
        if (!isLoading && hasMorePages) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoading(true);

            new FollowService().getFollowing(authToken, targetUser, PAGE_SIZE, lastFollowee, this);
        }
    }

    @Override
    public void handleSuccess(List<User> users, boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
        lastFollowee = (users.size() > 0) ? users.get(users.size() - 1) : null;
        isLoading = false;
        view.setLoading(false);
        view.addItems(users);
    }

    @Override
    public void handleSuccess(User user, AuthToken authToken) {
        view.navigateToUser(user);
        view.displayInfoMessage("Getting user's profile...");
    }

    @Override
    public void handleFailure(String message) {
        isLoading = false;
        view.displayErrorMessage("Login failed: " + message);
    }

    @Override
    public void handleException(Exception ex) {
        isLoading = false;
        view.displayErrorMessage("Login failed: " + ex.getMessage());
    }
}
