package knoma.newsgroup;

import knoma.newsgroup.domain.Message;
import knoma.newsgroup.domain.TokenizedMessage;
import knoma.newsgroup.preprocessing.MessageDataCleaner;
import knoma.newsgroup.preprocessing.MessageParser;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.inject.Inject;


import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.file.Files.readAllLines;
import static java.nio.file.Paths.get;
import static org.junit.Assert.assertTrue;

/**
 * Created by gabriel on 28/10/15.
 */
//@RunWith(CdiRunner.class)
//@AdditionalClasses({DefaultProducer.class})
    @Ignore
public class MessageDataCleanerTest {
    private Message message;
    @Inject
    private MessageDataCleaner cleaner;

    @Before
    public void setUp() throws Exception {
        message = new MessageParser().parse(null, readAllLines(get("src/test/resources/fixture/alt-atheism/49960"), ISO_8859_1).stream());
    }

    @Test
    public void clearningTestWithSuccess() throws Exception {
        TokenizedMessage tokenizedMessage = (TokenizedMessage) cleaner.clean(message);
        assertTrue(tokenizedMessage.getTokens().size() > 0);
    }
}
