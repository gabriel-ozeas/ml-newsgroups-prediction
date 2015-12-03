package knoma.newsgroup.experiments;

import knoma.newsgroup.classifiers.ClassifierBuilder;
import knoma.newsgroup.domain.BagOfWords;
import knoma.newsgroup.domain.ExecutionContext;
import knoma.newsgroup.domain.NewsgroupScenario;
import knoma.newsgroup.preprocessing.AttributeVectorExtractor;
import knoma.newsgroup.preprocessing.ExecutionContextBuilder;
import knoma.newsgroup.preprocessing.MessageInstanceConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.FastVector;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static knoma.newsgroup.classifiers.ClassifierLiteral.classifierType;
import static knoma.newsgroup.domain.BagOfWordsBuilder.newBagOfWords;

/**
 * Created by gabriel on 29/11/15.
 */
@Experiment(name = "find-best-number-of-attributes",
        description = "This experiment evaluate classifiers with diferent attribute number trainning. " +
                "It uses Naive Bayes and Naive Bayes Multinomial to build the classifiers. " +
                "By default experiment will run with 100, 1000, 2500, 5000, 20000, 40000 words.")
public class FindBestNumberOfAttributesExperiment implements RunnableExperiment {
    private static final Logger logger = LogManager.getLogger(FindBestNumberOfAttributesExperiment.class.getName());

    @Inject
    @Any
    private Instance<ClassifierBuilder> classifierBuilder;

    @Inject
    private Instance<NewsgroupScenario> scenarioInstance;

    @Inject
    private AttributeVectorExtractor attributeVectorExtractor;

    @Inject
    private MessageInstanceConverter instanceConverter;

    @Override
    public void run(Map<String, String> configuration) {
        NewsgroupScenario scenario = scenarioInstance.get();

        float trainningSize = configuration.containsKey("tranning-size") ? Integer.parseInt(configuration.get("tranning-size")) : 0.7f;

        IntStream.of(100, 1000, 2500, 5000, 20000, 40000)
                .forEach(i -> tranningWithWordsSize("naive-bayes", scenario, trainningSize, i));

        IntStream.of(100, 1000, 2500, 5000, 20000, 40000)
                .forEach(i -> tranningWithWordsSize("naive-bayes-multinomial", scenario, trainningSize, i));
    }

    private void tranningWithWordsSize(String classifierName, NewsgroupScenario scenario, float trainningSize, int i) {
        logger.info("Extracting bag of words with {} words.", i);
        BagOfWords bagOfWords = newBagOfWords()
                .messages(scenario.getMessages())
                .size(i).build();

        FastVector attributes = attributeVectorExtractor.extract(bagOfWords, scenario.getGroups());

        logger.info("Converting messages to weka instances...");
        List<weka.core.Instance> instances = instanceConverter.convert(scenario.getMessages(), bagOfWords, attributes);

        ExecutionContext executionContext = new ExecutionContextBuilder()
                .attributes(attributes)
                .instances(instances)
                .trainingSize(trainningSize)
                .build();

        logger.info("Creating classifer with {} attributes.", i);

        try {
            ClassifierBuilder builder = classifierBuilder
                    .select(classifierType(classifierName))
                    .get();

            logger.info("Building classifier...");
            Classifier classifier = builder.build(executionContext.getTraningInstances());

            Evaluation evaluation = new Evaluation(executionContext.getTraningInstances());
            logger.info("Evaluating classifier...");
            evaluation.evaluateModel(classifier, executionContext.getTestingInstances());

            logger.info(evaluation.toSummaryString("\nResults\n======\n", false));

        } catch (Exception e) {
            throw new RuntimeException("Cannot construct classifier. ", e);
        }
    }
}
