package edu.byu.cs.tweeter.model.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

import edu.byu.cs.tweeter.model.util.Timestamp;

/**
 * Represents an auth token in the system.
 */
public class AuthToken implements Serializable {

    private static final int TOKEN_LENGTH = 16;

    public static String generateToken() {
        char[] availableChars = {'1','2','3','4','5','6','7','8','9','0','a','b','c','d','e','f'};
        StringBuilder token = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            if ((i > 0) && ((i%4) == 0)) {
                token.append('-');
            }
            token.append(availableChars[random.nextInt(availableChars.length)]);
        }
        return token.toString();
    }

    public String userAlias;

    /**
     * Value of the auth token.
     */
    public String token;

    /**
     * String representation of date/time at which the auth token was created.
     */
    public String datetime;

    public Boolean isValid;

    public String lastActivity;

    private AuthToken() {
    }

    public AuthToken(String token) {
        this.token = token;
        this.datetime = Timestamp.getDatetime();
    }

    public AuthToken(String token, String datetime) {
        this.token = token;
        this.datetime = datetime;
    }

    public AuthToken(String userAlias, String token, String datetime, Boolean isValid) {
        this.userAlias = userAlias;
        this.token = token;
        this.datetime = datetime;
        this.isValid = isValid;
    }

    public AuthToken(String userAlias, String token, String datetime, Boolean isValid, String lastActivity) {
        this.userAlias = userAlias;
        this.token = token;
        this.datetime = datetime;
        this.isValid = isValid;
        this.lastActivity = lastActivity;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

    public String getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(String lastActivity) {
        this.lastActivity = lastActivity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken authToken = (AuthToken) o;
        return userAlias.equals(authToken.userAlias) && token.equals(authToken.token) && datetime.equals(authToken.datetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userAlias, token, datetime);
    }

    @Override
    public String toString() {
        return "AuthToken{" +
                "userAlias='" + userAlias + '\'' +
                ", token='" + token + '\'' +
                ", datetime='" + datetime + '\'' +
                ", isValid=" + isValid +
                ", lastActivity='" + lastActivity + '\'' +
                '}';
    }
}
