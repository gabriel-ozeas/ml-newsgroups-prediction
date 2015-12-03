package knoma.newsgroup.preprocessing;

import knoma.newsgroup.domain.ExecutionContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by gabriel on 12/2/15.
 */
public class ExecutionContextBuilder {
    private static final Logger logger = LogManager.getLogger(ExecutionContextBuilder.class);

    private float trainingSize = 0.7f;

    private FastVector attributes;
    private List<Instance> instances;

    public ExecutionContextBuilder trainingSize(float trainingSize) {
        this.trainingSize = trainingSize;
        return this;
    }

    public ExecutionContextBuilder attributes(FastVector attributes) {
        this.attributes = attributes;
        return this;
    }

    public ExecutionContextBuilder instances(List<Instance> instances) {
        this.instances = instances;
        return this;
    }

    public ExecutionContext build() {
        int trainingSetSize = (int) (instances.size() * 0.7);
        int testingSetSize = instances.size() - trainingSetSize;

        logger.info("The dataset will be splitted in {} trainning instances and {} test instances", trainingSetSize, testingSetSize);

        logger.info("Extracting training instances...");
        Instances trainingInstances = new Instances("@@class@@", attributes, trainingSetSize);
        trainingInstances.setClassIndex(0);
        IntStream.range(0, trainingSetSize).forEach(i -> trainingInstances.add(instances.get(i)));

        logger.info("Extracting testing instances...");
        Instances testingInstances = new Instances("@@class@@", attributes, testingSetSize);
        testingInstances.setClassIndex(0);
        IntStream.range(trainingSetSize, instances.size()).forEach(i -> testingInstances.add(instances.get(i)));

        ExecutionContext executionContext = new ExecutionContext();
        executionContext.setTraningInstances(trainingInstances);
        executionContext.setTestingInstances(testingInstances);

        return executionContext;
    }
}
