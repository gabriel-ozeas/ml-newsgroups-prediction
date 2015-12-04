package knoma.newsgroup;

import org.jboss.weld.environment.se.StartMain;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by gabriel on 01/11/15.
 */
public class CDIStarterTest {
    @Test
    @Ignore
    public void testName() throws Exception {
        StartMain.main(new String[]{"--download-dataset", "--naive-bayes-experiment"});
        while(true){Thread.sleep(1000);}
    }
}
