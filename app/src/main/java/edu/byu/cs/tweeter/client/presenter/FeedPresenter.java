package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.PagedServiceObserver;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter implements PagedServiceObserver<Status>, UserService.GetUserObserver {

    public interface View extends PresenterView {
        void addItems(List<Status> statuses);
        void setLoading(boolean value);
        void navigateToUser(User user);
    }

    private static final int PAGE_SIZE = 10;

    private FeedPresenter.View view;
    private AuthToken authToken;
    private User targetUser;
    private Status lastStatus;
    private boolean hasMorePages = true;
    private boolean isLoading = false;

    public FeedPresenter(FeedPresenter.View view, AuthToken authToken, User targetUser) {
        this.view = view;
        this.authToken = authToken;
        this.targetUser = targetUser;
    }

    public void getUser(String alias) {
        new UserService().getUser(authToken, alias, this);
    }

    public void loadMoreItems() {
        if (!isLoading && hasMorePages) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoading(true);

            new StatusService().getFeed(authToken, targetUser, PAGE_SIZE, lastStatus, this);
        }
    }

    @Override
    public void handleSuccess(List<Status> statuses, boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
        lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
        isLoading = false;
        view.setLoading(false);
        view.addItems(statuses);
    }

    @Override
    public void handleSuccess(User selectedUser) {
        view.navigateToUser(selectedUser);
        view.displayInfoMessage("Getting user's profile...");
    }

    @Override
    public void handleFailure(String message) {
        isLoading = false;
        view.displayErrorMessage("Login failed: " + message);
    }

    @Override
    public void handleException(Exception ex) {
        isLoading = false;
        view.displayErrorMessage("Login failed: " + ex.getMessage());
    }
}
