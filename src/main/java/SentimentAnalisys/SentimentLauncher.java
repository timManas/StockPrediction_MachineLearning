package SentimentAnalisys;

import Main.Main;
import SentimentAnalisys.ModelTrainManager.ModelTrainer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by timmanas on 2017-02-09.
 */
public class SentimentLauncher {

    public static void train(){
        try {
            ModelTrainer modelTrainer = new ModelTrainer();
            modelTrainer.train();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
