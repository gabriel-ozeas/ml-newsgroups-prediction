package knoma.newsgroup.domain;

import weka.core.Instances;

/**
 * Created by gabriel on 12/2/15.
 */
public class ExecutionContext {
    private Instances traningInstances;
    private Instances testingInstances;

    public Instances getTraningInstances() {
        return traningInstances;
    }

    public void setTraningInstances(Instances traningInstances) {
        this.traningInstances = traningInstances;
    }

    public Instances getTestingInstances() {
        return testingInstances;
    }

    public void setTestingInstances(Instances testingInstances) {
        this.testingInstances = testingInstances;
    }
}
