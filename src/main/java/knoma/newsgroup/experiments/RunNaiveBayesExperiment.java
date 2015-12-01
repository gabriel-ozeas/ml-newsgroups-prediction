package knoma.newsgroup.experiments;

import knoma.newsgroup.classifiers.ClassifierBuilder;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import static knoma.newsgroup.classifiers.ClassifierLiteral.classifierType;

/**
 * Created by gabriel on 29/11/15.
 */
@Experiment("run-naive-bayes")
public class RunNaiveBayesExperiment implements RunnableExperiment {
    @Inject
    @Any
    private Instance<ClassifierBuilder> classifierBuilder;

    @Override
    public void run() {
        ClassifierBuilder builder = classifierBuilder
                .select(classifierType("naive-bayes"))
                .get();
        try {
            builder.buildAndEvaluate(20000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}