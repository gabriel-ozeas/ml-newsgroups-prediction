package knoma.newsgroup.classifiers;

import knoma.newsgroup.BagOfWords;
import knoma.newsgroup.domain.NewsgroupScenario;
import knoma.newsgroup.domain.TokenizedMessage;
import knoma.newsgroup.preprocessing.MessageInstanceConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.evaluation.ThresholdCurve;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.Utils;
import weka.gui.visualize.PlotData2D;
import weka.gui.visualize.ThresholdVisualizePanel;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.awt.*;
import java.util.stream.IntStream;

/**
 * Created by gabriel on 25/11/15.
 */
@Classifier("naive-bayes-multinomial")
public class NaiveBayesMultinomialClassifierBuilder implements ClassifierBuilder {
    private static final Logger logger = LogManager.getLogger(NaiveBayesMultinomialClassifierBuilder.class.getName());

    @Inject
    private Instance<NewsgroupScenario> scenarioInstance;

    @Inject
    private MessageInstanceConverter instanceConverter;

    public void buildAndEvaluate(int numberOfWords) throws Exception {
        NewsgroupScenario scenario = scenarioInstance.get();

        BagOfWords bagOfWords = new BagOfWords(numberOfWords);

        logger.info("Founded {} messages.", scenario.getMessages().size());

        FastVector vector = new FastVector();

        FastVector categoryVector = new FastVector(20);
        scenario.getGroups().stream().forEach(group -> categoryVector.addElement(group.getName()));

        Attribute newsgroup = new Attribute("@@class@@", categoryVector);
        vector.addElement(newsgroup);

        logger.info("Extracting vocabulary...");
        scenario.getMessages().stream()
                .flatMap(message -> ((TokenizedMessage) message).getTokens().stream())
                .forEach(word -> bagOfWords.count(word));

        logger.info("Founded {} unique words for vocabulary. Limiting to {}", bagOfWords.sizeOfAll(), bagOfWords.limit());

        bagOfWords
                .getVocabulary()
                .stream()
                .map(word -> new Attribute(word))
                .forEach(attribute -> vector.addElement(attribute));

        logger.info("Vocabolary: " + bagOfWords.getVocabulary().toString());

        int trainingSetSize = (int) (scenario.getMessages().size() * 0.3);
        int testingSetSize = scenario.getMessages().size() - trainingSetSize;

        logger.info("The dataset will be splitted in {} trainning instances and {} test instances", trainingSetSize, testingSetSize);

        Instances trainingInstances = new Instances("Groups", vector, trainingSetSize);
        trainingInstances.setClassIndex(0);

        logger.info("Extracting training instances...");

        IntStream.range(0, trainingSetSize).parallel().forEach(i -> {
            trainingInstances.add(instanceConverter.convert(scenario.getMessages().get(i), bagOfWords, categoryVector));
        });

        Instances testingInstances = new Instances("@@class@@", vector, testingSetSize);

        logger.info("Extracting testing instances...");
        testingInstances.setClassIndex(0);
        IntStream.range(trainingSetSize, scenario.getMessages().size()).parallel().forEach(i -> {
            testingInstances.add(new MessageInstanceConverter().convert(scenario.getMessages().get(i), bagOfWords, categoryVector));
        });

        NaiveBayesMultinomial classifier = new NaiveBayesMultinomial();
        classifier.buildClassifier(trainingInstances);

        logger.info("Starting " + classifier.getClass().getSimpleName() + " classifier training....");

        classifier.buildClassifier(trainingInstances);

        logger.info("Starting " + classifier.getClass().getSimpleName() + " classifier evaluation....");

        Evaluation evaluation = new Evaluation(trainingInstances);
        //evaluation.crossValidateModel(classifier, trainingInstances, 10, new Random(1));
        evaluation.evaluateModel(classifier, testingInstances);
        evaluation.predictions();

        graph(evaluation);

        logger.info(evaluation.toSummaryString("\nResults\n======\n", false));
        logger.info(evaluation.toMatrixString());
    }

    private void graph(Evaluation eval) throws Exception {
        // generate curve
        ThresholdCurve tc = new ThresholdCurve();
        int classIndex = 0;
        Instances result = tc.getCurve(eval.predictions(), classIndex);

        // plot curve
        ThresholdVisualizePanel vmc = new ThresholdVisualizePanel();
        vmc.setROCString("(Area under ROC = " +
                Utils.doubleToString(tc.getROCArea(result), 4) + ")");
        vmc.setName(result.relationName());
        PlotData2D tempd = new PlotData2D(result);
        tempd.setPlotName(result.relationName());
        tempd.addInstanceNumberAttribute();
        // specify which points are connected
        boolean[] cp = new boolean[result.numInstances()];
        for (int n = 1; n < cp.length; n++)
            cp[n] = true;
        tempd.setConnectPoints(cp);
        // add plot
        vmc.addPlot(tempd);

        // display curve
        String plotName = vmc.getName();
        final javax.swing.JFrame jf = new javax.swing.JFrame("Weka Classifier Visualize: "+plotName);
        jf.setSize(500,400);
        jf.getContentPane().setLayout(new BorderLayout());
        jf.getContentPane().add(vmc, BorderLayout.CENTER);
        jf.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                jf.dispose();
            }
        });
        jf.setVisible(true);
    }
}
