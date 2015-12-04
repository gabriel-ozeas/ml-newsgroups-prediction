package knoma.newsgroup.preprocessing;

import knoma.newsgroup.domain.BagOfWords;
import knoma.newsgroup.domain.Message;
import knoma.newsgroup.domain.TokenizedMessage;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.SparseInstance;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

import static java.util.Arrays.sort;
import static java.util.stream.Collectors.toList;

/**
 * Created by gabriel on 01/11/15.
 */
@ApplicationScoped
public class MessageInstanceConverter {

    public List<Instance> convert(List<Message> messages, BagOfWords bagOfWords, FastVector categoryVector) {
        return messages
                .parallelStream()
                .map(message -> convert(message, bagOfWords, categoryVector))
                .filter(instance -> instance != null)
                .collect(toList());
    }

    public Instance convert(Message message, BagOfWords bagOfWords, FastVector categoryVector) {
        TokenizedMessage tokenizedMessage = (TokenizedMessage) message;

        List<String> vocabulary = bagOfWords.getVocabulary();

        List<String> words = tokenizedMessage
                .getTokens()
                .stream()
                .filter(word -> vocabulary.contains(word))
                .distinct()
                .collect(toList());

        if (words.isEmpty() || message.getGroup() == null) {
            return null;
        }

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

        return new SparseInstance(1, values, indexes, values.length);
    }
}
