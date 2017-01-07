package org.sample.twitter;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/**
 * @author arungupta
 */
public class TwitterRequestHandler implements RequestHandler<String, String> {

    @Override
    public String handleRequest(String name, Context context) {
        new TwitterFeed().readFeed("arungupta");
        
        return null;
    }
    
}
