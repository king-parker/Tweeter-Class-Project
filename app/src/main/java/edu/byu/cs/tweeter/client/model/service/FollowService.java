package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.backgroundTask.AuthorizedTask;
import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {
    public static final String UPDATE_FOLLOW_KEY = "follow-unfollow";

    public interface Observer {
        void handleSuccess(@NonNull Message msg);
        // TODO: Add parameters to failure and exception - message prefix, task tag
        void handleFailure(String message);
        void handleException(Exception ex);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~ Get Following Service ~~~~~~~~~~~~~~~~~~~~~~~~~
    public void getFollowing(AuthToken authToken, User targetUser,
                             int limit, User lastFollowee, Observer observer) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(authToken, targetUser, limit,
                lastFollowee, new GetFollowingHandler(observer));

        BackgroundTaskUtils.executeTask(getFollowingTask);
    }

    /**
     * Message handler (i.e., observer) for GetFollowingTask.
     */
    private class GetFollowingHandler extends Handler {
        private Observer observer;

        public GetFollowingHandler(Observer observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {

            boolean success = msg.getData().getBoolean(GetFollowingTask.SUCCESS_KEY);
            if (success) {
                observer.handleSuccess(msg);
            } else if (msg.getData().containsKey(GetFollowingTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowingTask.MESSAGE_KEY);

                observer.handleFailure(message);
            } else if (msg.getData().containsKey(GetFollowingTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowingTask.EXCEPTION_KEY);

                observer.handleException(ex);
            }
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~ Get Followers Service ~~~~~~~~~~~~~~~~~~~~~~~~~
    public void getFollowers(AuthToken authToken, User targetUser,
                             int limit, User lastFollower, Observer observer) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(authToken, targetUser, limit,
                lastFollower, new GetFollowersHandler(observer));

        BackgroundTaskUtils.executeTask(getFollowersTask);
    }

    /**
     * Message handler (i.e., observer) for GetFollowersTask.
     */
    private class GetFollowersHandler extends Handler {
        private Observer observer;

        public GetFollowersHandler(Observer observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {

            boolean success = msg.getData().getBoolean(GetFollowersTask.SUCCESS_KEY);
            if (success) {
                observer.handleSuccess(msg);
            } else if (msg.getData().containsKey(GetFollowersTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowersTask.MESSAGE_KEY);

                observer.handleFailure(message);
            } else if (msg.getData().containsKey(GetFollowersTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersTask.EXCEPTION_KEY);

                observer.handleException(ex);
            }
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~ Get Following Count Service ~~~~~~~~~~~~~~~~~~~~~~~~~
    public interface FollowingCountObserver extends ServiceObserver {
        void handleSuccessFollowing(int count);
    }

    public void getFollowingCount(AuthToken authToken, User targetUser, FollowingCountObserver observer) {
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(authToken,
                targetUser, new GetFollowingCountHandler(observer));

        BackgroundTaskUtils.executeTask(followingCountTask);
    }

    /**
     * Message handler (i.e., observer) for GetFollowingCountTask.
     */
    private class GetFollowingCountHandler extends BackgroundTaskHandler<FollowingCountObserver> {

        public GetFollowingCountHandler(FollowingCountObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(Message msg) {
            int count = msg.getData().getInt(GetCountTask.COUNT_KEY);

            observer.handleSuccessFollowing(count);
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~ Get Followers Count Service ~~~~~~~~~~~~~~~~~~~~~~~~~
    public interface FollowersCountObserver extends ServiceObserver {
        void handleSuccessFollowers(int count);
    }

    public void getFollowersCount(AuthToken authToken, User targetUser, FollowersCountObserver observer) {
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(authToken,
                targetUser, new GetFollowersCountHandler(observer));

        BackgroundTaskUtils.executeTask(followersCountTask);
    }

    /**
     * Message handler (i.e., observer) for GetFollowersCountTask.
     */
    private class GetFollowersCountHandler extends BackgroundTaskHandler<FollowersCountObserver> {

        public GetFollowersCountHandler(FollowersCountObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(Message msg) {
            int count = msg.getData().getInt(GetCountTask.COUNT_KEY);

            observer.handleSuccessFollowers(count);
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~ Is Follower Service ~~~~~~~~~~~~~~~~~~~~~~~~~
    public interface IsFollowerObserver extends ServiceObserver {
        void handleSuccessIsFollower(boolean isFollower);
    }

    public void isFollower(AuthToken authToken, User currUser, User selectedUser, IsFollowerObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(authToken, currUser, selectedUser,
                new IsFollowerHandler(observer));

        BackgroundTaskUtils.executeTask(isFollowerTask);
    }

    /**
     * Message handler (i.e., observer) for IsFollowerTask.
     */
    private class IsFollowerHandler extends BackgroundTaskHandler<IsFollowerObserver> {

        public IsFollowerHandler(IsFollowerObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(Message msg) {
            boolean isFollower = msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);

            observer.handleSuccessIsFollower(isFollower);
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~ Follow Unfollow Service ~~~~~~~~~~~~~~~~~~~~~~~~~
    public interface FollowUnfollowObserver extends ServiceObserver {
        void handleSuccessFollowUnfollow(boolean wasFollowing);
    }
    public void followUnfollow(AuthToken authToken, User selectedUser, boolean wasFollowing, FollowUnfollowObserver observer) {
        AuthorizedTask followUnfollowTask;
        FollowUnfollowHandler handler = new FollowUnfollowHandler(observer, wasFollowing);

        if (wasFollowing) {
            followUnfollowTask = new UnfollowTask(authToken, selectedUser, handler);
        } else {
            followUnfollowTask = new FollowTask(authToken, selectedUser, handler);
        }

        BackgroundTaskUtils.executeTask(followUnfollowTask);
    }

    /**
     * Message handler (i.e., observer) for FollowUnfollowTask.
     */
    private class FollowUnfollowHandler extends BackgroundTaskHandler<FollowUnfollowObserver> {

        private boolean wasFollowing;

        public FollowUnfollowHandler(FollowUnfollowObserver observer, boolean wasFollowing) {
            super(observer);

            this.wasFollowing = wasFollowing;
        }

        @Override
        protected void handleSuccessMessage(Message msg) {
            observer.handleSuccessFollowUnfollow(wasFollowing);
        }
    }
}
