package edu.byu.cs.tweeter.client.model.net;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;

public class ServerFacadeTest {

    private final ServerFacade serverFacade = new ServerFacade();
    private static final AuthToken authToken = new AuthToken("token", "time");
    private static final User user = new User("Allen", "Anderson", "@allen", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

    @BeforeEach
    public void setUp() throws Exception {
    }

    @Test
    public void RegistrationIntegrationTest() throws IOException, TweeterRemoteException {
        RegisterRequest request = new RegisterRequest("test","user", "@test", "pass", "image");
        RegisterResponse response = serverFacade.sendRequest(request, "/user/register", RegisterResponse.class);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(authToken, response.getAuthToken());
        Assertions.assertEquals(user, response.getUser());
        Assertions.assertNull(response.getMessage());
    }

    @Test
    public void GetFollowersIntegrationTest() throws IOException, TweeterRemoteException {
        /**
         * Test user profile images.
         */
        final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
        final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";

        /**
         * Generated users.
         */
        User user1 = new User("Allen", "Anderson", "@allen", MALE_IMAGE_URL);
        User user2 = new User("Amy", "Ames", "@amy", FEMALE_IMAGE_URL);
        User user3 = new User("Bob", "Bobson", "@bob", MALE_IMAGE_URL);
        User user4 = new User("Bonnie", "Beatty", "@bonnie", FEMALE_IMAGE_URL);
        User user5 = new User("Chris", "Colston", "@chris", MALE_IMAGE_URL);
        User user6 = new User("Cindy", "Coats", "@cindy", FEMALE_IMAGE_URL);
        User user7 = new User("Dan", "Donaldson", "@dan", MALE_IMAGE_URL);
        User user8 = new User("Dee", "Dempsey", "@dee", FEMALE_IMAGE_URL);
        User user9 = new User("Elliott", "Enderson", "@elliott", MALE_IMAGE_URL);
        User user10 = new User("Elizabeth", "Engle", "@elizabeth", FEMALE_IMAGE_URL);
        User user11 = new User("Frank", "Frandson", "@frank", MALE_IMAGE_URL);
        User user12 = new User("Fran", "Franklin", "@fran", FEMALE_IMAGE_URL);
        User user13 = new User("Gary", "Gilbert", "@gary", MALE_IMAGE_URL);
        User user14 = new User("Giovanna", "Giles", "@giovanna", FEMALE_IMAGE_URL);
        User user15 = new User("Henry", "Henderson", "@henry", MALE_IMAGE_URL);
        User user16 = new User("Helen", "Hopwell", "@helen", FEMALE_IMAGE_URL);
        User user17 = new User("Igor", "Isaacson", "@igor", MALE_IMAGE_URL);
        User user18 = new User("Isabel", "Isaacson", "@isabel", FEMALE_IMAGE_URL);
        User user19 = new User("Justin", "Jones", "@justin", MALE_IMAGE_URL);
        User user20 = new User("Jill", "Johnson", "@jill", FEMALE_IMAGE_URL);
        User user21 = new User("John", "Brown", "@john", MALE_IMAGE_URL);

        List<User> firstPageUsers = Arrays.asList(
                user1, user2, user3, user4, user5, user6, user7, user8, user9, user10
        );

        List<User> secondPageUsers = Arrays.asList(
                user11, user12, user13, user14, user15, user16, user17, user18, user19, user20
        );

        List<User> finalPageUsers = Arrays.asList(
                user21
        );

        String url = "/follow/following";

        FollowingRequest request = new FollowingRequest(authToken, user.getAlias(), 10, null);
        FollowingResponse response = serverFacade.sendRequest(request, url, FollowingResponse.class);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(firstPageUsers, response.getFollowees());
        Assertions.assertNull(response.getMessage());

        request = new FollowingRequest(authToken, user.getAlias(), 10, firstPageUsers.get(9).getAlias());
        response = serverFacade.sendRequest(request, url, FollowingResponse.class);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(secondPageUsers, response.getFollowees());
        Assertions.assertNull(response.getMessage());

        request = new FollowingRequest(authToken, user.getAlias(), 10, secondPageUsers.get(9).getAlias());
        response = serverFacade.sendRequest(request, url, FollowingResponse.class);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(finalPageUsers, response.getFollowees());
        Assertions.assertNull(response.getMessage());
    }

    @Test
    public void GetFollowingIntegrationTest() throws IOException, TweeterRemoteException {
        int numFollowees = 22;

        FollowingCountRequest request = new FollowingCountRequest(authToken, user.getAlias(), user.getAlias());
        FollowingCountResponse response = serverFacade.sendRequest(request, "/follow/following/count", FollowingCountResponse.class);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(numFollowees, response.getCount());
        Assertions.assertNull(response.getMessage());
    }
}