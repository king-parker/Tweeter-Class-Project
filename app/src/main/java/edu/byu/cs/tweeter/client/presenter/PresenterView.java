package edu.byu.cs.tweeter.client.presenter;

public interface PresenterView {
    void displayErrorMessage(String message);
    void clearErrorMessage();

    void displayInfoMessage(String message);
    void clearInfoMessage();
}
