package knoma.newsgroup.experiments;

import knoma.newsgroup.domain.BagOfWords;
import knoma.newsgroup.domain.ExecutionContext;
import knoma.newsgroup.domain.NewsgroupScenario;
import knoma.newsgroup.preprocessing.AttributeVectorExtractor;
import knoma.newsgroup.preprocessing.ExecutionContextBuilder;
import knoma.newsgroup.preprocessing.MessageInstanceConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import weka.core.FastVector;
import weka.core.Instances;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import static java.lang.System.getProperty;
import static java.util.stream.Collectors.toList;
import static knoma.newsgroup.domain.BagOfWordsBuilder.newBagOfWords;

/**
 * Created by gabriel on 04/12/15.
 */
@Experiment(name = "arff", description = "This experiment export the ARFF training and testing files ")
public class RunARFFExtratorExperiment implements RunnableExperiment {
    private static final Logger logger = LogManager.getLogger(RunARFFExtratorExperiment.class.getName());

    private static final String ARFF_TRAINNING_FILE = "20newsgroups-trainning.arff";
    private static final String ARFF_TESTING_FILE = "20newsgroups-testing.arff";

    public static final int DEFAULT_NUMBER_OF_WORDS = 20000;

    @Inject
    private Instance<NewsgroupScenario> scenarioInstance;

    @Inject
    private AttributeVectorExtractor attributeVectorExtractor;

    @Inject
    private MessageInstanceConverter instanceConverter;

    @Override
    public void run(Map<String, String> configuration) {
        ExecutionContext context = preprocess(configuration);
        writeToFile(context.getTraningInstances(), ARFF_TRAINNING_FILE);
        writeToFile(context.getTestingInstances(), ARFF_TESTING_FILE);
    }

    private void writeToFile(Instances instances, String fileName) {
        File arffFile = new File(getProperty("java.io.tmpdir") + File.separator + fileName);
        logger.info("Writing instances to " + arffFile.getAbsolutePath());

        if (arffFile.exists()) {
            arffFile.delete();
        }

        try (PrintStream out = new PrintStream(new FileOutputStream(arffFile))) {
            out.print(instances.toString());
        } catch(IOException e) {
            throw new RuntimeException("Cannot write trainning ARFF file to " + arffFile.getAbsolutePath());
        }
    }

    protected ExecutionContext preprocess(Map<String, String> configuration) {
        NewsgroupScenario scenario = scenarioInstance.get();

        int numberOfAttributes = configuration.containsKey("number-of-words") ? Integer.parseInt(configuration.get("number-of-words")) : DEFAULT_NUMBER_OF_WORDS;
        float trainningSize = configuration.containsKey("tranning-size") ? Integer.parseInt(configuration.get("tranning-size")) : 0.7f;

        logger.info("Extracting bag of words with {} words.", numberOfAttributes);
        BagOfWords bagOfWords = newBagOfWords()
                .messages(scenario.getMessages())
                .size(numberOfAttributes).build();

        logger.info("Vocabulary: {}...", bagOfWords.getVocabulary().stream().limit(30).collect(toList()).toString());

        FastVector categories = attributeVectorExtractor.extractCategoryAttribute(scenario.getGroups());
        FastVector attributes = attributeVectorExtractor.extract(bagOfWords, categories);

        logger.info("Converting messages to weka instances...");
        List<weka.core.Instance> instances = instanceConverter.convert(scenario.getMessages(), bagOfWords, categories);

        return new ExecutionContextBuilder()
                .attributes(attributes)
                .instances(instances)
                .trainingSize(trainningSize)
                .build();
    }
}
