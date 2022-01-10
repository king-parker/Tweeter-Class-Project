package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.PagedServiceObserver;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter implements PagedServiceObserver<User>, UserService.GetUserObserver {

    public interface View extends PresenterView {
        void addItems(List<User> followees);
        void setLoading(boolean value);
        void navigateToUser(User user);
    }

    private static final int PAGE_SIZE = 10;

    private View view;
    private AuthToken authToken;
    private User targetUser;
    private User lastFollowee;
    private boolean hasMorePages = true;
    private boolean isLoading = false;

    public FollowingPresenter(View view, AuthToken authToken, User targetUser) {
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

            new FollowService().getFollowing(authToken, targetUser, PAGE_SIZE, lastFollowee, this);
        }
    }

//    @Override
//    public void handleSuccess(@NonNull Message msg) {
//        if (msg.getData().containsKey(PagedTask.ITEMS_KEY)
//                && msg.getData().containsKey(PagedTask.MORE_PAGES_KEY)) {
//            List<User> followees = (List<User>) msg.getData().getSerializable(PagedTask.ITEMS_KEY);
//            boolean hasMorePages = msg.getData().getBoolean(PagedTask.MORE_PAGES_KEY);
//
//            updateItems(followees, hasMorePages);
//        }
//        else {
//            handleException(new Exception("Internal Error: Improper call for observer to handle success"));
//        }
//    }

    @Override
    public void handleSuccess(List<User> users, boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
        lastFollowee = (users.size() > 0) ? users.get(users.size() - 1) : null;
        isLoading = false;
        view.setLoading(false);
        view.addItems(users);
    }

    @Override
    public void handleSuccess(User selectedUser) {
        view.navigateToUser(selectedUser);
        view.displayInfoMessage("Getting user's profile...");
    }

    @Override
    public void handleFailure(String message) {
        isLoading = false;
        view.displayErrorMessage(message);
    }

    @Override
    public void handleException(Exception ex) {
        isLoading = false;
        view.displayErrorMessage(ex.getMessage());
    }
}
