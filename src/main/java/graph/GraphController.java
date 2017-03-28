package graph;

import models.Request;
import models.Response;
import org.springframework.http.HttpStatus;
import org.springframework.social.twitter.api.*;
import org.springframework.web.bind.annotation.*;


import javax.inject.Inject;
import org.springframework.social.connect.ConnectionRepository;

import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

/**
 *
 *
 * @author SCO
 */

@RestController
public class GraphController {
    private static Integer tweetCounter = 0;

    private Twitter twitter;

    private ConnectionRepository connectionRepository;

    private Stream stream;

    @Inject
    public GraphController(Twitter twitter, ConnectionRepository connectionRepository) {
        this.twitter = twitter;
        this.connectionRepository = connectionRepository;
    }

    @RequestMapping(value = "/search/{q}", method=RequestMethod.GET)
    public String search(@PathVariable(value="q") String q, Model model) {
        if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
            return "KO";
        }

        SearchResults searchResults = twitter.searchOperations().search(q);
        List tweets = searchResults.getTweets();
        SearchMetadata searchMetadata = searchResults.getSearchMetadata();

        System.out.println("elem" + searchMetadata.getMaxId());

        tweets.forEach(o -> {

            Tweet t = (Tweet) o;

            System.out.println("------------------------------------------");
            System.out.println("t.getRetweetCount()" + t.getRetweetCount());
            System.out.println("t.getFromUser()" + t.getFromUser());
            System.out.println("t.getIdStr()" + t.getIdStr());
            System.out.println("t.getInReplyToScreenName()" + t.getInReplyToScreenName());
            //System.out.println("t.getProfileImageUrl()" + t.getProfileImageUrl());
            System.out.println("t.getText()" + t.getText());
            System.out.println("t.getFavoriteCount()" + t.getFavoriteCount());
            System.out.println("t.getRetweetedStatus()" + t.getRetweetedStatus());
            System.out.println("t.hasTags()" + t.hasTags());
            System.out.println("------------------------------------------");

        });

        return "OK";
    }

    @RequestMapping(value = "/streamSample", method=RequestMethod.GET)
    public String streamingOpsSample(Model model) {
        if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
            return "KO";
        }

        StreamingOperations streamingOperations = twitter.streamingOperations();
        streamingOperations.sample(Arrays.asList(new TweetListener()));

        return "OK";
    }

    @RequestMapping(value = "/streamTrack/{q}", method=RequestMethod.GET)
    public String streamParameters(@PathVariable(value="q") String q, Model model) {
        if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
            return "KO";
        }

        FilterStreamParameters filterParams = new FilterStreamParameters();
        filterParams.track( q );

        StreamingOperations streamingOperations = twitter.streamingOperations();
        streamingOperations.filter(filterParams, Arrays.asList( new TweetListener() ) );

        return "OK";
    }

    /**
     * Open a stream connection to track a parameter.
     *
     * @param req - the parameter to filter the search
     * @return - operation feedback
     */
    @RequestMapping(value = "/track", method=RequestMethod.POST)
    public String track( @RequestBody Request req ) {
        tweetCounter = 0;

        if (connectionRepository.findPrimaryConnection( Twitter.class ) == null) {
            return "KO";
        }

        FilterStreamParameters filterParams = new FilterStreamParameters();
        filterParams.track( req.getText() );

        StreamingOperations streamingOperations = twitter.streamingOperations();
        streamingOperations.filter( filterParams, Arrays.asList( new TweetListener() ) );

        return "OK";
    }

    /**
     * This method retrieves the counter of tweets in a defined
     * period of time and reset the counter.
     *
     * @return the number of tweets
     */
    @RequestMapping(value="/getTrackData", method=RequestMethod.GET)
    public Response getTrackData() {
        System.out.println("Getting track counter -> " + tweetCounter);

        Integer tweetsPerPeriod = tweetCounter;

        tweetCounter = 0;

        return new Response(tweetsPerPeriod);
    }


    private class TweetListener implements StreamListener {

        public TweetListener(){
            System.out.println( "Creating listener..." );
        }


        @Override
        public void onTweet(Tweet tweet) {
            System.out.println( tweet.getText() );
            tweetCounter++;
        }

        @Override
        public void onDelete(StreamDeleteEvent streamDeleteEvent) {
            System.out.println("Tweet deleted!");
        }

        @Override
        public void onLimit(int i) {
            System.out.println("Track limited!");
        }

        @Override
        public void onWarning(StreamWarningEvent streamWarningEvent) {
            System.out.println("Warning! Client is stalling, the connection may be closed!");
        }


    }




}
