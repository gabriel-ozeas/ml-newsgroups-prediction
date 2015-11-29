package knoma.newsgroup.classifiers;

import knoma.newsgroup.BagOfWords;
import knoma.newsgroup.preprocessing.MessageInstanceConverter;
import knoma.newsgroup.domain.NewsgroupScenario;
import knoma.newsgroup.domain.TokenizedMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
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

import static weka.core.Utils.doubleToString;

/**
 * Created by gabriel on 25/11/15.
 */
@Classifier("naive-bayes")
public class NaiveBayesClassifierBuilder implements ClassifierBuilder {
    private static final Logger logger = LogManager.getLogger(NaiveBayesClassifierBuilder.class.getName());

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

        int trainingSetSize = (int) (scenario.getMessages().size() * 0.7);
        int testingSetSize = scenario.getMessages().size() - trainingSetSize;

        logger.info("The dataset will be splitted in {} trainning instances and {} test instances", trainingSetSize, testingSetSize);

        Instances trainingInstances = new Instances("Groups", vector, trainingSetSize);
        trainingInstances.setClassIndex(0);

        logger.info("Extracting training instances...");
        IntStream.range(0, trainingSetSize)
                .mapToObj(i -> scenario.getMessages().get(i))
                .map(message -> instanceConverter.convert(message, bagOfWords, categoryVector))
                .filter(instance -> instance != null)
                .forEach(instance -> trainingInstances.add(instance));

        Instances testingInstances = new Instances("@@class@@", vector, testingSetSize);
        testingInstances.setClassIndex(0);

        logger.info("Extracting testing instances...");
        IntStream.range(trainingSetSize, scenario.getMessages().size())
                .mapToObj(i -> scenario.getMessages().get(i))
                .map(message -> instanceConverter.convert(message, bagOfWords, categoryVector))
                .filter(instance -> instance != null)
                .forEach(instance -> testingInstances.add(instance));

        NaiveBayes naiveBayesClassifier = new NaiveBayes();
        naiveBayesClassifier.setUseSupervisedDiscretization(true);
        naiveBayesClassifier.buildClassifier(trainingInstances);

        logger.info("Starting " + naiveBayesClassifier.getClass().getSimpleName() + " classifier training....");

        naiveBayesClassifier.buildClassifier(trainingInstances);

        logger.info("Starting " + naiveBayesClassifier.getClass().getSimpleName() + " classifier evaluation....");

        Evaluation evaluation = new Evaluation(trainingInstances);
        //evaluation.crossValidateModel(naiveBayesClassifier, trainingInstances, 10, new Random(1));
        evaluation.evaluateModel(naiveBayesClassifier, testingInstances);

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
        vmc.setROCString("(Area under ROC = " + doubleToString(tc.getROCArea(result), 4) + ")");
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
