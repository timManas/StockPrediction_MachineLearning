package Main;

import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * Created by timmanas on 2017-02-09.
 */
public class ProjectConfig {

    //region Main Control Panel
    public static boolean useTestStockData = true;
    //endregion

    //region Main
    public static final String STOCK_SYMBOL = "ExxonMobil";
    //endregion

    //region Genetic Algorithm
    public static final int MAX_ITERATIONS_GA = 40;
    public static final int POPULATION_SIZE = 50;
    public static final double PARENT_PERCENTAGE = 0.1;
    public static final int GENOME_SIZE = 14;               // DO NOT CHANGE THIS
    public static final double MUTATION_PERCENTAGE = 0.01;
    public static final int MAX_TOP_INDIVIDUALS = 5;
    public static final String rawStockDataListPath = "input/StockHistoricalDataV1.csv";
    public static final int MAX_RUN_ITERATIONS = 10;
    //endregion

    //region Constant Neural Network
    public static final int MAX_ITERATIONS = 2500;
    public static final double LEARNING_RATE = 0.5;
    public static final double MAX_ERROR =  0.00001;
    public static final String charset = "ISO-8859-1";
    public static final String trainDirectory = "input/trainDirectory";
    public static final String classifierPath = "input/trainDirectory/classifier.txt";
    public static final String learningDataFilePath = "input/learningData.csv";
    public static final String rawDataFilePath = "input/TrainingData_SP500.csv";
    public static final String neuralNetworkModelFilePath = "stockPredictor.nnet";
    //endregion

    //region Sentiment Analysis
    public static final String NEWS_API_KEY = "a5a60aacd8b747feb07a5de92975b0e2";
    public static final int queryCount = 100; // # of tweets returned for each query
    public static final int MAX_NUM_RECEIVED_TWEETS = 1000;
    public static final String CONSUMER_KEY = "uYEQApzHyTfYNumIZqxXaJCK7";
    public static final String CONSUMER_SECRET = "vFhXEXOKcNyxxalUb2cebDXTRvjnP3gKglu5mqQFqaD6pizqdy";
    public static final String ACCESS_TOKEN = "826684415283707904-doBy2rh2EW0Yz1FFCHICpjfn1w7gVME";
    public static final String ACCESS_TOKEN_SECRET = "RvPW5mAvyUXsu2t0ARdHOZfmOksgHqW6jUf29THyTCKDo";

    public static final String NEWS_SOURCE_URL_BI = "https://newsapi.org/v1/articles?source=business-insider-uk&sortBy=top&apiKey=" + NEWS_API_KEY;
    public static final String NEWS_SOURCE_URL_BBC = "https://newsapi.org/v1/articles?source=bbc-news&sortBy=top&apiKey=" + NEWS_API_KEY;
    public static final String NEWS_SOURCE_URL_BLOOMBERG = "https://newsapi.org/v1/articles?source=bloomberg&sortBy=top&apiKey=" + NEWS_API_KEY;
    public static final String NEWS_SOURCE_URL_CNN = "https://newsapi.org/v1/articles?source=cnn&sortBy=top&apiKey=" + NEWS_API_KEY;
    public static final String NEWS_SOURCE_URL_FINANCIAL_TIMES= " https://newsapi.org/v1/articles?source=financial-times&sortBy=top&apiKey=" + NEWS_API_KEY;
    public static final String NEWS_SOURCE_URL_ECONOMIST = " https://newsapi.org/v1/articles?source=the-economist&sortBy=top&apiKey=" + NEWS_API_KEY;
    public static final String NEWS_SOURCE_URL_WALLSTJOURNAL = " https://newsapi.org/v1/articles?source=the-wall-street-journal&sortBy=top&apiKey=" + NEWS_API_KEY;

    //endregion
}
