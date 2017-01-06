package org.sample.twitter;

/**
 * @author arungupta
 */
public class TwitterCredentials {
    private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String accessSecret;

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String key) {
        this.consumerKey = key;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public void setConsumerSecret(String secret) {
        this.consumerSecret = secret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }
}
