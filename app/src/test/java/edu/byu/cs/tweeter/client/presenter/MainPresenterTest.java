package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.text.ParseException;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

class MainPresenterTest {

    private  MainPresenter.View mockMainView;
    private StatusService mockStatusService;
    private Cache mockCache;

    private MainPresenter mainPresenterSpy;

    @BeforeEach
    void setUp() {
        mockMainView = Mockito.mock(MainPresenter.View.class);
        mockStatusService = Mockito.mock(StatusService.class);
        mockCache = Mockito.mock(Cache.class);

        AuthToken authToken = new AuthToken();
        User user = new User();

        mainPresenterSpy = Mockito.spy(new MainPresenter(mockMainView, authToken, user));

        Mockito.doReturn(mockStatusService).when(mainPresenterSpy).getStatusService();
        Cache.setInstance(mockCache);
    }

    @Test
    void postStatusSuccess() {
        Answer<Void> postSucceededAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                StatusService.PostStatusObserver observer = invocation.getArgumentAt(2,StatusService.PostStatusObserver.class);
                observer.handlePostSuccess();
                return null;
            }
        };

        Mockito.doAnswer(postSucceededAnswer).when(mockStatusService).postStatus(Mockito.any(), Mockito.any(), Mockito.any());

        String post = "Test";

        // Run test case
        try {
            mainPresenterSpy.postStatus(post);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Verify expected methods were called on the view
        Mockito.verify(mockMainView).displayInfoMessage("Posting Status...");
        Mockito.verify(mockMainView).clearInfoMessage();
        Mockito.verify(mockMainView).displayInfoMessage("Successfully Posted!");

        // Verify expected methods were called in the presenter
        try {
            Mockito.verify(mainPresenterSpy).getFormattedDateTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Mockito.verify(mainPresenterSpy).parseURLs(post);
        Mockito.verify(mainPresenterSpy).parseMentions(post);
        Mockito.verify(mainPresenterSpy).handlePostSuccess();

        // Verify expected methods were called on the service
        Mockito.verify(mockStatusService).postStatus(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void postStatusFail() {
        String message = "post failed message";

        Answer<Void> postFailedAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                StatusService.PostStatusObserver observer = invocation.getArgumentAt(2,StatusService.PostStatusObserver.class);
                observer.handleFailure(message);
                return null;
            }
        };

        Mockito.doAnswer(postFailedAnswer).when(mockStatusService).postStatus(Mockito.any(), Mockito.any(), Mockito.any());

        String post = "Test";

        // Run test case
        try {
            mainPresenterSpy.postStatus(post);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Verify expected methods were called on the view
        Mockito.verify(mockMainView).displayInfoMessage("Posting Status...");
        Mockito.verify(mockMainView).clearInfoMessage();
        Mockito.verify(mockMainView, Mockito.times(0)).displayInfoMessage("Successfully Posted!");
        Mockito.verify(mockMainView).displayErrorMessage(message);

        // Verify expected methods were called in the presenter
        try {
            Mockito.verify(mainPresenterSpy).getFormattedDateTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Mockito.verify(mainPresenterSpy).parseURLs(post);
        Mockito.verify(mainPresenterSpy).parseMentions(post);
        Mockito.verify(mainPresenterSpy).handleFailure(message);

        // Verify expected methods were called on the service
        Mockito.verify(mockStatusService).postStatus(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void postStatusException() {
        String message = "post failed message";
        Exception ex = new RuntimeException(message);

        Answer<Void> postExceptionAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                StatusService.PostStatusObserver observer = invocation.getArgumentAt(2,StatusService.PostStatusObserver.class);
                observer.handleException(ex);
                return null;
            }
        };

        Mockito.doAnswer(postExceptionAnswer).when(mockStatusService).postStatus(Mockito.any(), Mockito.any(), Mockito.any());

        String post = "Test";

        // Run test case
        try {
            mainPresenterSpy.postStatus(post);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Verify expected methods were called on the view
        Mockito.verify(mockMainView).displayInfoMessage("Posting Status...");
        Mockito.verify(mockMainView).clearInfoMessage();
        Mockito.verify(mockMainView, Mockito.times(0)).displayInfoMessage("Successfully Posted!");
        Mockito.verify(mockMainView).displayErrorMessage(message);

        // Verify expected methods were called in the presenter
        try {
            Mockito.verify(mainPresenterSpy).getFormattedDateTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Mockito.verify(mainPresenterSpy).parseURLs(post);
        Mockito.verify(mainPresenterSpy).parseMentions(post);
        Mockito.verify(mainPresenterSpy).handleException(ex);

        // Verify expected methods were called on the service
        Mockito.verify(mockStatusService).postStatus(Mockito.any(), Mockito.any(), Mockito.any());
    }
}