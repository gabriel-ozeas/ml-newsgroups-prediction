package knoma.newsgroup.classifiers;

/**
 * Created by gabriel on 28/11/15.
 */

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface Classifier {
    String value();
}
