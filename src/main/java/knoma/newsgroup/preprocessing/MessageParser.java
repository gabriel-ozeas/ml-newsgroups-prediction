package knoma.newsgroup.preprocessing;


import knoma.newsgroup.domain.Group;
import knoma.newsgroup.domain.Message;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Created by gabriel on 27/10/15.
 */
@ApplicationScoped
public class MessageParser {
    public Message parse(Group group, Stream<String> lines) {
        Map<String, String> headers = new HashMap<>();

        int startIndex = 0;
        List<String> collect = lines.collect(toList());
        for (int i = 0; i < collect.size(); i++) {
            if (!collect.get(i).trim().equals("")) {
                String[] header = collect.get(i).split(":", 2);
                if (header.length == 2) {
                    headers.put(header[0].trim().toLowerCase(), header[1].trim());
                }
                continue;
            }

            startIndex = i + 1;
            break;
        }

        StringBuilder message = new StringBuilder();
        for (int i = startIndex; i < collect.size(); i++) {
            message.append(collect.get(i).trim()).append(" ");
        }

        return new Message(group, headers, message.toString());
    }
}
