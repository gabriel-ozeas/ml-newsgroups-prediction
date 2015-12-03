package knoma.newsgroup.classifiers;

import knoma.newsgroup.domain.BagOfWords;
import knoma.newsgroup.preprocessing.MessageInstanceConverter;
import knoma.newsgroup.domain.NewsgroupScenario;
import knoma.newsgroup.domain.TokenizedMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.ThresholdCurve;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;
import weka.gui.visualize.PlotData2D;
import weka.gui.visualize.ThresholdVisualizePanel;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.awt.*;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static weka.core.Utils.doubleToString;

/**
 * Created by gabriel on 25/11/15.
 */
@Classifier("naive-bayes")
@ApplicationScoped
public class NaiveBayesClassifierBuilder implements ClassifierBuilder {
    private static final Logger logger = LogManager.getLogger(NaiveBayesClassifierBuilder.class.getName());

    public weka.classifiers.Classifier build(Instances instances) throws Exception {
        NaiveBayes naiveBayesClassifier = new NaiveBayes();
        naiveBayesClassifier.setUseSupervisedDiscretization(true);
        naiveBayesClassifier.buildClassifier(instances);
        return naiveBayesClassifier;
    }
}
