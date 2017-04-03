package SentimentAnalisys.DataManager;

import Main.ProjectConfig;
import SentimentAnalisys.SentimentClassifier;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by timmanas on 2017-02-09.
 */
public class DataFeedManager {
    SentimentClassifier sentimentClassifier;
    ConfigurationBuilder configurationBuilder;
    Twitter twitter;
    QueryResult queryResult;

    public DataFeedManager() {
        sentimentClassifier = new SentimentClassifier();

        configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(ProjectConfig.CONSUMER_KEY);
        configurationBuilder.setOAuthConsumerSecret(ProjectConfig.CONSUMER_SECRET);
        configurationBuilder.setOAuthAccessToken(ProjectConfig.ACCESS_TOKEN);
        configurationBuilder.setOAuthAccessTokenSecret(ProjectConfig.ACCESS_TOKEN_SECRET);

        twitter = new TwitterFactory(configurationBuilder.build()).getInstance();
    }

    public int[]    performQuery(String companyName) throws InterruptedException, IOException, TwitterException {
        Query query = new Query(companyName);
        query.setCount(ProjectConfig.queryCount);
        int count = 0;
        int result[] = new int[3];
        int pos = 0;
        int neu = 0;
        int neg = 0;

        try {
            do {
                System.out.println("Count: " + count);
                queryResult = twitter.search(query);
                ArrayList tweetList = (ArrayList) queryResult.getTweets();
                System.out.println("TweetList Size: " + tweetList.size());

                for (int i = 0; i < tweetList.size() && count < ProjectConfig.MAX_NUM_RECEIVED_TWEETS; i++) {
                    Status tweet = (Status) tweetList.get(i);

                    if(tweet.getLang().equalsIgnoreCase("en")){
                        String text = tweet.getText();
                        String sent = sentimentClassifier.classify(text);

                        System.out.println("Text: " + text);
                        System.out.println("Sentiment: " + sent);

                        if(sent.equalsIgnoreCase("pos"))
                            pos++;
                        else if(sent.equalsIgnoreCase("neu"))
                            neu++;
                        else if(sent.equalsIgnoreCase("neg"))
                            neg++;
                    }
                }
                count++;
            } while ((query = queryResult.nextQuery()) != null && count < ProjectConfig.MAX_NUM_RECEIVED_TWEETS);


        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Could not connect to Twitter");
        }

        System.out.println("Num Positive: " + pos + "   Neutral: " + neu + "   Negative: " + neg);

        result [0] = pos;
        result [1] = neu;
        result [2] = neg;

        return result;
    }
}
