package knoma.newsgroup;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

import knoma.newsgroup.classifiers.ClassifierBuilder;
import knoma.newsgroup.experiments.RunnableExperiment;
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

import static knoma.newsgroup.classifiers.ClassifierLiteral.classifierType;
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

    public void bootListener(@Observes ContainerInitialized event, @Parameters List<String> cmdLineArgs) throws Exception {
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(cliOptions, cmdLineArgs.toArray(new String[cmdLineArgs.size()]));

        if (commandLine.hasOption("-help")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "20newsgroup", cliOptions);
            return;
        }

        if (!commandLine.hasOption("experiment")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.setWidth(130);
            formatter.printHelp( "20newsgroup -experiment find-best-number-of-attributes", cliOptions);
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
