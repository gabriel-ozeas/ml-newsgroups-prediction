package knoma.newsgroup;

import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static java.nio.file.Files.readAllLines;
import static java.nio.file.Paths.get;

/**
 * Created by gabriel on 28/10/15.
 */
@RunWith(CdiRunner.class)
@AdditionalClasses({DefaultProducer.class})
public class MessageDataCleanerTest {
    private Message message;
    @Inject
    private MessageDataCleaner cleaner;

    @Before
    public void setUp() throws Exception {
        message = new MessageParser().parse(null, readAllLines(get("20_newsgroups/comp.graphics/37261")).stream());
    }

    @Test
    public void clearningTestWithSuccess() throws Exception {
        Message cleared = cleaner.clean(message);

        System.out.println(message.getMessage());
        System.out.println(cleared.getMessage());
    }
}
