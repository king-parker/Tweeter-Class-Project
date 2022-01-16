package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedTask<T> extends AuthorizedTask {

    private static final String LOG_TAG = "PagedStatusTask";

    public static final String ITEMS_KEY = "items";
    public static final String MORE_PAGES_KEY = "more-pages";

    /**
     * The user whose items are being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    protected User targetUser;
    /**
     * Maximum number of items to return (i.e., page size).
     */
    protected int limit;
    /**
     * The last item returned in the previous page of results (can be null).
     * This allows the new page to begin where the previous page ended.
     */
    protected T lastItem;
    /**
     * List of items returned by the server
     */
    protected List<T> items;
    /**
     * Value returned by the server indicating whether or not there are remaining items
     * to be retrieved from the server
     */
    protected boolean hasMorePages;


    protected PagedTask(User targetUser, int limit, T lastItem, Handler messageHandler) {
        super(messageHandler);

        this.targetUser = targetUser;
        this.limit = limit;
        this.lastItem = lastItem;
    }

    @Override
    protected void loadBundle(Bundle msgBundle) {
        msgBundle.putSerializable(ITEMS_KEY, (Serializable) this.items);
        msgBundle.putBoolean(MORE_PAGES_KEY, this.hasMorePages);
    }
}
