package knoma.newsgroup.classifiers;

import weka.classifiers.lazy.IBk;
import weka.core.Instances;

import javax.enterprise.context.ApplicationScoped;

/**
 * Created by gabriel on 29/11/15.
 */
@Classifier("knn")
@ApplicationScoped
public class KNNClassifierBuilder implements ClassifierBuilder  {
    public weka.classifiers.Classifier build(Instances instances) throws Exception {
        IBk classifier = new IBk();
        classifier.buildClassifier(instances);
        classifier.setCrossValidate(true);
        classifier.setMeanSquared(true);
        return classifier;
    }
}
