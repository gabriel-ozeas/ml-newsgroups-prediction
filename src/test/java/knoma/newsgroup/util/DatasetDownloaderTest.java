package knoma.newsgroup.util;

import knoma.newsgroup.DatasetDownloader;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by gabriel on 12/3/15.
 */
public class DatasetDownloaderTest {
    @Test
    @Ignore
    public void downloadingWithSuccess() throws Exception {
        new DatasetDownloader().download("http://qwone.com/~jason/20Newsgroups/20news-19997.tar.gz");
    }
}
