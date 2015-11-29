package knoma.newsgroup.domain;

import java.util.List;

/**
 * Created by gabriel on 25/11/15.
 */
public class NewsgroupScenario {
    private List<Group> groups;
    private List<Message> messages;

    public NewsgroupScenario(java.util.List<Group> groups, java.util.List<Message> messages) {
        this.groups = groups;
        this.messages = messages;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
