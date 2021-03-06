package knoma.newsgroup.experiments;

/**
 * Created by gabriel on 29/11/15.
 */
@Experiment(name = "naive-bayes", description = "This experiment create a classifier using naive-bayes algorithm. You can use the parameters -number-of-words to specify how many words " +
        "will be used in the vocabulary. -tranning-size specifies the percentage of instances that will be used in tranning.")
public class RunNaiveBayesExperiment extends DefaultClassifierExperiment  implements RunnableExperiment {
    @Override
    protected String getClassifierName() {
        return "naive-bayes";
    }
}
