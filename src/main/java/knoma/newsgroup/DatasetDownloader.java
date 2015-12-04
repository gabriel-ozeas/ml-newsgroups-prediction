package knoma.newsgroup;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import static java.lang.System.getProperty;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

/**
 * Created by gabriel on 12/3/15.
 */
public class DatasetDownloader {
    private static final Logger logger = LogManager.getLogger(DatasetDownloader.class.getName());

    private static final String DATASET_HASH = "36baeea1b2b4865fbab029f664da173e";

    public String download(String url, String fileName, String dir, boolean alwaysDownload) throws Exception {
        logger.info("Downloading {} to {}...", fileName, dir);

        URL website = new URL(url);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        File file = new File(dir  + File.separator +  fileName);

        if (!alwaysDownload && file.exists()) {
            if (checkConsistency(file)) {
                return file.getAbsolutePath();
            } else {
                file.delete();
            }
        }

        FileOutputStream fos = new FileOutputStream(new File(getProperty("java.io.tmpdir") + File.separator + fileName));
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        return file.getAbsolutePath();
    }

    private boolean checkConsistency(File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        String md5 = md5Hex(fis);
        fis.close();
        return DATASET_HASH.equals(md5);
    }
}
