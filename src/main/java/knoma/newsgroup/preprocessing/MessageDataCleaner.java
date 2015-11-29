package knoma.newsgroup.preprocessing;

import knoma.newsgroup.domain.Message;
import knoma.newsgroup.StopWords;
import knoma.newsgroup.domain.TokenizedMessage;
import weka.core.stemmers.LovinsStemmer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

/**
 * Created by gabriel on 28/10/15.
 */
@ApplicationScoped
public class MessageDataCleaner {
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Inject @StopWords
    private List<String> stopwords;

    public Message clean(Message message) {
        String text = message.getMessage()
                .toLowerCase()
                .replaceAll(EMAIL_PATTERN, "EMAIL")
                .replaceAll("/\\(?([0-9]{3})\\)?([ .-]?)([0-9]{3})\\2([0-9]{4})/", "PHONE")
                .replaceAll(" [0-9]{5}(?:-[0-9]{4})? ", "ZIPCODE")
                .replaceAll("[^\\w\\s]", " ")
                .replaceAll("[_0-9]+", " ")
                .replaceAll(" [a-z] ", " ");

        text = stopwords.stream().reduce(text, (t, word) -> t.replace(" " + word + " ", " "));

        LovinsStemmer stemmer = new LovinsStemmer();

        List<String> tokens = asList(text.split(" "))
                .stream()
                .map(word -> word.trim())
                .filter(word -> !"".equals(word))
                .map(word -> stemmer.stem(word))
                .collect(toList());

        return new TokenizedMessage(tokens, message);
    }

    public List<String> getStopwords() {
        return stopwords;
    }

    public void setStopwords(List<String> stopwords) {
        this.stopwords = stopwords;
    }
}
