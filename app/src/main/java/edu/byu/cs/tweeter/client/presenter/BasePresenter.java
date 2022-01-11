package edu.byu.cs.tweeter.client.presenter;

public abstract class BasePresenter <T extends PresenterView> {

    protected T view;

    protected BasePresenter(T view) {
        this.view = view;
    }
}
