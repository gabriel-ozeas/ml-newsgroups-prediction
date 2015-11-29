package knoma.newsgroup.preprocessing;

import knoma.newsgroup.domain.Group;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.List;
import static java.util.stream.Collectors.toList;

/**
 * Created by gabriel on 03/11/15.
 */
@ApplicationScoped
public class GroupReader {
    public List<Group> groups(String dir) {
        try(Stream<Path> stream = Files.walk(Paths.get(dir), 1)) {
            return stream
                    .filter(path -> !path.toString().contains(File.separator + "."))
                    .filter(path -> !path.toFile().getName().equals(dir))
                    .map(path -> path.toFile())
                    .filter(file -> file.isDirectory())
                    .map(file -> new Group(file.getName(), file.getAbsolutePath()))
                    .collect(toList());
        } catch (IOException e) {
            throw new RuntimeException("Cannot load groups", e);
        }
    }
}
