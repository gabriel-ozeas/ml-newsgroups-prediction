package knoma.newsgroup.experiments;

import knoma.newsgroup.domain.ExecutionContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import weka.classifiers.Classifier;
import weka.classifiers.lazy.IBk;

/**
 * Created by gabriel on 29/11/15.
 */
@Experiment(name = "knn", description = "This experiment create a classifier using knn algorithm. You can use the parameters -number-of-words to specify how many words " +
        "will be used in the vocabulary. -tranning-size specifies the percentage of instances that will be used in tranning.")
public class RunKNNExperiment extends DefaultClassifierExperiment implements RunnableExperiment {
    private static final Logger logger = LogManager.getLogger(RunKNNExperiment.class.getName());

    protected Classifier tranning(ExecutionContext context) throws Exception {
        IBk classifier = (IBk) super.tranning(context);
        logger.info("K number founded by weka: {}", classifier.getKNN());
        return classifier;
    }
    @Override
    protected String getClassifierName() {
        return "knn";
    }
}
