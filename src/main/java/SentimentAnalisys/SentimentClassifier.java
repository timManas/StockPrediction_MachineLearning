package SentimentAnalisys;

import Main.ProjectConfig;
import com.aliasi.classify.*;
import com.aliasi.util.AbstractExternalizable;
import java.io.File;
import java.io.IOException;

public class SentimentClassifier {

    LMClassifier lmClassifier;
    ConditionalClassification conditionalClassification;
    String[] categories;

    public SentimentClassifier() {
        try {
            lmClassifier = (LMClassifier) AbstractExternalizable.readObject(new File(ProjectConfig.classifierPath));
            categories = lmClassifier.categories();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    public String classify(String text) {
        conditionalClassification = lmClassifier.classify(text);
        return conditionalClassification.bestCategory();
    }

}
