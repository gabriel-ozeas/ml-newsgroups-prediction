package knoma.newsgroup;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import static java.lang.System.getProperty;

/**
 * Created by gabriel on 12/3/15.
 */
public class DatasetDownloader {
    public String download(String url) throws Exception {
        URL website = new URL(url);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        File file = new File(getProperty("java.io.tmpdir")  + File.separator +  "20news-19997.tar.gz");
        FileOutputStream fos = new FileOutputStream(new File(getProperty("java.io.tmpdir") + File.separator + "20news-19997.tar.gz"));
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        return file.getAbsolutePath();
    }
}
