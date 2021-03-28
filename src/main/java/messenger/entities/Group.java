package messenger.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Group {

    @Id
    @GeneratedValue
    private String id;

    @ElementCollection
    private List<String> users = new ArrayList();

    @ElementCollection
    private List<String> messages = new ArrayList();

    public Group() {
    }

    public Group(List<String> users, List<String> messages) {
        this.users = users;
        this.messages = messages;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(id, group.id) && Objects.equals(users, group.users) && Objects.equals(messages, group.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, users, messages);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id='" + id + '\'' +
                ", users=" + users +
                ", messages=" + messages +
                '}';
    }

    public void addMessage(String message) {
        this.messages.add(message);
    }

    public void removeMessage(String message) {
        this.messages.remove(message);
    }

    public void addUser(String userId) {
        this.users.add(userId);
    }

    public void removeUser(String userId) {
        this.users.remove(userId);

    }}
