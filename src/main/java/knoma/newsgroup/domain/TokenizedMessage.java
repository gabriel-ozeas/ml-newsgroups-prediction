package knoma.newsgroup.domain;

import java.util.List;
import java.util.Map;

/**
 * Created by gabriel on 31/10/15.
 */
public class TokenizedMessage extends Message {
    private List<String> tokens;
    private Message message;

    public TokenizedMessage() {
    }

    public TokenizedMessage(List<String> tokens, Message message) {
        this.tokens = tokens;
        this.message = message;
    }

    public Group getGroup() {return message.getGroup(); }

    public Map<String, String> getHeaders() {
        return message.getHeaders();
    }

    public String getMessage() {
        return message.getMessage();
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }



    @Override
    public String toString() {
        return "TokenizedMessage{" +
                "tokens=" + tokens +
                ", message=" + message +
                '}';
    }
}
