package knoma.newsgroup.preprocessing;

import knoma.newsgroup.domain.BagOfWords;
import knoma.newsgroup.domain.Group;
import weka.core.Attribute;
import weka.core.FastVector;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

/**
 * Created by gabriel on 12/2/15.
 */
@ApplicationScoped
public class AttributeVectorExtractor {
    public FastVector extract(BagOfWords bagOfWords, List<Group> groups){
        final FastVector vector = new FastVector();

        FastVector categoryVector = new FastVector(20);
        groups.stream().forEach(group -> categoryVector.addElement(group.getName()));

        Attribute newsgroup = new Attribute("@@class@@", categoryVector);
        vector.addElement(newsgroup);

        bagOfWords
                .getVocabulary()
                .stream()
                .map(word -> new Attribute(word))
                .forEach(attribute -> vector.addElement(attribute));

        return vector;
    }
}
