package knoma.newsgroup;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by gabriel on 01/11/15.
 */

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface StopWords {
}
