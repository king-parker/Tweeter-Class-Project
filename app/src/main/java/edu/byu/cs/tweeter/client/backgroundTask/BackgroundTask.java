package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import edu.byu.cs.tweeter.util.FakeData;

/**
 * Base class for background task.
 */
public abstract class BackgroundTask implements Runnable {

    private static final String LOG_TAG = "BackgroundTask";

    public static final String SUCCESS_KEY = "success";
    public static final String MESSAGE_KEY = "message";
    public static final String EXCEPTION_KEY = "exception";

    /**
     * Message handler that will receive task results.
     */
    private final Handler messageHandler;

    /**
     * Error message indicating why the task failed.
     */
    protected String errorMessage;

    protected BackgroundTask(Handler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        try {
            if (runTask()) {
                sendSuccessMessage();
            }
            else {
                sendFailedMessage();
            }
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            sendExceptionMessage(ex);
        }
    }

    protected abstract boolean runTask() throws IOException;

    protected FakeData getFakeData() {
        return new FakeData();
    }

    private void sendSuccessMessage() {
        Bundle msgBundle = createMessageBundle(true);
        loadBundle(msgBundle);
        sendMessage(msgBundle);
    }

    protected void loadBundle(Bundle msgBundle) {
        // By default, do nothing
    }

    private void sendFailedMessage() {
        Bundle msgBundle = createMessageBundle(false);
        msgBundle.putString(MESSAGE_KEY, this.errorMessage);
        sendMessage(msgBundle);
    }

    private void sendExceptionMessage(Exception exception) {
        Bundle msgBundle = createMessageBundle(false);
        msgBundle.putSerializable(EXCEPTION_KEY, exception);
        sendMessage(msgBundle);
    }

    @NotNull
    private Bundle createMessageBundle(boolean value) {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, value);
        return msgBundle;
    }

    private void sendMessage(Bundle msgBundle) {
        Message msg = Message.obtain();
        msg.setData(msgBundle);
        messageHandler.sendMessage(msg);
    }
}
