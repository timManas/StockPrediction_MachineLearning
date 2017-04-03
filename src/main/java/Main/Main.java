package Main;

import ArtificialNeuralNetwork.NeuralNetwork.NeuralNetworkStockPredictor;
import GeneticAlgorithm.GeneticAlgorithm;
import GeneticAlgorithm.Individual;
import OutputResult.PredictionResult;
import SentimentAnalisys.DataManager.DataFeedManager;
import SentimentAnalisys.NewsFeed.NewsDataFeed;
import SentimentAnalisys.SentimentLauncher;
import twitter4j.TwitterException;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {


        GeneticAlgorithm geneticAlgorithm;
        Individual bestIndividual = null;
        String bestIndividualCompanyName = null;
        String bestIndividualSymbol = null;
        double predictedValue = 0;
        int sentimentAnalysisResults[] = new int[3];

        //region Genetic Algorithm

        if(!ProjectConfig.useTestStockData){
            try {
                geneticAlgorithm = new GeneticAlgorithm();

                do{
                    geneticAlgorithm.run();
                    bestIndividual = geneticAlgorithm.checkIfStockRankingMatchesFitness();
                }while (!geneticAlgorithm.isWithinTopPopulation());

                if(bestIndividual != null){
                    bestIndividualCompanyName = bestIndividual.getStockData().getCompanyName();
                    bestIndividualSymbol = bestIndividual.getStockData().getSYMBOL();
                    System.out.println("Best stock listed: " + bestIndividualCompanyName + "    " + " Symbol: " + bestIndividualSymbol);
                }
                System.out.println("\n\n");
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(bestIndividual == null){
                System.out.println("System unable to locate the Best stock for Analysis ... Now Exiting");
                return;
            }
        }

        //endregion

        String companyName = ProjectConfig.useTestStockData ? ProjectConfig.STOCK_SYMBOL : bestIndividualCompanyName;

        //region Sentiment Analysis
        NewsDataFeed newsDataFeed = new NewsDataFeed();
        newsDataFeed.readJSONFromURL(ProjectConfig.NEWS_SOURCE_URL_BBC);
        // Train the SentimentAnalysis Model first
        SentimentLauncher.train(); //use this to train

        // Perform Query
        DataFeedManager dataFeedManager = new DataFeedManager();
        try {
           sentimentAnalysisResults = dataFeedManager.performQuery(companyName);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        //endregion


        //region Neural Network
        // Analyze Stock Data using Neural Network
        String companySymbol = ProjectConfig.useTestStockData ? ProjectConfig.STOCK_SYMBOL : bestIndividualSymbol;
        NeuralNetworkStockPredictor predictor = new NeuralNetworkStockPredictor(5);
        try {
            predictor.prepareData(companySymbol);
            System.out.println("Training starting");
            predictor.trainNetwork();

            System.out.println("Testing network");
            predictedValue = predictor.testNetwork();

        } catch (IOException e) {
            e.printStackTrace();
        }
        //endregion

        //region Prediction Result

        PredictionResult predictionResult = new PredictionResult();
        predictionResult.analyzeBestTradingStrategy(companyName, sentimentAnalysisResults[0], sentimentAnalysisResults[1], sentimentAnalysisResults[2], predictedValue);

        //endregion





    }

}


/**
    List of Improvements to be accomplished

 Done - Refactor Project Structure  -> One directory for Sentiment Analysis, Neural Network, OutputResult (BUY, HOLD, SELL)
 Done - Use feed from Stuff Networks (World, Business, Politics)
 Done, but no effect - Set the neg, neu & pos training Directory
 Done but can be Improved significantly - Implement a Genetic Algorithm

Done - Implement a improved Sentiment Analysis
 - Implement User Action Algorithm for (BUY, HOLD, SELL)
 - Refactor Code
 - Add visual representation using Neuroph Studio for results
 */