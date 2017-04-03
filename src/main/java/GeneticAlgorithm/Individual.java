package GeneticAlgorithm;

import Main.ProjectConfig;

import java.util.Random;

/**
 * Created by timmanas on 2017-02-21.
 */
public class Individual implements Comparable<Individual> {

    //region Constants
    private boolean[] genotype;
    private final Random rand = new Random();
    private int initialFitness = 0;
    private StockData stockData;
    //endregion

    //region Constructor
    public Individual() {
        genotype = new boolean[ProjectConfig.GENOME_SIZE];
    }
    //endregion


    //region Properties
    void random() {
        for (int i = 0; i < genotype.length; i++) {
            genotype[i] = 0.5 > rand.nextFloat();
        }
    }

    public void createGenome() {

        // Encode P/E Ratio
        boolean[] peRatioScore = encode_PERatio();
        addScoreToGenome(0, 1, peRatioScore);

        // Encode Div Yield
        boolean[] divYieldScore = encode_DivYield();
        addScoreToGenome(2,3, divYieldScore);

        // Encode EPS Growth Rate
        boolean[] epsGrowthRateScore = encode_EPSGrowthRate();
        addScoreToGenome(4,5, epsGrowthRateScore);

        // Encode Current Ratio
        boolean[] currentRatioScore = encode_CurrentRatio();
        addScoreToGenome(6,7, currentRatioScore);

        // Encode Return on Investment
        boolean[] returnInvestmentScore = encode_ReturnInvestment();
        addScoreToGenome(8,9, returnInvestmentScore);

        // Encode Return on Assets
        boolean[] returnAssetsScore = encode_ReturnAssets();
        addScoreToGenome(10,11, returnAssetsScore);

        // Encode Return on Equity
        boolean[] returnEquityScore = encode_ReturnEquity();
        addScoreToGenome(12,13, returnEquityScore);

    }

