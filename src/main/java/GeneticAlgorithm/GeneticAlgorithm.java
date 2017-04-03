package GeneticAlgorithm;

import Main.ProjectConfig;
import org.jenetics.internal.util.Hash;

import java.io.*;
import java.util.*;


public class GeneticAlgorithm {

    //region Constants
    private ArrayList<StockData> stockList = new ArrayList<StockData>();
    private LinkedList<Individual> originalPopulation = null;
    private LinkedList<Individual> population = new LinkedList<Individual>();
    private ArrayList<Individual> topIndividuals = new ArrayList<Individual>();
    private HashMap<String, Integer> stockRankMap = new HashMap<String, Integer>();
    private final Random rand = new Random();
    private boolean isWithinTopPopulation = false;
    private int iterationCount = 0;
    //endregion


    //region Constructor
    public GeneticAlgorithm() throws IOException {
        // Parse the StockHistoricalData here
        parseStockList();
        for(int i = 0; i < stockList.size(); i++){
            Individual individual = new Individual();
            individual.setStockData(stockList.get(i));
//            individual.random();
            individual.createGenome();
            population.add(individual);
        }

        // Sorts the newly created population
        Collections.sort(population); // sort method
        if(originalPopulation == null)
            originalPopulation = population;

        System.out.println("\nPopulation Created & Sorted");
        print();

        initializeTopIndividual();
    }



    //endregion

    //region
    public void run() {

        int count = 0;
        while (count < ProjectConfig.MAX_ITERATIONS_GA) {
            count++;
            produceNextGen();
        }

        System.out.println("\n\n");
        System.out.println("Result");
        print();

        System.out.println("\n\n");
        System.out.println("Top Individuals based on Fitness");
        printTopIndividualBasedOnFitness();

        System.out.println("\n\n");
        System.out.println("=====================================================================");
        System.out.println("=====================================================================");
        System.out.println("\n");

        getStockRanking();
        System.out.println("Stock Ranking Based on Genetic Algorithm");
        printStockRanking();

        System.out.println("\n");
        System.out.println("=====================================================================");
        System.out.println("=====================================================================");
        System.out.println("\n\n");

        if(iterationCount < ProjectConfig.MAX_RUN_ITERATIONS) {
            iterationCount++;
        }else{
            System.out.println("ERROR \nStock Not within Range of Acceptable Ranking \nExiting Program... \nPlease restart again");
            System.exit(0);
        }


    }

    //endregion

    //region LifeCycle
    /**
     * Selection strategy: Tournament method Replacement strategy: elitism 10%
     * and steady state find 4 random in population not same let 2 fight, and 2
     * fight the winners makes 2 children
     */
    private void produceNextGen() {
        LinkedList<Individual> newpopulation = new LinkedList<>();

        while (newpopulation.size() < ProjectConfig.POPULATION_SIZE * (1.0 - ProjectConfig.PARENT_PERCENTAGE )) {

            int size = population.size();
            int i = rand.nextInt(size);
            int j, k, l;

            j = k = l = i;

            while (j == i)
                j = rand.nextInt(size);
            while (k == i || k == j)
                k = rand.nextInt(size);
            while (l == i || l == j || k == l)
                l = rand.nextInt(size);

            Individual individual1 = population.get(i);
            Individual individual2 = population.get(j);
            Individual individual3 = population.get(k);
            Individual individual4 = population.get(l);

            int fitness1 = individual1.fitness();
            int fitness2 = individual2.fitness();
            int fitness3 = individual3.fitness();
            int fitness4 = individual4.fitness();

            Individual w1;
            Individual w2;

            if (fitness1 > fitness2)
                w1 = individual1;
            else
                w1 = individual2;

            if (fitness3 > fitness4)
                w2 = individual3;
            else
                w2 = individual4;

            Individual child1;
            Individual child2;

            // Method uniform crossover
            Individual[] childs = newChilds(w1, w2);
            child1 = childs[0];
            child2 = childs[1];

            boolean m1 = rand.nextFloat() <= ProjectConfig.MUTATION_PERCENTAGE;
            boolean m2 = rand.nextFloat() <= ProjectConfig.MUTATION_PERCENTAGE;

            // Mutation
            if (m1)
                mutate(child1);
            if (m2)
                mutate(child2);

            boolean isChild1Good = child1.fitness() >= w1.fitness();
            boolean isChild2Good = child2.fitness() >= w2.fitness();

            newpopulation.add(isChild1Good ? child1 : w1);
            newpopulation.add(isChild2Good ? child2 : w2);
        }

        // Add top percent to the parent
        int j = (int) (ProjectConfig.POPULATION_SIZE * ProjectConfig.PARENT_PERCENTAGE / 100.0);
        for (int i = 0; i < j; i++) {
            newpopulation.add(population.get(i));
        }

        population = newpopulation;
        Collections.sort(population);
    }



