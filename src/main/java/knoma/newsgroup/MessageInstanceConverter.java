package knoma.newsgroup;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.SparseInstance;

import javax.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Arrays.sort;
import static java.util.stream.Collectors.toList;

/**
 * Created by gabriel on 01/11/15.
 */
@ApplicationScoped
public class MessageInstanceConverter {
    public Instance convert(Message message, BagOfWords bagOfWords, FastVector categoryVector) {
        TokenizedMessage tokenizedMessage = (TokenizedMessage) message;

        List<String> vocabulary = bagOfWords.getVocabulary();

        List<String> words = tokenizedMessage
                .getTokens()
                .stream()
                .filter(word -> vocabulary.contains(word))
                .distinct()
                .collect(toList());

        double[] values = new double[words.size() + 1];
        int[] indexes = new int[words.size() + 1];

        values[0] = categoryVector.indexOf(message.getGroup().getName());
        indexes[0] = 0;

        for (int i = 0; i < words.size(); i++) {
            int index = vocabulary.indexOf(words.get(i)) + 1;
            indexes[i + 1] = index;
            values[i + 1] = 1;
        }

        sort(indexes);

        SparseInstance instance = new SparseInstance(1, values, indexes, values.length);

        return instance;
    }
}
