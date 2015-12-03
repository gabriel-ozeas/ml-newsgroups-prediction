package knoma.newsgroup.util;

import org.junit.Test;

import java.io.File;

/**
 * Created by gabriel on 12/3/15.
 */
public class CompressionUtilTest {
    @Test
    public void unzipWithSuccess() throws Exception {
        new CompressionUtil().untar(new File("/tmp/20news-19997.tar.gz"), new File("/tmp"));
    }
}
