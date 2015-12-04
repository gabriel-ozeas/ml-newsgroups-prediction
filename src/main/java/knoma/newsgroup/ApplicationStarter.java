package knoma.newsgroup;

import knoma.newsgroup.experiments.RunnableExperiment;
import knoma.newsgroup.util.CompressionUtil;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.weld.environment.se.bindings.Parameters;
import org.jboss.weld.environment.se.events.ContainerInitialized;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.File;
import java.util.HashMap;
import java.util.List;

import static java.lang.System.getProperty;
import static knoma.newsgroup.experiments.ExperimentLiteral.experimentType;

/**
 * Created by gabriel on 01/11/15.
 */
@ApplicationScoped
public class ApplicationStarter {
    private static final Logger logger = LogManager.getLogger(ApplicationStarter.class.getName());

    @Inject
    @Any
    private Instance<RunnableExperiment> experiments;

    @Inject
    private Options cliOptions;

    @Inject
    private ConfigurationsProducer configurations;
    
    @Inject
    private DatasetDownloader downloader;

    @Inject
    private CompressionUtil compressionUtil;

    public void bootListener(@Observes ContainerInitialized event, @Parameters List<String> cmdLineArgs) throws Exception {
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(cliOptions, cmdLineArgs.toArray(new String[cmdLineArgs.size()]));

        if (commandLine.hasOption("--help") || !(commandLine.hasOption("dataset-dir") || commandLine.hasOption("download-dataset"))) {
            printHelp();
            return;
        }
        
        if (commandLine.hasOption("dataset-dir")) {
            logger.info("Using dataset in {} dir", commandLine.getOptionValue("dataset-dir"));
            configurations.setDatasetDir(commandLine.getOptionValue("dataset-dir"));
        }
        
        if (commandLine.hasOption("download-dataset")) {
            String datasetFile = downloader.download(configurations.datasetUrl(), "20news-19997.tar.gz", getProperty("java.io.tmpdir"), false);
            logger.info("Extracting dataset {}", datasetFile);
            File uncompressedDir = compressionUtil.untar(new File(datasetFile), new File(new File(datasetFile).getParent()));
            configurations.setDatasetDir(uncompressedDir.getAbsolutePath());
        }

        String stopwordsFile = downloader.download(configurations.stopWordsUrl(), "common-english-words.txt", getProperty("java.io.tmpdir"), true);
        configurations.setStopWordsFile(stopwordsFile);

        HashMap<String, String> configuration = new HashMap<>();

        Option[] options = commandLine.getOptions();
        for (Option option : options) {
            configuration.put(option.getLongOpt(), option.getValue());
        }

        String experiment = configuration.keySet()
                .stream()
                .filter(parameter -> parameter.endsWith("-experiment"))
                .findFirst()
                .map(parameter -> parameter.replace("-experiment", ""))
                .orElse(null);

        if (experiment == null) {
            logger.error("Cannot find experiment");
            printHelp();
            return;
        }

        RunnableExperiment runnableExperiment = experiments.select(experimentType(experiment)).get();
        if (runnableExperiment == null) {
            logger.info("No experiment found with name {}", experiment);
            printHelp();
            return;
        }


        runnableExperiment.run(configuration);
    }

    private void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(130);
        formatter.printHelp("20newsgroup [Options]", cliOptions);
    }
}
