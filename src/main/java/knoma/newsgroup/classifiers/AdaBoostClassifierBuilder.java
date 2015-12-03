package knoma.newsgroup.classifiers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.meta.AdaBoostM1;
import weka.core.Instances;

import javax.enterprise.context.ApplicationScoped;

/**
 * Created by gabriel on 29/11/15.
 */
@Classifier("adaboost-with-nb-multinomial")
@ApplicationScoped
public class AdaBoostClassifierBuilder implements ClassifierBuilder  {
    private static final Logger logger = LogManager.getLogger(AdaBoostClassifierBuilder.class.getName());

    public weka.classifiers.Classifier build(Instances instances) throws Exception {
        AdaBoostM1 classifier = new AdaBoostM1();
        classifier.setUseResampling(true);
        classifier.setClassifier(new NaiveBayesMultinomial());
        classifier.setNumIterations(20);
        classifier.buildClassifier(instances);

        return classifier;
    }
}
