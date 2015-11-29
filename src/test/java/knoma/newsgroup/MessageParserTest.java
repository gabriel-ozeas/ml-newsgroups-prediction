package knoma.newsgroup;

import knoma.newsgroup.domain.Message;
import knoma.newsgroup.preprocessing.MessageParser;
import org.junit.Assert;
import org.junit.Test;

import static java.nio.file.Files.readAllLines;
import static java.nio.file.Paths.get;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 * Created by gabriel on 27/10/15.
 */
public class MessageParserTest {

    @Test
    public void readMessageFromFileWithSuccess() throws Exception {
        Message message = new MessageParser().parse(null, readAllLines(get("20_newsgroups/comp.graphics/37261")).stream());
        Assert.assertThat(message.getHeaders().size(), equalTo(14));
    }
}
