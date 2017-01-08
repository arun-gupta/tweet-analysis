package org.sample.twitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.auth.AccessToken;

/**
 * @author arungupta
 */
public class AccessTokenCredentials {

    void printAccessToken() {
        try {
            TwitterCredentials creds = TwitterFeed.readCredentials();
            Twitter twitter = new TwitterFactory().getInstance();
            twitter.setOAuthConsumer(creds.getConsumerKey(), creds.getConsumerSecret());
            RequestToken requestToken = twitter.getOAuthRequestToken();
            System.out.println("Authorization URL: \n" + requestToken.getAuthorizationURL());
            
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            
            System.out.print("Hit above Authorization URL and Input PIN here: ");
            String pin = br.readLine();
            
            AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, pin);
            
            System.out.println("Access Token: " + accessToken.getToken());
            System.out.println("Access Token Secret: " + accessToken.getTokenSecret());
        } catch (TwitterException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
