package knoma.newsgroup.classifiers;

/**
 * Created by gabriel on 12/2/15.
 */
public class ClassifierConfiguration {
    private int attributeSize;

    public ClassifierConfiguration(int attributeSize) {
        this.attributeSize = attributeSize;
    }

    public int getAttributeSize() {
        return attributeSize;
    }
}
