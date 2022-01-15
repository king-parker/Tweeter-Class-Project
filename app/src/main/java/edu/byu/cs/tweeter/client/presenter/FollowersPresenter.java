package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.PagedServiceObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PaginatedPresenter<User> {

    public FollowersPresenter(PaginatedPresenter.View<User> view, User targetUser) {
        super(view, targetUser);
    }

    @Override
    public void callPaginatedService(User targetUser, User lastUser, PagedServiceObserver<User> observer) {
        getFollowService().getFollowers(targetUser, PAGE_SIZE, lastUser, this);
    }
}
