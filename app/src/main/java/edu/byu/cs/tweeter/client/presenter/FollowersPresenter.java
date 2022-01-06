package edu.byu.cs.tweeter.client.presenter;

import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter implements FollowService.Observer, UserService.Observer {

    public interface View {
        void addItems(List<User> followers);
        void setLoading(boolean value);
        void navigateToUser(User user);

        void displayErrorMessage(String message);
        void clearErrorMessage();

        void displayInfoMessage(String message);
        void clearInfoMessage();
    }

    private static final int PAGE_SIZE = 10;

    private FollowersPresenter.View view;
    private AuthToken authToken;
    private User targetUser;
    private User lastFollower;
    private boolean hasMorePages = true;
    private boolean isLoading = false;

    public FollowersPresenter(FollowersPresenter.View view, AuthToken authToken, User targetUser) {
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

            new FollowService().getFollowers(authToken, targetUser, PAGE_SIZE, lastFollower, this);
        }
    }

    @Override
    public void handleSuccess(@NonNull Message msg) {
        if (msg.getData().containsKey(GetFollowersTask.FOLLOWERS_KEY)
                && msg.getData().containsKey(GetFollowersTask.MORE_PAGES_KEY)) {
            List<User> followers = (List<User>) msg.getData().getSerializable(GetFollowersTask.FOLLOWERS_KEY);
            boolean hasMorePages = msg.getData().getBoolean(GetFollowersTask.MORE_PAGES_KEY);

            updateItems(followers, hasMorePages);
        }
        else {
            handleException(new Exception("Internal Error: Improper call for observer to handle success"));
        }
    }

    private void updateItems(List<User> users, boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
        lastFollower = (users.size() > 0) ? users.get(users.size() - 1) : null;
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
        view.displayErrorMessage(message);
    }

    @Override
    public void handleException(Exception ex) {
        isLoading = false;
        view.displayErrorMessage(ex.getMessage());
    }
}
