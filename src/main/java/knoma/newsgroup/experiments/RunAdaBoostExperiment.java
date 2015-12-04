package knoma.newsgroup.experiments;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by gabriel on 29/11/15.
 */
@Experiment(name = "adaboost-multinomial",
        description = "This experiment create a classifier using AdaBoost algorithm " +
                "with Naive Bayes Multinomial. You can use the parameters -number-of-words to specify how many words " +
                "will be used in the vocabulary. -tranning-size specifies the percentage of instances that will be used in tranning.")
public class RunAdaBoostExperiment extends DefaultClassifierExperiment implements RunnableExperiment {
    private static final Logger logger = LogManager.getLogger(RunAdaBoostExperiment.class.getName());

    @Override
    protected String getClassifierName() {
        return "adaboost-with-nb-multinomial";
    }
}
