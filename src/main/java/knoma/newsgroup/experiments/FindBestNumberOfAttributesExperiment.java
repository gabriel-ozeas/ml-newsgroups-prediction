package knoma.newsgroup.experiments;

import knoma.newsgroup.classifiers.ClassifierBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.stream.IntStream;

import static knoma.newsgroup.classifiers.ClassifierLiteral.classifierType;

/**
 * Created by gabriel on 29/11/15.
 */
@Experiment("find-best-number-of-attributes")
public class FindBestNumberOfAttributesExperiment implements RunnableExperiment {
    private static final Logger logger = LogManager.getLogger(FindBestNumberOfAttributesExperiment.class.getName());

    @Inject
    @Any
    private Instance<ClassifierBuilder> classifierBuilder;

    @Override
    public void run() {
        IntStream.of(100, 1000, 2500, 5000, 20000, 40000)
                .forEach(i -> {
                    logger.info("Creating classifer with {} attributes.", i);

                    ClassifierBuilder builder = classifierBuilder
                            .select(classifierType("naive-bayes-multinomial"))
                            .get();
                    try {
                        builder.build(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        IntStream.of(100, 1000, 2500, 5000, 20000, 40000)
                .forEach(i -> {
                    logger.info("Creating classifer with {} attributes.", i);

                    ClassifierBuilder builder = classifierBuilder
                            .select(classifierType("naive-bayes"))
                            .get();
                    try {
                        builder.build(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
