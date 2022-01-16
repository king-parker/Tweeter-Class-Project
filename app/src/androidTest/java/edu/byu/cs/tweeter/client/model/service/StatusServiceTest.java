package edu.byu.cs.tweeter.client.model.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusServiceTest {

    private StatusService statusService;

    private MockStoryObserver observer;

    private CountDownLatch countDownLatch;

    private final String IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private final AuthToken authToken = new AuthToken("token", "time");
    private final User user = new User("Test", "User", IMAGE_URL);
    private final User user0 = new User("Allen", "Anderson", "@allen", IMAGE_URL);
    private final User user8 = new User("Elliott", "Enderson", "@elliott", IMAGE_URL);

    @Before
    public void setUp() {
        statusService = new StatusService();
        observer = Mockito.spy(new MockStoryObserver());

        Cache.getInstance().setCurrUserAuthToken(authToken);
        Cache.getInstance().setCurrUser(user);

        resetCountDownLatch();
    }

    private void resetCountDownLatch() {
        countDownLatch = new CountDownLatch(1);
    }

    private void awaitCountDownLatch() throws InterruptedException {
        countDownLatch.await();
        resetCountDownLatch();
    }

    @Test
    public void getStory() throws InterruptedException {
        int pageSize = 10;
        statusService.getStory(user, pageSize, null, observer);
        awaitCountDownLatch();

        Assert.assertNull(observer.getMessage());
        Assert.assertNull(observer.getException());
        Assert.assertTrue(observer.isSuccess());
        Assert.assertEquals(pageSize, observer.getStory().size());
        Assert.assertTrue(observer.getHasMorePages());

        Assert.assertEquals(user0, observer.getStory().get(0).getUser());
        Assert.assertEquals(user8, observer.getStory().get(8).getUser());

        Mockito.verify(observer).handleSuccess(Mockito.any(), Mockito.anyBoolean());
        Mockito.verify(observer, Mockito.times(0)).handleFailure(Mockito.any());
        Mockito.verify(observer, Mockito.times(0)).handleException(Mockito.any());
    }

    private class MockStoryObserver implements PagedServiceObserver<Status> {

        private boolean success;
        private String message;
        private List<Status> story;
        private boolean hasMorePages;
        private Exception exception;

        @Override
        public void handleSuccess(List<Status> statuses, boolean hasMorePages) {
            this.success = true;
            this.message = null;
            this.story = statuses;
            this.hasMorePages = hasMorePages;
            this.exception = null;

            countDownLatch.countDown();
        }

        @Override
        public void handleFailure(String message) {
            this.success = false;
            this.message = message;
            this.story = null;
            this.hasMorePages = false;
            this.exception = null;

            countDownLatch.countDown();
        }

        @Override
        public void handleException(Exception ex) {
            this.success = false;
            this.message = null;
            this.story = null;
            this.hasMorePages = false;
            this.exception = ex;

            countDownLatch.countDown();
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public List<Status> getStory() {
            return story;
        }

        public boolean getHasMorePages() {
            return hasMorePages;
        }

        public Exception getException() {
            return exception;
        }
    }
}