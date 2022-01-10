package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.backgroundTask.PagedTask;
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
                        int limit, Status lastStatus, StatusService.Observer observer) {
        GetFeedTask getFeedTask = new GetFeedTask(authToken, targetUser, limit,
                lastStatus, new GetFeedHandler(observer));

        BackgroundTaskUtils.executeTask(getFeedTask);
    }

    /**
     * Message handler (i.e., observer) for GetFeedTask.
     */
    private class GetFeedHandler extends Handler {

        private Observer observer;

        public GetFeedHandler(Observer observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {

            boolean success = msg.getData().getBoolean(PagedTask.SUCCESS_KEY);
            if (success) {
                List<Status> statuses = (List<Status>) msg.getData().getSerializable(PagedTask.ITEMS_KEY);
                boolean hasMorePages = msg.getData().getBoolean(PagedTask.MORE_PAGES_KEY);

                observer.handleSuccess(statuses, hasMorePages);
            } else if (msg.getData().containsKey(PagedTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(PagedTask.MESSAGE_KEY);

                observer.handleFailure(message);
            } else if (msg.getData().containsKey(PagedTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(PagedTask.EXCEPTION_KEY);

                observer.handleException(ex);
            }
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~ Get Story Service ~~~~~~~~~~~~~~~~~~~~~~~~~
    public void getStory(AuthToken authToken, User targetUser,
                         int limit, Status lastStatus, StatusService.Observer observer) {
        GetStoryTask getStoryTask = new GetStoryTask(authToken, targetUser, limit,
                lastStatus, new GetStoryHandler(observer));

        BackgroundTaskUtils.executeTask(getStoryTask);
    }

    /**
     * Message handler (i.e., observer) for GetStoryTask.
     */
    private class GetStoryHandler extends Handler {

        private Observer observer;

        public GetStoryHandler(Observer observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {

            boolean success = msg.getData().getBoolean(PagedTask.SUCCESS_KEY);
            if (success) {
                List<Status> statuses = (List<Status>) msg.getData().getSerializable(PagedTask.ITEMS_KEY);
                boolean hasMorePages = msg.getData().getBoolean(PagedTask.MORE_PAGES_KEY);

                observer.handleSuccess(statuses, hasMorePages);
            } else if (msg.getData().containsKey(PagedTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(PagedTask.MESSAGE_KEY);

                observer.handleFailure(message);
            } else if (msg.getData().containsKey(PagedTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(PagedTask.EXCEPTION_KEY);

                observer.handleException(ex);
            }
        }
    }
}
