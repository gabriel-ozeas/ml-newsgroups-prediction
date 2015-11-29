package knoma.newsgroup.classifiers;

/**
 * Created by gabriel on 28/11/15.
 */
public interface ClassifierBuilder {
    public void buildAndEvaluate(int numberOfWords) throws Exception;
}
