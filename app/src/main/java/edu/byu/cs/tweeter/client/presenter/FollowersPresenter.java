package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.PagedServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PaginatedPresenter<User> {

    public FollowersPresenter(PaginatedPresenter.View<User> view, AuthToken authToken, User targetUser) {
        super(view, authToken, targetUser);
    }

    @Override
    public void callPaginatedService(AuthToken authToken, User targetUser, int limit,
                                     User lastUser, PagedServiceObserver<User> observer) {
        getFollowService().getFollowers(authToken, targetUser, PAGE_SIZE, lastUser, this);
    }
}
