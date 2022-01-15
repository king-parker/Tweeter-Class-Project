package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.PagedServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PaginatedPresenter<Status> {

    public FeedPresenter(PaginatedPresenter.View<Status> view, AuthToken authToken, User targetUser) {
        super(view, authToken, targetUser);
    }

    @Override
    public void callPaginatedService(AuthToken authToken, User targetUser, int limit,
                                     Status lastStatus, PagedServiceObserver<Status> observer) {
        getStatusService().getFeed(authToken, targetUser, PAGE_SIZE, lastStatus, this);
    }
}
