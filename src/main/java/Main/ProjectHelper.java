package Main;

import java.util.ArrayList;

/**
 * Created by timmanas on 2017-04-01.
 */
public class ProjectHelper {

    public static ArrayList<Double> cleanMissingData(ArrayList<Double> stockPriceList){

        ArrayList<Double> cleanDataArrayList = new ArrayList<>();

        for(int index = 0; index < stockPriceList.size(); index++){
            double currentStockPrice = stockPriceList.get(index);
            if(currentStockPrice != 0){
                cleanDataArrayList.add(currentStockPrice);
                continue;
            }

            if(index == 0){
                currentStockPrice = findNextNonZeroStockPrice(stockPriceList, index);
            }else if(index == stockPriceList.size() - 1){
                currentStockPrice = usePreviousStockData(stockPriceList);
            }else {
                currentStockPrice = takeAverageOfStockPrices(stockPriceList, index);
            }

            cleanDataArrayList.add(currentStockPrice);
        }

        return cleanDataArrayList;
    }

    private static double findNextNonZeroStockPrice(ArrayList<Double> stockPriceList, int index) {

        for(int i = index ; i < stockPriceList.size() - 1; i++){

            double price = stockPriceList.get(i);
            if(price != 0)
                return price;
        }
        return 0;
    }

    private static double usePreviousStockData(ArrayList<Double> stockPriceList) {
        return stockPriceList.get(stockPriceList.size() - 2);
    }

    private static double takeAverageOfStockPrices(ArrayList<Double> stockPriceList, int index) {

        double previousData = stockPriceList.get(index - 1);
        double nextNonZeroData = findNextNonZeroStockPrice(stockPriceList, index);
        double average = (previousData + nextNonZeroData) / 2;

        return average;

    }

}
