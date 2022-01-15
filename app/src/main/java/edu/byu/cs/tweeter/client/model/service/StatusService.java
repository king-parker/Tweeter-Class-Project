package edu.byu.cs.tweeter.client.model.service;

import android.os.Message;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService {

    //~~~~~~~~~~~~~~~~~~~~~~~~~ Post Status Service ~~~~~~~~~~~~~~~~~~~~~~~~~
    public interface PostStatusObserver extends ServiceObserver {
        void handlePostSuccess();
    }

    public void postStatus(Status newStatus, PostStatusObserver observer) {
        PostStatusTask statusTask = new PostStatusTask(newStatus, new PostStatusHandler(observer));

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
    public void getFeed(User targetUser, int limit, Status lastStatus,
                        PagedServiceObserver<Status> observer) {
        GetFeedTask getFeedTask = new GetFeedTask(targetUser, limit, lastStatus,
                new PagedTaskHandler<>(observer));

        BackgroundTaskUtils.executeTask(getFeedTask);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~ Get Story Service ~~~~~~~~~~~~~~~~~~~~~~~~~
    public void getStory(User targetUser, int limit, Status lastStatus,
                         PagedServiceObserver<Status> observer) {
        GetStoryTask getStoryTask = new GetStoryTask(targetUser, limit,
                lastStatus, new PagedTaskHandler<>(observer));

        BackgroundTaskUtils.executeTask(getStoryTask);
    }
}
