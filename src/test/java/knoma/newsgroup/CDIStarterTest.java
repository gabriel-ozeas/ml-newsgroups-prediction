package knoma.newsgroup;

import org.jboss.weld.environment.se.StartMain;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by gabriel on 01/11/15.
 */
public class CDIStarterTest {
    @Test
    public void testName() throws Exception {
        StartMain.main(new String[]{"-experiment", "run-adaboost"});
        while(true){Thread.sleep(1000);}
    }
}
