package SentimentAnalisys.ModelTrainManager;

import Main.ProjectConfig;
import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.classify.DynamicLMClassifier;
import com.aliasi.classify.LMClassifier;
import com.aliasi.corpus.ObjectHandler;
import com.aliasi.util.AbstractExternalizable;
import com.aliasi.util.Compilable;
import com.aliasi.util.Files;

import java.io.File;
import java.io.IOException;

/**
 * Created by timmanas on 2017-02-09.
 */
public class ModelTrainer {

    private File trainDir;
    private String[] categoryList;
    private LMClassifier lmClassifier;

    private int nGram = 7; //Set this between 7 - 12

    public ModelTrainer() {
        trainDir = new File(ProjectConfig.trainDirectory);
        categoryList = trainDir.list();
        lmClassifier = DynamicLMClassifier.createNGramProcess(categoryList, nGram);
    }

    public void train() throws IOException, ClassNotFoundException {

        for (String category : categoryList) {

            if(category.equalsIgnoreCase(".DS_Store") ||
                    category.equalsIgnoreCase("classifier.txt"))
                continue;

            Classification classification = new Classification(category);
            File file = new File(trainDir, category);
            File[] trainFilesList = file.listFiles();

            for (File trainFile : trainFilesList) {
                String review = Files.readFromFile(trainFile, ProjectConfig.charset);
                Classified classified = new Classified(review, classification);
                ((ObjectHandler) lmClassifier).handle(classified);
            }
        }

        AbstractExternalizable.compileTo((Compilable) lmClassifier, new File(ProjectConfig.classifierPath));
    }
}
