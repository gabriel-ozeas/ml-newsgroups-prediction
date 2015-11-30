package knoma.newsgroup.experiments;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by gabriel on 29/11/15.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface Experiment {
    String value() default "";
}
