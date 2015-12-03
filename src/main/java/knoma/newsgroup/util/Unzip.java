package knoma.newsgroup.util;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by gabriel on 12/3/15.
 */
public class Unzip {
    private static final Logger logger = LogManager.getLogger(Unzip.class.getName());
    public File unzip(File zipFile, File outputFolder) throws Exception {
        final File outputFile = new File(outputFolder, zipFile.getName().substring(0, zipFile.getName().length() - 3));

        final GZIPInputStream in = new GZIPInputStream(new FileInputStream(zipFile));
        final FileOutputStream out = new FileOutputStream(outputFile);

        IOUtils.copy(in, out);

        in.close();
        out.close();

        return outputFile;
    }
}
