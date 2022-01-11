package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;

public abstract class BasePresenter <T extends PresenterView> {

    protected T view;

    protected BasePresenter(T view) {
        this.view = view;
    }

    protected UserService getUserService() {
        return new UserService();
    }

    protected StatusService getStatusService() {
        return new StatusService();
    }

    protected FollowService getFollowService() {
        return new FollowService();
    }
}
