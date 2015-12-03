package knoma.newsgroup.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by gabriel on 12/3/15.
 */
public class BagOfWordsBuilder {
    private static final Logger logger = LogManager.getLogger(BagOfWords.class.getName());

    private List<Message> messages;
    private int size;

    public static BagOfWordsBuilder newBagOfWords() {
        return new BagOfWordsBuilder();
    }

    public BagOfWordsBuilder messages(List<Message> messages) {
        this.messages = messages;
        return this;
    }

    public BagOfWordsBuilder size(int size) {
        this.size = size;
        return this;
    }

    public BagOfWords build() {
        BagOfWords bagOfWords = new BagOfWords(size);
        messages.stream()
                .flatMap(message -> ((TokenizedMessage) message).getTokens().stream())
                .forEach(word -> bagOfWords.count(word));
        return bagOfWords;
    }
}
