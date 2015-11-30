package knoma.newsgroup.cli;

import org.apache.commons.cli.Options;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * Created by gabriel on 25/11/15.
 */
public class OptionsProducer {
    @Produces
    @ApplicationScoped
    public Options commandLineInterfaceOptions() {
        Options options = new Options();
        options.addOption("dataset", true, "Specify the 20 newsgroup dataset path");
        options.addOption("experiment", true, "Name of the experiment that will be run");
        options.addOption("help", "Print this message");
        return options;
    }
}
