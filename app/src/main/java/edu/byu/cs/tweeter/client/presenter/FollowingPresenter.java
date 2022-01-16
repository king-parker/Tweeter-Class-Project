package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.PagedServiceObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PaginatedPresenter<User> {

    public FollowingPresenter(PaginatedPresenter.View<User> view, User targetUser) {
        super(view, targetUser);
    }

    @Override
    public void callPaginatedService(User targetUser, User lastUser, PagedServiceObserver<User> observer) {
        getFollowService().getFollowing(targetUser, PAGE_SIZE, lastUser, this);
    }
}
