package knoma.newsgroup;

import knoma.newsgroup.domain.Message;
import knoma.newsgroup.preprocessing.MessageParser;
import org.junit.Test;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.file.Files.readAllLines;
import static java.nio.file.Paths.get;
import static org.junit.Assert.assertNotNull;

/**
 * Created by gabriel on 27/10/15.
 */
public class MessageParserTest {
    @Test
    public void readMessageFromFileWithSuccess() throws Exception {
        Message message = new MessageParser().parse(null, readAllLines(get("src/test/resources/fixture/alt-atheism/49960"), ISO_8859_1).stream());
        assertNotNull(message);
    }
}