    private String gene() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < genotype.length; i++) {
            sb.append(genotype[i] == true ? 1 : 0);
        }
        return sb.toString();
    }

    int fitness() {
        int sum = 0;
        for (int i = 0; i < genotype.length; i++) {
            if (genotype[i])
                sum++;
        }

        if(initialFitness == 0)
            initialFitness = sum;

        return sum;
    }

    //endregion

    //region Getters
    public boolean[] getGenotype() {
        return genotype;
    }

    public Random getRand() {
        return rand;
    }

    public StockData getStockData(){
        return stockData;
    }

    public int getInitialFitness() {
        return initialFitness;
    }

    //endregion

    //region Setters
    public void setGenotype(boolean[] genotype) {
        this.genotype = genotype;
    }

    public void setInitialFitness(int initialFitness) {
        this.initialFitness = initialFitness;
    }

    public void setStockData(StockData stockData){
        this.stockData = stockData;
    }

    //endregion


    //region Helper
    public int compareTo(Individual o) {
        int f1 = this.fitness();
        int f2 = o.fitness();

        if (f1 < f2)
            return 1;
        else if (f1 > f2)
            return -1;
        else
            return 0;
    }

    @Override
    public String toString() {
        return "Symbol: " + stockData.getSYMBOL() + "     gene =" + gene() + " fitness =" + fitness();
    }

    private void addScoreToGenome(int start, int end, boolean[] score) {
        genotype[start] = score[0];
        genotype[end] = score[1];
    }

    //endregion

    //region Encoders
    private boolean[] encode_ReturnEquity() {
        boolean[] score = new boolean[2];
        double returnEquity = stockData.getReturnOnEquity();
//        System.out.println("Return on Equity: " + returnEquity);

        // Score 0 [0,0] -> 0 <= returnEquity <= 10
        if( 0 <= returnEquity && returnEquity <= 10){
            score[0] = false;
            score[1] = false;
            return score;
        }
        // Score 1 [0,1] -> 10 < returnEquity <= 20
        if(10 < returnEquity && returnEquity <= 20){
            score[0] = false;
            score[1] = true;
            return score;
        }
        // Score 2 [1,0] -> 20 < returnEquity <= 30
        if(20 < returnEquity && returnEquity <= 30){
            score[0] = true;
            score[1] = false;
            return score;
        }

        // Score 3 [1,1] -> 30 < return Equity
        if(30 < returnEquity){
            score[0] = true;
            score[1] = true;
            return score;
        }

        return score;
    }

    private boolean[] encode_ReturnAssets() {
        boolean[] score = new boolean[2];
        double returnAssets = stockData.getReturnOnAssets();
//        System.out.println("Return on Assets: " + returnAssets);

        // Score 0 [0,0] ->  0 <= returnAssets <= 10
        if( 0 <= returnAssets && returnAssets <= 10){
            score[0] = false;
            score[1] = false;
            return score;
        }

        // Score 1 [0,1] ->  10 < returnAssets <= 20
        if(10 < returnAssets && returnAssets <= 20){
            score[0] = false;
            score[1] = true;
            return score;
        }

        // Score 2 [1,0] ->  20 < returnAssets <= 30
        if(20 < returnAssets && returnAssets <= 30){
            score[0] = true;
            score[1] = false;
            return score;
        }

        // Score 3 [1,1] ->  30 < returnAssets
        if(30 < returnAssets){
            score[0] = true;
            score[1] = true;
            return score;
        }

        return score;
    }

    private boolean[] encode_ReturnInvestment() {
        boolean[] score = new boolean[2];
        double returnInvestment = stockData.getReturnOnInvestments();
//        System.out.println("Return On Investment: " + returnInvestment);

        // Score 0 [0,0] ->  0 < returnInvestment <= 10
        if( 0 <= returnInvestment && returnInvestment <= 10){
            score[0] = false;
            score[1] = false;
            return score;
        }

        // Score 1 [0,1] -> 10 < returnInvestment <= 20
        if( 10 < returnInvestment && returnInvestment <= 20){
            score[0] = false;
            score[1] = true;
            return score;
        }

        // Score 2 [1,0] -> 20 < returnInvestment <= 30
        if( 20 < returnInvestment && returnInvestment <= 30){
            score[0] = true;
            score[1] = false;
            return score;
        }

        // Score 3 [1,1] -> 30 < returnInvestment
        if(30 < returnInvestment){
            score[0] = true;
            score[1] = true;
            return score;
        }

        return score;
    }

    private boolean[] encode_CurrentRatio() {
        boolean[] score = new boolean[2];
        double currentRatio = stockData.getCurrent_Ratio();
//        System.out.println("CurrentRatio: " + currentRatio);

        // Score 0 [0,0] -> 0 <= CurrentRatio <= 10
        if( 0 <= currentRatio && currentRatio <= 10){
            score[0] = false;
            score[1] = false;
            return score;
        }

        // Score 1 [0,1] -> 10 < CurrentRatio <= 20
        if(10 < currentRatio && currentRatio <= 20){
            score[0] = false;
            score[1] = true;
            return score;
        }

        // Score 2 [1,0] -> 20 < CurrentRatio <= 30
        if(20 < currentRatio && currentRatio <= 30){
            score[0] = true;
            score[1] = false;
            return score;
        }

        // Score 3 [1,1] -> 30 < CurrentRatio
        if(30 < currentRatio){
            score[0] = true;
            score[1] = true;
            return score;
        }

        return score;
    }

    private boolean[] encode_EPSGrowthRate() {
        boolean[] score = new boolean[2];
        double epsGrowthRate = stockData.getEps_Growth();
//        System.out.println("EPSGrowth: " + epsGrowthRate );

        // Score 0 [0,0] -> 0 <= EPSGrowth <= 15
        if(0 <= epsGrowthRate && epsGrowthRate <= 10){
            score[0] = false;
            score[1] = false;
            return score;
        }

        // Score 1 [0,1] -> 10 < EPSGrowth <= 20
        if(10 < epsGrowthRate && epsGrowthRate <= 20){
            score[0] = false;
            score[1] = true;
            return score;
        }

        // Score 2 [1,0] -> 20 < EPSGrowth <= 30
        if(20 < epsGrowthRate && epsGrowthRate <= 30){
            score[0] = true;
            score[1] = false;
            return score;
        }

        // Score 3 [1,1] -> 30 < EPSGrowth
        if(30 < epsGrowthRate){
            score[0] = true;
            score[1] = true;
            return score;
        }

        return score;
    }

    private boolean[] encode_DivYield() {
        boolean[] score = new boolean[2];
        double divYield = stockData.getDivYield();
//        System.out.println("DivYield = "+ divYield);

        // Score 0 [0,0] -> 0 == DivYield
        if(divYield <= 0){
            score[0] = false;
            score[1] = false;
            return score;
        }

        // Score 1 [0,1] -> 0.1 < DivYield < 3
        if(0.1 < divYield && divYield <= 3 ){
            score[0] = false;
            score[1] = true;
            return score;
        }

        // Score 2 [1,0] -> 2 < DivYield < 5
        if(3 < divYield && divYield <= 6 ){
            score[0] = true;
            score[1] = false;
            return score;
        }

        // Score 4 [1,1] -> 5 < DivYield
        if( 6 < divYield ){
            score[0] = true;
            score[1] = true;
            return score;
        }

        return score;
    }

    private boolean[] encode_PERatio() {
        boolean[] score = new boolean[2];
        double peRatio = stockData.getPE_Ratio();
//        System.out.println("PeRatio = "+ peRatio);

        // Score 0 [0,0] -> 0 <= PERatio <= 15
        if(0 <= peRatio && peRatio <= 15 ){
            score[0] = false;
            score[1] = false;
            return score;
        }

        // Score 1 [0,1] -> 0.1 < PE Ratio < 20
        if(15 < peRatio && peRatio <= 25 ){
            score[0] = false;
            score[1] = true;
            return score;
        }

        // Score 2 [1,0] -> 2 < PE Ratio < 5
        if(25 < peRatio && peRatio <= 35 ){
            score[0] = true;
            score[1] = false;
            return score;
        }

        // Score 4 [1,1] -> 5 < PE Ratio
        if(35 < peRatio ){
            score[0] = true;
            score[1] = true;
            return score;
        }

        return score;
    }
    //endregion

}
