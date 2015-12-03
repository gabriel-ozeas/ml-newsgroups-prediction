package knoma.newsgroup.classifiers;


import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.core.Instances;

import javax.enterprise.context.ApplicationScoped;

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
