package knoma.newsgroup.classifiers;


import weka.classifiers.bayes.NaiveBayesMultinomial;

import weka.core.Instances;
import weka.gui.visualize.PlotData2D;
import weka.gui.visualize.ThresholdVisualizePanel;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.awt.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static weka.core.Utils.doubleToString;

/**
 * Created by gabriel on 25/11/15.
 */
@Classifier("naive-bayes-multinomial")
@ApplicationScoped
public class NaiveBayesMultinomialClassifierBuilder implements ClassifierBuilder {
    public weka.classifiers.Classifier build(Instances instances) throws Exception {
        NaiveBayesMultinomial classifier = new NaiveBayesMultinomial();
        classifier.buildClassifier(instances);
        return classifier;
    }
}
