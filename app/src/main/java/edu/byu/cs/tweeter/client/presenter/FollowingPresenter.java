package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.PagedServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PaginatedPresenter<User> {

    public FollowingPresenter(PaginatedPresenter.View<User> view, AuthToken authToken, User targetUser) {
        super(view, authToken, targetUser);
    }

    @Override
    public void callPaginatedService(AuthToken authToken, User targetUser, int limit,
                                     User lastUser, PagedServiceObserver<User> observer) {
        getFollowService().getFollowing(authToken, targetUser, PAGE_SIZE, lastUser, this);
    }
}
