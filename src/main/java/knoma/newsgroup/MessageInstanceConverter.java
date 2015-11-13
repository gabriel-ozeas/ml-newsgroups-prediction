package knoma.newsgroup;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.SparseInstance;

import javax.enterprise.context.ApplicationScoped;
import java.util.stream.IntStream;

/**
 * Created by gabriel on 01/11/15.
 */
@ApplicationScoped
public class MessageInstanceConverter {
    public Instance convert(Message message, FastVector vector) {
        TokenizedMessage tokenizedMessage = (TokenizedMessage) message;

        SparseInstance instance = new SparseInstance(vector.size());
        instance.setValue((Attribute) vector.elementAt(0), message.getGroup().getName());

        IntStream.range(1, vector.size()).forEach(i -> {
            Attribute attribute = (Attribute) vector.elementAt(i);
            instance.setValue(attribute, tokenizedMessage.getTokens().contains(attribute.name()) ? "true" : "false");
        });

        return instance;
    }
}
