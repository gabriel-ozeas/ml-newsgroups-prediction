package knoma.newsgroup;


import knoma.newsgroup.domain.Group;
import knoma.newsgroup.domain.Message;
import knoma.newsgroup.domain.NewsgroupScenario;
import knoma.newsgroup.preprocessing.GroupReader;
import knoma.newsgroup.preprocessing.MessageDataCleaner;
import knoma.newsgroup.preprocessing.MessageReader;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.nio.file.Files.readAllLines;
import static java.nio.file.Paths.get;
import static java.util.stream.Collectors.toList;

/**
 * Created by gabriel on 01/11/15.
 */
public class DefaultProducer {
    @Inject
    private MessageReader messageReader;
    @Inject
    private GroupReader groupReader;
    @Inject
    private MessageDataCleaner cleaner;

    @Produces
    private NewsgroupScenario scenario(List<Group> groups, List<Message> messages) {
        return new NewsgroupScenario(groups, messages);
    }

    @Produces
    private List<Group> groups() {
        return groupReader.groups("20_newsgroups");
    }

    @Produces
    private List<Message> messages(List<Group> groups) {
        List<Message> messages = groups.stream()
                .flatMap(group -> messageReader.messages(group).stream())
                .filter(message -> message != null)
                .parallel()
                .map(message -> cleaner.clean(message))
                .limit(20000)
                .collect(toList());

        Collections.shuffle(messages);

        return messages.stream().collect(toList());
    }

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
