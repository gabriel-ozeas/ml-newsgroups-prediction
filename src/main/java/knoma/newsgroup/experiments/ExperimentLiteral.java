package knoma.newsgroup.experiments;

import javax.enterprise.util.AnnotationLiteral;

/**
 * Created by gabriel on 29/11/15.
 */
public class ExperimentLiteral extends AnnotationLiteral<Experiment> implements Experiment {
    private String value;

    public ExperimentLiteral(String value) {
        this.value = value;
    }

    public static ExperimentLiteral experimentType(String name) {
        return new ExperimentLiteral(name);
    }

    @Override
    public String value() {
        return value;
    }
}
