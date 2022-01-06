package edu.byu.cs.tweeter.client.presenter;

import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter implements UserService.Observer, FollowService.Observer {

    public interface View {
        void logoutUser();
        void setFollowingCount(int count);
        void setFollowersCount(int count);
        void setIsFollower(boolean isFollower);

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

    public void getFollowingCount() {
        new FollowService().getFollowingCount(authToken, selectedUser, this);
    }

    public void getFollowersCount() {
        new FollowService().getFollowersCount(authToken, selectedUser, this);
    }

    public void isFollowing() {
        new FollowService().isFollower(authToken, Cache.getInstance().getCurrUser(), selectedUser, this);
    }

    @Override
    public void handleSuccess(User user, AuthToken authToken) {
        view.clearInfoMessage();
        view.logoutUser();
    }

    @Override
    public void handleSuccess(@NonNull Message msg) {
        if (msg.getData().containsKey(GetFollowingCountTask.COUNT_KEY)) {
            int count = msg.getData().getInt(GetFollowingCountTask.COUNT_KEY);
            view.setFollowingCount(count);
        }
        else if (msg.getData().containsKey(GetFollowersCountTask.COUNT_KEY)) {
            int count = msg.getData().getInt(GetFollowersCountTask.COUNT_KEY);
            view.setFollowersCount(count);
        }
        else if (msg.getData().containsKey(IsFollowerTask.IS_FOLLOWER_KEY)) {
            boolean isFollower = msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
            view.setIsFollower(isFollower);
        }
        else {
            handleException(new Exception("Internal Error: Improper call for observer to handle success"));
        }
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
