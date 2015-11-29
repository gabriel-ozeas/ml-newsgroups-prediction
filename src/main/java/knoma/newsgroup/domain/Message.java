package knoma.newsgroup.domain;

import java.util.Map;

/**
 * Created by gabriel on 27/10/15.
 */
public class Message {
    private Group group;
    private Map<String, String> headers;
    private String message;

    public Message() {}

    public Message(Group group, Map<String, String> headers, String message) {
        this.group = group;
        this.headers = headers;
        this.message = message;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getMessage() {
        return message;
    }

    public Group getGroup() {
        return group;
    }

    @Override
    public String toString() {
        return "Message{" +
                "group='" + group + '\'' +
                ", headers=" + headers +
                ", message='" + message + '\'' +
                '}';
    }
}
