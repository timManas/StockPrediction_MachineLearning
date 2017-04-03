package SentimentAnalisys.NewsFeed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by timmanas on 2017-02-09.
 */
public class NewsDataFeed {

    private ArrayList<String> newsArticles;

    public NewsDataFeed(){
        newsArticles = new ArrayList<>();
    }

    public void readJSONFromURL(String url){
        InputStream is = null;
        try {
            is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readJSONContents(rd);
            System.out.println("News Feed: " + jsonText);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String readJSONContents(BufferedReader rd) {
        StringBuilder sb = new StringBuilder();
        int cp;

        try {
            while ((cp = rd.read()) != -1){
                sb.append((char) cp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public ArrayList<String> getNewsArticleList(){
        return this.newsArticles;
    }

}
