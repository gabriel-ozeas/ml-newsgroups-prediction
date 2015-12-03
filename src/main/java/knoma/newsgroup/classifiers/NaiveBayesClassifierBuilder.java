package knoma.newsgroup.classifiers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

import javax.enterprise.context.ApplicationScoped;

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
