package knoma.newsgroup;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.weld.environment.se.bindings.Parameters;
import org.jboss.weld.environment.se.events.ContainerInitialized;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import static java.util.stream.Collectors.toList;

/**
 * Created by gabriel on 01/11/15.
 */
@ApplicationScoped
public class ApplicationStarter {
    private static final Logger logger = LogManager.getLogger(ApplicationStarter.class.getName());

    @Inject
    private MessageReader messageReader;
    @Inject
    private GroupReader groupReader;
    @Inject
    private MessageDataCleaner cleaner;
    @Inject
    private MessageInstanceConverter instanceConverter;

    public void bootListener(@Observes ContainerInitialized event, @Parameters List<String> cmdLineArgs) throws Exception {
        logger.info("Reading messages from folder 20_newsgroups");

        List<Group> groups = groupReader.groups("20_newsgroups");

        logger.info("Founded {} groups", groups.size());

        List<Message> messages = groups.stream()
                .parallel()
                .flatMap(group -> messageReader.messages(group).stream())
                .filter(message -> message != null)
                .parallel()
                .map(message -> cleaner.clean(message))
                .collect(toList());

        Collections.shuffle(messages);

        BagOfWords bagOfWords = new BagOfWords(5000);

        logger.info("Founded {} messages.", messages.size());

        FastVector vector = new FastVector();

        FastVector categoryVector = new FastVector(20);
        groups.stream().forEach(group -> categoryVector.addElement(group.getName()));

        Attribute newsgroup = new Attribute("newsgroup-class", categoryVector);
        vector.addElement(newsgroup);

        logger.info("Extracting vocabulary...");
        messages.stream()
                .flatMap(message -> ((TokenizedMessage) message).getTokens().stream())
                .forEach(word -> bagOfWords.count(word));

        logger.info("Founded {} words for vocabulary.", bagOfWords.getVocabulary().size());

        final FastVector v = new FastVector(2);
        v.addElement("true");
        v.addElement("false");

        bagOfWords
                .getVocabulary()
                .stream()
                .skip(100)
                .map(word -> new Attribute(word, v))
                .forEach(attribute -> vector.addElement(attribute));

        int trainingSetSize = (int) (messages.size() * 0.7);
        int testingSetSize = messages.size() - trainingSetSize;

        logger.info("The dataset will be splitted in {} trainning instances and {} test instances", trainingSetSize, testingSetSize);

        Instances trainingInstances = new Instances("Groups", vector, trainingSetSize);
        trainingInstances.setClassIndex(0);

        logger.info("Extracting training instances...");

        IntStream.range(0, trainingSetSize).parallel().forEach(i -> {
            trainingInstances.add(instanceConverter.convert(messages.get(i), vector));
        });

        logger.info("Starting naive bayes classifier training....");

        NaiveBayes classifier = new NaiveBayes();
        classifier.buildClassifier(trainingInstances);

        Evaluation eval = new Evaluation(trainingInstances);

        Instances testingInstances = new Instances("Groups", vector, testingSetSize);
        testingInstances.setClassIndex(0);

        logger.info("Extracting testing instances...");

        IntStream.range(trainingSetSize, messages.size()).parallel().forEach(i -> {
            testingInstances.add(new MessageInstanceConverter().convert(messages.get(i), vector));
        });

        logger.info("Starting classifier evaluation....");

        eval.evaluateModel(classifier, testingInstances);

        logger.info(eval.toSummaryString("\nResults\n======\n", false));

    }
}
