package knoma.newsgroup.classifiers;

import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.core.Instances;

import javax.enterprise.context.ApplicationScoped;

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
