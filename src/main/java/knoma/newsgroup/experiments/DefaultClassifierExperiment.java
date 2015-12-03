package knoma.newsgroup.experiments;

import knoma.newsgroup.classifiers.ClassifierBuilder;
import knoma.newsgroup.domain.BagOfWords;
import knoma.newsgroup.domain.ExecutionContext;
import knoma.newsgroup.domain.NewsgroupScenario;
import knoma.newsgroup.gui.GraphEvaluationDisplay;
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

import static knoma.newsgroup.classifiers.ClassifierLiteral.classifierType;
import static knoma.newsgroup.domain.BagOfWordsBuilder.newBagOfWords;

/**
 * Created by gabriel on 12/3/15.
 */
public abstract class DefaultClassifierExperiment implements RunnableExperiment {
    private static final Logger logger = LogManager.getLogger(DefaultClassifierExperiment.class.getName());

    public static final int DEFAULT_NUMBER_OF_WORDS = 20000;

    @Inject
    @Any
    private Instance<ClassifierBuilder> classifierBuilder;

    @Inject
    private Instance<NewsgroupScenario> scenarioInstance;

    @Inject
    private AttributeVectorExtractor attributeVectorExtractor;

    @Inject
    private MessageInstanceConverter instanceConverter;

    @Inject
    private GraphEvaluationDisplay evaluationDisplay;

    @Override
    public void run(Map<String, String> configuration) {
        try {
            ExecutionContext context = preprocess(configuration);
            Classifier classifier = tranning(context);
            evaluate(context, classifier);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Classifier tranning(ExecutionContext context) throws Exception {
        ClassifierBuilder builder = classifierBuilder
                .select(classifierType("adaboost-with-nb-multinomial"))
                .get();

        logger.info("Building classifier...");
        return builder.build(context.getTraningInstances());
    }

    protected void evaluate(ExecutionContext context, Classifier classifier) throws Exception {
        Evaluation evaluation = new Evaluation(context.getTraningInstances());
        logger.info("Evaluating classifier...");
        evaluation.evaluateModel(classifier, context.getTestingInstances());

        logger.info(evaluation.toSummaryString("\nResults\n======\n", false));
        logger.info(evaluation.toMatrixString());

        evaluationDisplay.show(evaluation);
    }

    protected ExecutionContext preprocess(Map<String, String> configuration) {
        NewsgroupScenario scenario = scenarioInstance.get();

        int numberOfAttributes = configuration.containsKey("number-of-words") ? Integer.parseInt(configuration.get("number-of-words")) : DEFAULT_NUMBER_OF_WORDS;
        float trainningSize = configuration.containsKey("tranning-size") ? Integer.parseInt(configuration.get("tranning-size")) : 0.7f;

        logger.info("Extracting bag of words with {} words.", numberOfAttributes);
        BagOfWords bagOfWords = newBagOfWords()
                .messages(scenario.getMessages())
                .size(numberOfAttributes).build();

        FastVector attributes = attributeVectorExtractor.extract(bagOfWords, scenario.getGroups());

        logger.info("Converting messages to weka instances...");
        List<weka.core.Instance> instances = instanceConverter.convert(scenario.getMessages(), bagOfWords, attributes);

        return new ExecutionContextBuilder()
                .attributes(attributes)
                .instances(instances)
                .trainingSize(trainningSize)
                .build();
    }

    protected abstract String getClassifierName();
}
