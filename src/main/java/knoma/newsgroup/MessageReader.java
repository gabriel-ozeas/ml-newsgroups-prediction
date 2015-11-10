package knoma.newsgroup;


import java.io.File;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.nio.file.Files.lines;
import static java.util.stream.Collectors.toList;

/**
 * Created by gabriel on 27/10/15.
 */
@ApplicationScoped
public class MessageReader {
    @Inject
    private MessageParser parser;

    public List<Message> messages(Group group) {
        try(Stream<Path> stream = Files.walk(Paths.get(group.getPath()), 1)) {
            return stream
                    .filter(path -> !path.toString().contains(File.separator + "."))
                    .filter(file -> file.toFile().isFile())
                    .filter(file -> !file.toFile().getName().startsWith("."))
                    .map(file -> {
                        try(Stream<String> lines = lines(file, Charset.forName("ISO-8859-1"))) {
                            return parser.parse(group, lines);
                        } catch (IOException e) {
                            throw new RuntimeException("Cannot list files in directory " + group.getPath());
                        }
                    })
                    .collect(toList());
        } catch (IOException e) {
            throw new RuntimeException("Cannot list files in directory " + group.getPath());
        }
    }
}
