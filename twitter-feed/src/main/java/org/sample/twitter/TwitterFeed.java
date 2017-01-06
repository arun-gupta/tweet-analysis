package org.sample.twitter;

import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Hello world!
 */
public class TwitterFeed {

    public static void main(String[] args) {
        new TwitterFeed().readFeed(args[0]);
    }

    void readFeed(String user) {
        try {
            TwitterCredentials creds = readCredentials();
            ConfigurationBuilder twitterConfig = new ConfigurationBuilder();
            twitterConfig.setOAuthConsumerKey(creds.getConsumerKey());
            twitterConfig.setOAuthConsumerSecret(creds.getConsumerSecret());
            twitterConfig.setOAuthAccessToken(creds.getAccessToken());
            twitterConfig.setOAuthAccessTokenSecret(creds.getAccessSecret());
            twitterConfig.setJSONStoreEnabled(true);
            Twitter twitter = new TwitterFactory(twitterConfig.build()).getInstance();

            List<Status> list = twitter.getUserTimeline(user);
            list.stream().forEach((status) -> {
                System.out.println("Sent by: @" 
                        + status.getUser().getScreenName()
                        + " - " + status.getUser().getName() 
                        + "\n" + status.getText()
                        + "\n");
            });
        } catch (TwitterException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static TwitterCredentials readCredentials() {
        try (InputStream is = new FileInputStream("twitter.json")) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(is, TwitterCredentials.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
