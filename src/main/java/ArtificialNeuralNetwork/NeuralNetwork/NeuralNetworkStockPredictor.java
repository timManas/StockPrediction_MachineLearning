package ArtificialNeuralNetwork.NeuralNetwork;

import Main.ProjectConfig;
import Main.ProjectHelper;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.SupervisedLearning;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class NeuralNetworkStockPredictor {

    //region Constants
    private int slidingWindowSize;
    private double max = 0;
    private double min = Double.MAX_VALUE;
    private ArrayList<Double> stockPriceList = new ArrayList<>();
    private ArrayList<Double> normalizedStockPriceList = new ArrayList<>();
    //endregion

    //region Constructor
    public NeuralNetworkStockPredictor(int slidingWindowSize) {
        this.slidingWindowSize = slidingWindowSize;
    }
    //endregion

    //region SubRoutines
    public void prepareData(String companySymbol) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader("input/TrainingData_" + companySymbol +".csv"));
        // Find the minimum and maximum values - needed for normalization
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                String date = tokens[0];
                double stockPrice;

                try{
                    stockPrice = Double.parseDouble(tokens[1]);
                }catch (Exception e){
                    stockPrice = 0;
                }

                System.out.println("Date: " + date + "     " + "Price: " + stockPrice);
                if (stockPrice > max) {
                    max = stockPrice;
                }
                if (stockPrice < min) {
                    min = stockPrice;
                }

                stockPriceList.add(stockPrice);
            }
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            reader.close();
        }

        stockPriceList = ProjectHelper.cleanMissingData(stockPriceList);
        for(double element : stockPriceList)
            System.out.println("New Price: " + element);

        reader = new BufferedReader(new FileReader("input/TrainingData_" + companySymbol+".csv"));

        // Use a buffered Writer to write files to to Leaning Data
        BufferedWriter writer = new BufferedWriter(new FileWriter(ProjectConfig.learningDataFilePath));

        // Keep a queue with slidingWindowSize + 1 values
        LinkedList<Double> valuesQueue = new LinkedList<Double>();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                double crtValue = Double.valueOf(line.split(",")[1]);
                // Normalize values and add it to the queue
                double normalizedValue = normalizeValue(crtValue);
                valuesQueue.add(normalizedValue);
                normalizedStockPriceList.add(normalizedValue);

                if (valuesQueue.size() == slidingWindowSize + 1) {
                    String valueLine = valuesQueue.toString().replaceAll("\\[|\\]", "");
                    writer.write(valueLine);
                    writer.newLine();
                    // Remove the first element in queue to make place for a new
                    // one
                    valuesQueue.removeFirst();
                }
            }
        } finally {
            reader.close();
            writer.close();
        }
    }

    public void trainNetwork() throws IOException {
        NeuralNetwork<BackPropagation> neuralNetwork = new MultiLayerPerceptron(slidingWindowSize,
                2 * slidingWindowSize + 1, 1);

        SupervisedLearning learningRule = neuralNetwork.getLearningRule();
        learningRule.setMaxError(ProjectConfig.MAX_ERROR);
        learningRule.setLearningRate(ProjectConfig.LEARNING_RATE);
        learningRule.setMaxIterations(ProjectConfig.MAX_ITERATIONS);
        learningRule.addListener(new LearningEventListener() {

            public void handleLearningEvent(LearningEvent learningEvent) {
                SupervisedLearning rule = (SupervisedLearning) learningEvent.getSource();
                System.out.println("Iteration: " + rule.getCurrentIteration() + "       " + "Network Error: " + rule.getTotalNetworkError());
            }
        });

        DataSet trainingSet = loadTraininigData(ProjectConfig.learningDataFilePath);
        neuralNetwork.learn(trainingSet);
        neuralNetwork.save(ProjectConfig.neuralNetworkModelFilePath);

    }

    public double testNetwork() {
        //Opens the Saved Neural Network after training
        NeuralNetwork neuralNetwork = NeuralNetwork.createFromFile(ProjectConfig.neuralNetworkModelFilePath);

        // These values represent the Input Data.
        System.out.println("#1 " +  stockPriceList.get(stockPriceList.size()-1) + "   " +
                "#2 " + stockPriceList.get(stockPriceList.size()-2) + "    " +
                "#3 " + stockPriceList.get(stockPriceList.size()-3) + "    " +
                "#4 " + stockPriceList.get(stockPriceList.size()-4) + "    " +
                "#5 " + stockPriceList.get(stockPriceList.size()-5));

        neuralNetwork.setInput(normalizeValue(stockPriceList.get(stockPriceList.size()-1)),
                normalizeValue(stockPriceList.get(stockPriceList.size()-2)),
                normalizeValue(stockPriceList.get(stockPriceList.size()-3)),
                normalizeValue(stockPriceList.get(stockPriceList.size()-4)),
                normalizeValue(stockPriceList.get(stockPriceList.size()-5)));


        neuralNetwork.calculate();
        double[] networkOutput = neuralNetwork.getOutput();
        double predictedValue = deNormalizeValue(networkOutput[0]);
        System.out.println("Expected value  : " + stockPriceList.get(stockPriceList.size()-1));
        System.out.println("Predicted value : " + predictedValue);

        return predictedValue;

    }

    //endregion

    //region Helper
    private double normalizeValue(double input) {
        return (input - min) / (max - min) * 0.8 + 0.1;
    }

    private double deNormalizeValue(double input) {
        return min + (input - 0.1) * (max - min) / 0.8;
    }

    private DataSet loadTraininigData(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        DataSet trainingSet = new DataSet(slidingWindowSize, 1);

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");

                double trainValues[] = new double[slidingWindowSize];
                for (int i = 0; i < slidingWindowSize; i++) {
                    trainValues[i] = Double.valueOf(tokens[i]);
                }
                double expectedValue[] = new double[] { Double.valueOf(tokens[slidingWindowSize]) };
                trainingSet.addRow(new DataSetRow(trainValues, expectedValue));
            }
        } finally {
            reader.close();
        }
        return trainingSet;
    }
    //endregion



}