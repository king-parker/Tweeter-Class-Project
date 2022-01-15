package edu.byu.cs.tweeter.client.model.service;

import android.os.Message;

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
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {
    public static final String UPDATE_FOLLOW_KEY = "follow-unfollow";

    //~~~~~~~~~~~~~~~~~~~~~~~~~ Get Following Service ~~~~~~~~~~~~~~~~~~~~~~~~~
    public void getFollowing(User targetUser, int limit, User lastFollowee,
                             PagedServiceObserver<User> observer) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(targetUser, limit, lastFollowee,
                new PagedTaskHandler<>(observer));

        BackgroundTaskUtils.executeTask(getFollowingTask);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~ Get Followers Service ~~~~~~~~~~~~~~~~~~~~~~~~~
    public void getFollowers(User targetUser, int limit, User lastFollower,
                             PagedServiceObserver<User> observer) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(targetUser, limit, lastFollower,
                new PagedTaskHandler<>(observer));

        BackgroundTaskUtils.executeTask(getFollowersTask);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~ Get Following Count Service ~~~~~~~~~~~~~~~~~~~~~~~~~
    public interface FollowingCountObserver extends ServiceObserver {
        void handleSuccessFollowing(int count);
    }

    public void getFollowingCount(User targetUser, FollowingCountObserver observer) {
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(targetUser,
                new GetFollowingCountHandler(observer));

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

    public void getFollowersCount(User targetUser, FollowersCountObserver observer) {
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(targetUser,
                new GetFollowersCountHandler(observer));

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

    public void isFollower(User currUser, User selectedUser, IsFollowerObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(currUser, selectedUser,
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
    public void followUnfollow(User selectedUser, boolean wasFollowing, FollowUnfollowObserver observer) {
        AuthorizedTask followUnfollowTask;
        FollowUnfollowHandler handler = new FollowUnfollowHandler(observer, wasFollowing);

        if (wasFollowing) {
            followUnfollowTask = new UnfollowTask(selectedUser, handler);
        } else {
            followUnfollowTask = new FollowTask(selectedUser, handler);
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