    // Uniform crossover
    private Individual[] newChilds(Individual c1, Individual c2) {
        Individual child1 = new Individual();
        Individual child2 = new Individual();

        for (int i = 0; i < ProjectConfig.GENOME_SIZE; i++) {
            boolean b = rand.nextFloat() >= 0.5;
            if (b) {
                child1.getGenotype()[i]= c1.getGenotype()[i];
                child2.getGenotype()[i] = c2.getGenotype()[i];

                child1.setStockData(c1.getStockData());
                child2.setStockData(c2.getStockData());

            } else {
                child1.getGenotype()[i] = c2.getGenotype()[i];
                child2.getGenotype()[i] = c1.getGenotype()[i];

                child1.setStockData(c2.getStockData());
                child2.setStockData(c1.getStockData());
            }
        }
        return new Individual[] { child1, child2 };
    }

    private void mutate(Individual c) {
        int i = rand.nextInt(ProjectConfig.GENOME_SIZE);
        c.getGenotype()[i] = !c.getGenotype()[i]; // flip
    }

    //endregion

    //region Getters
    public void getStockRanking() {
        for(int i = 0; i < population.size(); i++){
            Individual individual = population.get(i);
            String symbol = individual.getStockData().getSYMBOL();
            int fitness = individual.getInitialFitness();
            stockRankMap.put(symbol, fitness);
        }
    }

    public boolean isWithinTopPopulation() {
        return isWithinTopPopulation;
    }

    private void parseStockList() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(ProjectConfig.rawStockDataListPath));
        try{
            String line;
            int counter = 0;
            while ((line = reader.readLine()) != null){
                String[] tokens = line.split(",");
                StockData stockData = new StockData();
                stockData.setCompanyName(tokens[0].replace("-", "No Company Listed"));
                stockData.setSYMBOL(tokens[1].replace("-", "No Symbol Listed"));
                stockData.setMarketCap(tokens[2].replace("-", "0"));
                stockData.setPE_Ratio(Double.parseDouble(tokens[3].replace("-", "0")));
                stockData.setDivYield(Double.parseDouble(tokens[4].replace("-", "0")));
                stockData.setEps_Growth(Double.parseDouble(tokens[5].replace("-", "0")));
                stockData.setCurrent_Ratio(Double.parseDouble(tokens[6].replace("-", "0")));
                stockData.setReturnOnInvestments(Double.parseDouble(tokens[7].replace("-", "0")));
                stockData.setReturnOnAssets(Double.parseDouble(tokens[8].replace("-", "0")));
                stockData.setReturnOnEquity(Double.parseDouble(tokens[9].replace("-", "0")));

                counter++;
                stockList.add(stockData);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            reader.close();
        }

        System.out.println("Finished Parsing Stock Data List");
    }
    //endregion

    //region Helper
    public void print() {
        for (Individual c : population) {
            System.out.println(c);
        }
    }

    private void initializeTopIndividual() {
        for(int i = 0; i < ProjectConfig.MAX_TOP_INDIVIDUALS; i++){
            topIndividuals.add(population.get(i));
        }
    }

    public void printTopIndividualBasedOnFitness() {
        for(Individual individual : topIndividuals){
          System.out.println("Symbol: " + individual.getStockData().getSYMBOL() + "   " + "Fitness: " + individual.getInitialFitness());
        }
    }

    public void printStockRanking() {
        Iterator iterator = stockRankMap.entrySet().iterator();
        System.out.println("Printing Stock Ranking");
        while(iterator.hasNext()){
            Map.Entry pair = (Map.Entry) iterator.next();
            System.out.println("Symbol: " + pair.getKey() + "   " + "Fitness: " + pair.getValue());
//            iterator.remove();
        }

    }



    public Individual checkIfStockRankingMatchesFitness(){
        Iterator iterator = stockRankMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry pair = (Map.Entry) iterator.next();
            for(Individual individual : topIndividuals){
                String symbol = individual.getStockData().getSYMBOL();

                System.out.println("Stock Rank Symbol: " + pair.getKey() + "   " + " Top Individual on Fitness: " + individual.getStockData().getSYMBOL());
                if(symbol.equalsIgnoreCase(pair.getKey().toString())) {
                    isWithinTopPopulation = true;
                    return individual;
                }
            }
            iterator.remove();
        }
        return null;
    }

    //endregion


}