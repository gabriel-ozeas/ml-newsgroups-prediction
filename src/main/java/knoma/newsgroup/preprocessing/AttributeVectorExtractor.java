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

    public FastVector extractCategoryAttribute(List<Group> groups) {
        FastVector categoryVector = new FastVector(20);
        groups.stream().forEach(group -> categoryVector.addElement(group.getName()));

        return categoryVector;
    }


    public FastVector extract(BagOfWords bagOfWords, FastVector groups){
        final FastVector vector = new FastVector();

        Attribute newsgroup = new Attribute("@@class@@", groups);
        vector.addElement(newsgroup);

        bagOfWords
                .getVocabulary()
                .stream()
                .map(word -> new Attribute(word))
                .forEach(attribute -> vector.addElement(attribute));

        return vector;
    }
}
