package org.sample.twitter;

import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Hello world!
 */
public class GetFeed {

    public static void main(String[] args) {
        new GetFeed().readFeed(args[0]);
    }

    void readFeed(String user) {
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            TwitterCredentials creds = readCredentials();
            twitter.setOAuthConsumer(creds.getKey(), creds.getSecret());
            RequestToken requestToken = twitter.getOAuthRequestToken();
            System.out.println("Authorization URL: \n" + requestToken.getAuthorizationURL());

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Hit above Authorization URL and Input PIN here: ");
            String pin = br.readLine();

            AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, pin);

            System.out.println("Access Token: " + accessToken.getToken());
            System.out.println("Access Token Secret: " + accessToken.getTokenSecret());

            // I'm reading your timeline
            List<Status> list = twitter.getUserTimeline(user);
            list.stream().forEach((status) -> {
                System.out.println("Sent by: @" 
                        + status.getUser().getScreenName()
                        + " - " + status.getUser().getName() 
                        + "\n" + status.getText()
                        + "\n");
            });
        } catch (TwitterException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    TwitterCredentials readCredentials() {
        try (InputStream is = new FileInputStream("twitter.json")) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(is, TwitterCredentials.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
