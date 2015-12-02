package knoma.newsgroup.experiments;

import knoma.newsgroup.classifiers.ClassifierBuilder;
import knoma.newsgroup.domain.BagOfWords;
import knoma.newsgroup.domain.ExecutionContext;
import knoma.newsgroup.domain.NewsgroupScenario;
import knoma.newsgroup.gui.GraphEvaluationDisplay;
import knoma.newsgroup.preprocessing.AttributeVectorExtractor;
import knoma.newsgroup.preprocessing.ExecutionContextBuilder;
import knoma.newsgroup.preprocessing.MessageInstanceConverter;
import weka.classifiers.Evaluation;
import weka.core.FastVector;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import java.util.List;
import java.util.Map;

import static knoma.newsgroup.classifiers.ClassifierLiteral.classifierType;
import static knoma.newsgroup.domain.BagOfWords.wordsFromMessages;

/**
 * Created by gabriel on 29/11/15.
 */
@Experiment("adaboost-naivebayes")
public class RunAdaBoostExperiment implements RunnableExperiment {
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
        NewsgroupScenario scenario = scenarioInstance.get();

        int numberOfAttributes = configuration.containsKey("number-of-words") ? Integer.parseInt(configuration.get("number-of-words")) : DEFAULT_NUMBER_OF_WORDS;
        float trainningSize = configuration.containsKey("tranning-size") ? Integer.parseInt(configuration.get("tranning-size")) : 0.7f;

        BagOfWords bagOfWords = wordsFromMessages(scenario.getMessages(), numberOfAttributes);

        FastVector attributes = attributeVectorExtractor.extract(bagOfWords, scenario.getGroups());

        List<weka.core.Instance> instances = instanceConverter.convert(scenario.getMessages(), bagOfWords, attributes);

        ExecutionContext context = new ExecutionContextBuilder()
                .attributes(attributes)
                .instances(instances)
                .trainingSize(trainningSize)
                .build();

        ClassifierBuilder builder = classifierBuilder
                .select(classifierType("adaboost"))
                .get();

        try {
            weka.classifiers.Classifier classifier = builder.build(context.getTraningInstances());

            Evaluation evaluation = new Evaluation(context.getTraningInstances());
            evaluation.evaluateModel(classifier, context.getTestingInstances());

            evaluationDisplay.show(evaluation);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
