package knoma.newsgroup.classifiers;

import knoma.newsgroup.domain.BagOfWords;
import knoma.newsgroup.domain.NewsgroupScenario;
import knoma.newsgroup.domain.TokenizedMessage;
import knoma.newsgroup.preprocessing.MessageInstanceConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.ThresholdCurve;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.RBFKernel;
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
 * Created by gabriel on 29/11/15.
 */
@Classifier("smo")
@ApplicationScoped
public class SMOClassifierBuilder implements ClassifierBuilder  {
    public weka.classifiers.Classifier build(Instances instances) throws Exception {
        SMO classifier = new SMO();
        classifier.setKernel(new RBFKernel());
        classifier.buildClassifier(instances);
        return classifier;
    }
}
