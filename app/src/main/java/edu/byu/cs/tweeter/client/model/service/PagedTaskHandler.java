package edu.byu.cs.tweeter.client.model.service;

import android.os.Message;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.PagedTask;

public class PagedTaskHandler <S, T extends PagedServiceObserver<S>> extends BackgroundTaskHandler<T> {

    public PagedTaskHandler(T observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(Message msg) {
        List<S> items = (List<S>) msg.getData().getSerializable(PagedTask.ITEMS_KEY);
        boolean hasMorePages = msg.getData().getBoolean(PagedTask.MORE_PAGES_KEY);

        observer.handleSuccess(items, hasMorePages);
    }
}
