package knoma.newsgroup.cli;

import knoma.newsgroup.experiments.Experiment;
import knoma.newsgroup.experiments.RunnableExperiment;
import org.apache.commons.cli.Options;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import static org.apache.commons.cli.Option.builder;

/**
 * Created by gabriel on 25/11/15.
 */
public class OptionsProducer {
    @Inject
    @Any
    private Instance<RunnableExperiment> experiments;

    @Produces
    @ApplicationScoped
    public Options commandLineInterfaceOptions() {
        Options options = new Options();
        options.addOption(builder()
                .longOpt("dataset")
                .desc("Specify the 20 newsgroup dataset path")
                .build());

        options.addOption(builder()
                .longOpt("help")
                .desc("Print this message")
                .build());

        experiments.forEach(experiment -> {
            Experiment annotation = experiment.getClass().getAnnotation(Experiment.class);

            options.addOption(builder().longOpt(annotation.name())
                    .desc(annotation.description())
                    .build());

        });

        return options;
    }
}
