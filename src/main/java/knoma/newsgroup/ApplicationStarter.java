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
    
    private CompressionUtil compressionUtil;

    public void bootListener(@Observes ContainerInitialized event, @Parameters List<String> cmdLineArgs) throws Exception {
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(cliOptions, cmdLineArgs.toArray(new String[cmdLineArgs.size()]));

        if (commandLine.hasOption("--help") || !(commandLine.hasOption("dataset-dir") || commandLine.hasOption("download-dataset"))) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "20newsgroup", cliOptions);
            return;
        }
        
        if (commandLine.hasOption("dataset-dir")) {
            logger.info("Using dataset in {} dir", commandLine.getOptionValue("dataset-dir"));
            configurations.setDatasetDir(commandLine.getOptionValue("dataset-dir"));
        }
        
        if (commandLine.hasOption("download-dataset")) {
            logger.info("Downloading 20newsgroup dataset...");
            String datasetFile = downloader.download(configurations.datasetUrl());
            logger.info("Extracting dataset {}", datasetFile);
            File uncompressedDir = compressionUtil.untar(new File(datasetFile), new File(new File(datasetFile).getParent()));
            configurations.setDatasetDir(uncompressedDir.getAbsolutePath());
        }

        if (!commandLine.hasOption("experiment")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.setWidth(130);
            formatter.printHelp( "20newsgroup [Options]", cliOptions);
            return;
        }

        RunnableExperiment runnableExperiment = experiments.select(experimentType(commandLine.getOptionValue("experiment"))).get();
        if (runnableExperiment == null) {
            logger.info("No experiment found with name " + commandLine.getOptionValue("experiment"));
        }

        HashMap<String, String> configuration = new HashMap<>();

        Option[] options = commandLine.getOptions();
        for (Option option : options) {
            configuration.put(option.getArgName(), option.getValue());
        }

        runnableExperiment.run(configuration);
    }
}
