package OutputResult;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by timmanas on 2017-02-09.
 */
public class PredictionResult {


    public void analyzeBestTradingStrategy(String bestIndividualCompanyName, int numPos, int numNeu, int  numNeg, double predictedPrice){
        String action;

        if(numPos > numNeg  && numPos > numNeu)
            action = "Buy";
        else if(numNeg > numPos && numNeg > numNeu)
            action = "Sell";
        else
            action = "Hold";

        System.out.println("\n=====================================================================");
        System.out.println("=====================================================================");

        System.out.println("\n\nResults");
        System.out.println("\n" +
                "\nBest Stock picked based on Genetic Algorithm: " +  bestIndividualCompanyName +
                "\nSuggest Trader Action Based on Live News Feeds: " + action +
                "\nPredicted Value: " + predictedPrice +
                "\n"
        );

        System.out.println("=====================================================================");
        System.out.println("=====================================================================");


    }

}
