package knoma.newsgroup;


import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;

import static java.nio.file.Files.readAllLines;
import static java.nio.file.Paths.get;

/**
 * Created by gabriel on 01/11/15.
 */
public class DefaultProducer {
    @Produces
    @ApplicationScoped
    @StopWords
    public List<String> stopWords() {
        try {
            List<String> lines = readAllLines(get("src/main/resources/stopwords.txt"));
            return Arrays.asList(lines.get(0).split(","));
        } catch (IOException e) {
            throw new RuntimeException("Cannot list stop words.", e);
        }
    }
}
