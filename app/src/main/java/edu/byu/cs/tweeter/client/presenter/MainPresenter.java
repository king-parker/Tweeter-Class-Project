package edu.byu.cs.tweeter.client.presenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends BasePresenter<MainPresenter.View> implements UserService.LogoutObserver, FollowService.FollowingCountObserver,
        FollowService.FollowersCountObserver, FollowService.IsFollowerObserver,
        FollowService.FollowUnfollowObserver, StatusService.PostStatusObserver {

    public interface View extends PresenterView {
        void logoutUser();
        void setFollowingCount(int count);
        void setFollowersCount(int count);
        void setIsFollower(boolean isFollower);
        void updateFollowButton(boolean removed);
        void enableFollowButton(boolean enable);
    }

    private User selectedUser;

    public MainPresenter(View view, User selectedUser) {
        super(view);

        this.selectedUser = selectedUser;
    }

    public void logout() {
        view.clearErrorMessage();
        view.clearInfoMessage();

        view.displayInfoMessage("Logging Out...");

        getUserService().logout(this);
    }

    public void getFollowingCount() {
        getFollowService().getFollowingCount(selectedUser, this);
    }

    public void getFollowersCount() {
        getFollowService().getFollowersCount(selectedUser, this);
    }

    public void isFollowing() {
        getFollowService().isFollower(Cache.getInstance().getCurrUser(), selectedUser, this);
    }

    public void followUnfollow(boolean wasFollowing) {
        view.enableFollowButton(false);

        if (wasFollowing) {
            view.displayInfoMessage("Removing " + selectedUser.getName() + "...");
        }
        else {
            view.displayInfoMessage("Adding " + selectedUser.getName() + "...");
        }

        getFollowService().followUnfollow(selectedUser, wasFollowing, this);

        view.enableFollowButton(true);
    }

    public void postStatus(String post) throws ParseException {
        view.displayInfoMessage("Posting Status...");

        Status newStatus = new Status(post, Cache.getInstance().getCurrUser(),
                getFormattedDateTime(), parseURLs(post), parseMentions(post));
        getStatusService().postStatus(newStatus, this);
    }

    public void updateSelectedUserFollowingAndFollowers() {
        // TODO: Utilize this?
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Get count of most recently selected user's followers.
        getFollowersCount();

        // Get count of most recently selected user's followees (who they are following)
        getFollowingCount();
    }

    public String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }

    public List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    public List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }

    public int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    @Override
    public void handleSuccess() {
        //Clear user data (cached data).
        Cache.getInstance().clearCache();
        view.clearInfoMessage();
        view.logoutUser();
        view.displayInfoMessage("Logged out");
    }

    @Override
    public void handlePostSuccess() {
        view.clearInfoMessage();
        view.displayInfoMessage("Successfully Posted!");
    }

    @Override
    public void handleSuccessFollowing(int count) {
        view.setFollowingCount(count);
    }

    @Override
    public void handleSuccessFollowers(int count) {
        view.setFollowersCount(count);
    }

    @Override
    public void handleSuccessIsFollower(boolean isFollower) {
        view.setIsFollower(isFollower);
    }

    @Override
    public void handleSuccessFollowUnfollow(boolean wasFollowing) {
        view.updateFollowButton(wasFollowing);
    }

    @Override
    public void handleFailure(String message) {
        view.clearInfoMessage();
        view.displayErrorMessage(message);
    }

    @Override
    public void handleException(Exception ex) {
        view.clearInfoMessage();
        view.displayErrorMessage(ex.getMessage());
    }
}
