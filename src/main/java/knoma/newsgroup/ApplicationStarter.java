package knoma.newsgroup;

import java.util.List;

import knoma.newsgroup.classifiers.ClassifierBuilder;
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

/**
 * Created by gabriel on 01/11/15.
 */
@ApplicationScoped
public class ApplicationStarter {
    private static final Logger logger = LogManager.getLogger(ApplicationStarter.class.getName());

    @Inject
    @Any
    private Instance<ClassifierBuilder> classifierBuilder;

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

        String classifier = commandLine.hasOption("classifier") ? commandLine.getOptionValue("classifier") : "naive-bayes";

        ClassifierBuilder builder = classifierBuilder
                    .select(classifierType(classifier))
                    .get();

        builder.buildAndEvaluate(100);
    }
}
