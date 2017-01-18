package org.sample.twitter;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/**
 * @author arungupta
 */
public class TwitterRequestHandler implements RequestHandler<Request, String> {

    @Override
    public String handleRequest(Request request, Context context) {
        if (request.getName() == null)
            request.setName("realDonaldTrump");
        
        int tweets = new TwitterFeed().readFeed(request.getName());
        
        return "Updated " + tweets + " tweets for " + request.getName() + "!";
    }
    
}
