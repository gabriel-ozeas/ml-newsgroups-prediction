package knoma.newsgroup.classifiers;

import weka.classifiers.trees.J48;

import weka.core.Instances;


/**
 * Created by gabriel on 29/11/15.
 */
@Classifier("j48")
public class J48ClassifierBuilder implements ClassifierBuilder  {
    public weka.classifiers.Classifier build(Instances instances) throws Exception {
        J48 classifier = new J48();
        classifier.buildClassifier(instances);
        return classifier;
    }
}
