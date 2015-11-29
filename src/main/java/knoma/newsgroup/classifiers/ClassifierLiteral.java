package knoma.newsgroup.classifiers;

import javax.enterprise.util.AnnotationLiteral;

/**
 * Created by gabriel on 28/11/15.
 */
public class ClassifierLiteral extends AnnotationLiteral<Classifier> implements Classifier {
    private String value;

    public ClassifierLiteral(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }

    public static ClassifierLiteral classifierType(String name) {
        return new ClassifierLiteral(name);
    }
}
