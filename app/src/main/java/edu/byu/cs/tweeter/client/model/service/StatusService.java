package edu.byu.cs.tweeter.client.model.service;

import android.os.Message;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService {

    public interface Observer {
        void handleSuccess(List<Status> statuses, boolean hasMorePages);
        void handleFailure(String message);
        void handleException(Exception ex);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~ Post Status Service ~~~~~~~~~~~~~~~~~~~~~~~~~
    public interface PostStatusObserver extends ServiceObserver {
        void handlePostSuccess();
    }

    public void postStatus(AuthToken authToken, Status newStatus, PostStatusObserver observer) {
        PostStatusTask statusTask = new PostStatusTask(authToken, newStatus, new PostStatusHandler(observer));

        BackgroundTaskUtils.executeTask(statusTask);
    }

    /**
     * Message handler (i.e., observer) for PostStatusTask.
     */
    private class PostStatusHandler extends BackgroundTaskHandler<PostStatusObserver> {

        public PostStatusHandler(PostStatusObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(Message msg) {
            observer.handlePostSuccess();
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~ Get Feed Service ~~~~~~~~~~~~~~~~~~~~~~~~~
    public void getFeed(AuthToken authToken, User targetUser,
                        int limit, Status lastStatus, PagedServiceObserver<Status> observer) {
        GetFeedTask getFeedTask = new GetFeedTask(authToken, targetUser, limit,
                lastStatus, new PagedTaskHandler<>(observer));

        BackgroundTaskUtils.executeTask(getFeedTask);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~ Get Story Service ~~~~~~~~~~~~~~~~~~~~~~~~~
    public void getStory(AuthToken authToken, User targetUser,
                         int limit, Status lastStatus, PagedServiceObserver<Status> observer) {
        GetStoryTask getStoryTask = new GetStoryTask(authToken, targetUser, limit,
                lastStatus, new PagedTaskHandler<>(observer));

        BackgroundTaskUtils.executeTask(getStoryTask);
    }
}
