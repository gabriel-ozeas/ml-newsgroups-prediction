package knoma.newsgroup.classifiers;

import weka.classifiers.*;
import weka.core.Instances;

/**
 * Created by gabriel on 28/11/15.
 */
public interface ClassifierBuilder {
    public weka.classifiers.Classifier build(Instances instances) throws Exception;
}
