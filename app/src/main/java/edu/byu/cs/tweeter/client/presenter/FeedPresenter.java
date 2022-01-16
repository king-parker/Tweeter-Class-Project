package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.PagedServiceObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PaginatedPresenter<Status> {

    public FeedPresenter(PaginatedPresenter.View<Status> view, User targetUser) {
        super(view, targetUser);
    }

    @Override
    public void callPaginatedService(User targetUser,
                                     Status lastStatus, PagedServiceObserver<Status> observer) {
        getStatusService().getFeed(targetUser, PAGE_SIZE, lastStatus, this);
    }
}
