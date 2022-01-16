package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.PagedServiceObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PaginatedPresenter<Status> {

    public StoryPresenter(PaginatedPresenter.View<Status> view, User targetUser) {
        super(view, targetUser);
    }

    @Override
    public void callPaginatedService(User targetUser, Status lastStatus, PagedServiceObserver<Status> observer) {
        getStatusService().getStory(targetUser, PAGE_SIZE, lastStatus, this);
    }
}
