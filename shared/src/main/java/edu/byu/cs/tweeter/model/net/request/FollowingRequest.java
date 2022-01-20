package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * followees for a specified follower.
 */
public class FollowingRequest extends AuthorizedRequest {
    private String followerAlias;
    private Integer limit;
    private String lastFolloweeAlias;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private FollowingRequest() {super();}

    public FollowingRequest(AuthToken authToken, String currUserAlias, int limit, String lastFolloweeAlias) {
        super(authToken, currUserAlias);
        this.followerAlias = currUserAlias;
        this.limit = limit;
        this.lastFolloweeAlias = lastFolloweeAlias;
    }

    /**
     * Creates an instance.
     *
     * @param followerAlias the alias of the user whose followees are to be returned.
     * @param limit the maximum number of followees to return.
     * @param lastFolloweeAlias the alias of the last followee that was returned in the previous request (null if
     *                     there was no previous request or if no followees were returned in the
     *                     previous request).
     */
    public FollowingRequest(AuthToken authToken, String currUserAlias, String followerAlias, int limit, String lastFolloweeAlias) {
        super(authToken, currUserAlias);
        this.followerAlias = followerAlias;
        this.limit = limit;
        this.lastFolloweeAlias = lastFolloweeAlias;
    }

    /**
     * Returns the follower whose followees are to be returned by this request.
     *
     * @return the follower.
     */
    public String getFollowerAlias() {
        return followerAlias;
    }

    /**
     * Sets the follower.
     *
     * @param followerAlias the follower.
     */
    public void setFollowerAlias(String followerAlias) {
        this.followerAlias = followerAlias;
    }

    /**
     * Returns the number representing the maximum number of followees to be returned by this request.
     *
     * @return the limit.
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * Sets the limit.
     *
     * @param limit the limit.
     */
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    /**
     * Returns the last followee that was returned in the previous request or null if there was no
     * previous request or if no followees were returned in the previous request.
     *
     * @return the last followee.
     */
    public String getLastFolloweeAlias() {
        return lastFolloweeAlias;
    }

    /**
     * Sets the last followee.
     *
     * @param lastFolloweeAlias the last followee.
     */
    public void setLastFolloweeAlias(String lastFolloweeAlias) {
        this.lastFolloweeAlias = lastFolloweeAlias;
    }

    @Override
    public String toString() {
        return "FollowingRequest{" +
                "authToken=" + this.getAuthToken() +
                ", currUserAlias='" + this.getCurrUserAlias() + '\'' +
                ", followerAlias='" + followerAlias + '\'' +
                ", limit=" + limit +
                ", lastFolloweeAlias='" + lastFolloweeAlias + '\'' +
                '}';
    }
}
