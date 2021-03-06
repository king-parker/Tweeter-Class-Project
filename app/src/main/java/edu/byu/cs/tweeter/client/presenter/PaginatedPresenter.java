package edu.byu.cs.tweeter.client.presenter;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.PagedServiceObserver;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PaginatedPresenter<T> extends BasePresenter<PaginatedPresenter.View<T>>
        implements PagedServiceObserver<T>, UserService.GetUserObserver {

    public interface View<T> extends PresenterView {
        void addItems(List<T> items);
        void setLoading(boolean value);
        void navigateToUser(User user);
    }

    protected static final int PAGE_SIZE = 10;

    private User targetUser;
    private T lastItem;
    private boolean hasMorePages = true;
    private boolean isLoading = false;

    protected PaginatedPresenter(View<T> view, User targetUser) {
        super(view);

        this.targetUser = targetUser;
    }

    public void getUser(String alias) {
        new UserService().getUser(alias, this);
    }

    public void loadMoreItems() {
        if (!isLoading && hasMorePages) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoading(true);

//            new StatusService().getFeed(authToken, targetUser, PAGE_SIZE, lastItem, this);
            callPaginatedService(targetUser, lastItem, this);
        }
    }

    public abstract void callPaginatedService(User targetUser, T lastItem,
                                              PagedServiceObserver<T> observer);

    @Override
    public void handleSuccess(List<T> items, boolean hasMorePages) {
        if (items == null) {
            items = new ArrayList<>();
        }

        this.hasMorePages = hasMorePages;
        lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;
        isLoading = false;
        view.setLoading(false);
        view.addItems(items);
    }

    @Override
    public void handleSuccess(User selectedUser) {
        view.displayInfoMessage("Getting user's profile...");
        view.navigateToUser(selectedUser);
    }

    @Override
    public void handleFailure(String message) {
        isLoading = false;
        view.displayErrorMessage("Failed to get profile: " + message);
    }

    @Override
    public void handleException(Exception ex) {
        isLoading = false;
        view.displayErrorMessage("Failed to get profile: " + ex.getMessage());
    }
}
