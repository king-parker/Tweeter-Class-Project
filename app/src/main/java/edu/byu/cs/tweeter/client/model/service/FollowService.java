package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowingTask);
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
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowersTask);
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
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(followingCountTask);
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
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(followersCountTask);
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
    public void isFollower(AuthToken authToken, User currUser, User selectedUser, Observer observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(authToken, currUser, selectedUser,
                new IsFollowerHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(isFollowerTask);
    }

    /**
     * Message handler (i.e., observer) for IsFollowerTask.
     */
    private class IsFollowerHandler extends Handler {

        private Observer observer;

        public IsFollowerHandler(Observer observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(IsFollowerTask.SUCCESS_KEY);
            if (success) {
                observer.handleSuccess(msg);
            } else if (msg.getData().containsKey(IsFollowerTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(IsFollowerTask.MESSAGE_KEY);

                observer.handleFailure(message);
            } else if (msg.getData().containsKey(IsFollowerTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(IsFollowerTask.EXCEPTION_KEY);

                observer.handleException(ex);
            }
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~ Follow Unfollow Service ~~~~~~~~~~~~~~~~~~~~~~~~~
    public void followUnfollow(AuthToken authToken, User selectedUser, boolean wasFollowing, Observer observer) {
        if (wasFollowing) {
            UnfollowTask unfollowTask = new UnfollowTask(authToken, selectedUser, new UnfollowHandler(observer));
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(unfollowTask);
        } else {
            FollowTask followTask = new FollowTask(authToken, selectedUser, new FollowHandler(observer));
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(followTask);
        }
    }

    /**
     * Message handler (i.e., observer) for FollowTask.
     */
    private class FollowHandler extends Handler {
        private Observer observer;

        public FollowHandler(Observer observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(FollowTask.SUCCESS_KEY);
            if (success) {
                msg.getData().putBoolean(UPDATE_FOLLOW_KEY, false);
                observer.handleSuccess(msg);
            } else if (msg.getData().containsKey(FollowTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(FollowTask.MESSAGE_KEY);

                observer.handleFailure(message);
            } else if (msg.getData().containsKey(FollowTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(FollowTask.EXCEPTION_KEY);

                observer.handleException(ex);
            }

            // TODO: Add parameters to failure and exception so follow button can be enabled properly
//            followButton.setEnabled(true);
        }
    }

    /**
     * Message handler (i.e., observer) for UnfollowTask.
     */
    private class UnfollowHandler extends Handler {
        private Observer observer;

        public UnfollowHandler(Observer observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(UnfollowTask.SUCCESS_KEY);
            if (success) {
                msg.getData().putBoolean(UPDATE_FOLLOW_KEY, true);
                observer.handleSuccess(msg);
            } else if (msg.getData().containsKey(UnfollowTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(UnfollowTask.MESSAGE_KEY);

                observer.handleFailure(message);
            } else if (msg.getData().containsKey(UnfollowTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(UnfollowTask.EXCEPTION_KEY);

                observer.handleException(ex);
            }

            // TODO: Add parameters to failure and exception so follow button can be enabled properly
//            followButton.setEnabled(true);
        }
    }
}
