package knoma.newsgroup.util;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

/**
 * Created by gabriel on 12/3/15.
 */
public class CompressionUtil {
    private static final Logger logger = LogManager.getLogger(CompressionUtil.class.getName());

    public File untar(File zipFile, File outputFolder) throws Exception {
        outputFolder.mkdir();
        TarArchiveInputStream tarIn = null;

        tarIn = new TarArchiveInputStream(
                new GzipCompressorInputStream(
                        new BufferedInputStream(
                                new FileInputStream(
                                        zipFile
                                )
                        )
                )
        );

        TarArchiveEntry tarEntry = tarIn.getNextTarEntry();

        File firstPath = new File(outputFolder, tarEntry.getName());

        // tarIn is a TarArchiveInputStream
        while (tarEntry != null) {// create a file with the same name as the tarEntry
            File destPath = new File(outputFolder, tarEntry.getName());
            if (tarEntry.isDirectory()) {
                destPath.mkdirs();
            } else {
                destPath.createNewFile();
                //byte [] btoRead = new byte[(int)tarEntry.getSize()];
                byte [] btoRead = new byte[1024];
                //FileInputStream fin
                //  = new FileInputStream(destPath.getCanonicalPath());
                BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(destPath));
                int len = 0;

                while((len = tarIn.read(btoRead)) != -1)
                {
                    bout.write(btoRead,0,len);
                }

                bout.close();
                btoRead = null;

            }
            tarEntry = tarIn.getNextTarEntry();
        }
        tarIn.close();

        return firstPath;
    }
}
